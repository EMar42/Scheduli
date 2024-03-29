package com.example.scheduli.ui.signup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;
import com.example.scheduli.data.User;
import com.example.scheduli.data.repositories.StorageRepository;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.utils.SignUpNotification;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG_SIGN_UP = "Sign-up Activity";
    private static final int PERMISSION_REQUEST_PHONE = 0;
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    private UsersUtils usersUtils;
    private EditText userEmail, userName, userPassword, userFullName, userPhoneNumber;
    private Button signUpToApp, returnToLogin, selectProfileImage;
    private UserDataRepository userDataRepository;
    private ImageButton getCurrentPhoneButton;
    private ImageView profilePreview;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //setup utils for the activity
        usersUtils = UsersUtils.getInstance();
        userDataRepository = UserDataRepository.getInstance();
        initView();

        //setup buttons
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        getCurrentPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoneNumberOnCurrent();
            }
        });

        selectProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, PERMISSION_REQUEST_STORAGE);

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SignupActivity.this, "Cannot pull image from gallery without permissions", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            }
        });
    }

    private void setPhoneNumberOnCurrent() {
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,}, PERMISSION_REQUEST_PHONE); //request permission to read phone number

        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            userPhoneNumber.setError("No premissions to get phone number");
            return;
        } else {
            userPhoneNumber.setText(tMgr.getLine1Number());
        }

    }

    private void createNewAccount() {
        raiseErrorsOnMissingOrIncorrectInput();

        if (isFormValid()) {
            final String email = userEmail.getText().toString();
            final String userNameString = userName.getText().toString();
            final String fullName = userFullName.getText().toString();
            final String phoneNumber = userPhoneNumber.getText().toString();

            usersUtils.getFireBaseAuth().createUserWithEmailAndPassword(email, userPassword.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG_SIGN_UP, "Cannot create account ");
                        Toast.makeText(SignupActivity.this, "Sign-up failed", Toast.LENGTH_SHORT).show();
                    } else {
                        AuthResult result = task.getResult();
                        String uid = result.getUser().getUid();
                        writeNewUserToDataBase(uid, email, userNameString, fullName, phoneNumber);
                        if (imageBitmap != null) {
                            StorageRepository.getInstance().uploadBitmapImageToFireBase(uid, "profile.jpg", imageBitmap);
                        }
                        SignUpNotification.notify(getApplicationContext(), fullName, 1);
                        finish();
                    }
                }
            });

        }

    }

    private void writeNewUserToDataBase(String uid, String email, String userName, String fullName, String phoneNumber) {
        User user = new User(userName, fullName, email, phoneNumber, null);
        userDataRepository.createNewUserInApp(uid, user);
    }

    private boolean isFormValid() {
        return !checkIfEmpty(userEmail) && isEmailValid(userEmail.getText().toString()) && !checkIfEmpty(userPassword) && !checkIfEmpty(userName) && !checkIfEmpty(userFullName) && userPassword.getText().toString().length() >= 6
                && !checkIfEmpty(userPhoneNumber) && isPhoneValid(userPhoneNumber.getText().toString());
    }

    private void raiseErrorsOnMissingOrIncorrectInput() {
        if (checkIfEmpty(userEmail))
            userEmail.setError("Fill Missing email address");
        else if (!isEmailValid(userEmail.getText().toString()))
            userEmail.setError("Email format is incorrect");
        if (checkIfEmpty(userPassword))
            userPassword.setError("You must fill password");
        if (userPassword.getText().toString().length() < 6)
            userPassword.setError("Password need to be at least 6 characters");
        if (checkIfEmpty(userFullName))
            userFullName.setError("You need to give full name");
        if (checkIfEmpty(userName))
            userName.setError("You need to give user name");
        if (checkIfEmpty(userPhoneNumber))
            userPhoneNumber.setError("You need to give phone number");
        else if (!isPhoneValid(userPhoneNumber.getText().toString())) {
            userPhoneNumber.setError("Phone number provided is invalid");
        }
    }

    private boolean isPhoneValid(String phoneNumber) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber) || Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private void initView() {
        signUpToApp = findViewById(R.id.btn_sign_up_create);
        returnToLogin = findViewById(R.id.btn_sign_up_return);
        selectProfileImage = findViewById(R.id.btn_signup_select_profile_image);
        getCurrentPhoneButton = findViewById(R.id.btn_sign_up_getCurrentPhoneNumber);
        userEmail = findViewById(R.id.et_sign_up_email);
        userFullName = findViewById(R.id.et_sign_up_full_name);
        userName = findViewById(R.id.et_sign_up_user_name);
        userPassword = findViewById(R.id.et_sign_up_password);
        userPhoneNumber = findViewById(R.id.et_sign_up_phone_number);
        profilePreview = findViewById(R.id.signup_profile_preview);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            new AsyncTask<Intent, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Intent... intents) {
                    Uri selectedImage = intents[0].getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    return BitmapFactory.decodeFile(picturePath);
                }

                @Override
                protected void onPostExecute(Bitmap image) {
                    imageBitmap = image;
                    profilePreview.setImageBitmap(image);
                }
            }.execute(data);

        }
    }
}

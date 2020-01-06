package com.example.scheduli.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;
import com.example.scheduli.data.User;
import com.example.scheduli.data.UserDataRepository;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG_SIGN_UP = "Sign-up Activity";

    private UsersUtils usersUtils;
    private EditText userEmail, userName, userPassword, userFullName;
    private Button signUpToApp, returnToLogin;
    private UserDataRepository userDataRepository;

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
    }

    private void createNewAccount() {
        raiseErrorsOnMissingOrIncorrectInput();

        if (isFormValid()) {
            final String email = userEmail.getText().toString();
            final String userNameString = userName.getText().toString();
            final String fullName = userFullName.getText().toString();

            usersUtils.getFireBaseAuth().createUserWithEmailAndPassword(email, userPassword.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG_SIGN_UP, "Cannot create account ");
                        Toast.makeText(SignupActivity.this, "Sign-up failed", Toast.LENGTH_SHORT).show();
                    } else {
                        AuthResult result = task.getResult();
                        String uid = result.getUser().getUid();
                        writeNewUserToDataBase(uid, email, userNameString, fullName);
                        //TODO add notification for new account creation
                        finish();
                    }
                }
            });

        }

    }

    private void writeNewUserToDataBase(String uid, String email, String userName, String fullName) {
        User user = new User(userName, fullName, email);
        userDataRepository.createNewUserInApp(uid, user);
    }

    private boolean isFormValid() {
        return !checkIfEmpty(userEmail) && isEmailValid(userEmail.getText().toString()) && !checkIfEmpty(userPassword) && !checkIfEmpty(userName) && !checkIfEmpty(userFullName) && userPassword.getText().toString().length() >= 6;
    }

    private void raiseErrorsOnMissingOrIncorrectInput() {
        if (checkIfEmpty(userEmail))
            userEmail.setError("Fill Missing email address");
        else if (!isEmailValid(userEmail.getText().toString()))
            userEmail.setError("Email format is incorrect");
        if (checkIfEmpty(userPassword))
            userPassword.setError("Must fill password");
        if (userPassword.getText().toString().length() < 6)
            userPassword.setError("Password need to be at least 6 characters");
        if (checkIfEmpty(userFullName))
            userFullName.setError("Need to give full name");
        if (checkIfEmpty(userName))
            userName.setError("Need to give user name");
    }

    private void initView() {
        signUpToApp = findViewById(R.id.btn_sign_up_create);
        returnToLogin = findViewById(R.id.btn_sign_up_return);
        userEmail = findViewById(R.id.et_sign_up_email);
        userFullName = findViewById(R.id.et_sign_up_full_name);
        userName = findViewById(R.id.et_sign_up_user_name);
        userPassword = findViewById(R.id.et_sign_up_password);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
}

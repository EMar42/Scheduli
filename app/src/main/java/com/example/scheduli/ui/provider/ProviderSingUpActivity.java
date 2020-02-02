package com.example.scheduli.ui.provider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.utils.UsersUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderSingUpActivity extends BaseMenuActivity {

    private static final String TAG_PROVIDER_SINGUP = "Provider Sing-Up";
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;


    private FirebaseDatabase database;
    private DatabaseReference ref;

    private EditText providerCompanyName, providerProfession, providerPhoneNumber, providerAddress;
    private ImageView providerProfilePicture;
    private Button singUpProviderButton;
    private Button backToProfileButton;
    private UsersUtils usersUtils;
    private Bitmap imageBitmap;


    public static ProviderSingUpActivity newInstance() {
        return new ProviderSingUpActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_singup);

        usersUtils = UsersUtils.getInstance();
        initView();


        singUpProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUpNewProvider();

                Intent intent = new Intent(getBaseContext(), ProviderSingUpActivity.class);
                startActivity(intent);
            }
        });

        backToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToProfile();
            }
        });

        initView();
    }

    @Override
    public void onBackPressed() {
        if (!isFormClear()) {
            backToProfile();
        } else
            super.onBackPressed();
    }

    private void initView() {
        Log.i(TAG_PROVIDER_SINGUP, "initView()");
        providerCompanyName = findViewById(R.id.ed_provider_company_name);
        providerProfession = findViewById(R.id.ed_provider_profession);
        providerPhoneNumber = findViewById(R.id.ed_provider_phone_number);
        providerAddress = findViewById(R.id.ed_provider_address);
        providerProfilePicture = findViewById(R.id.im_provider_pic);
        singUpProviderButton = findViewById(R.id.btn_sign_up_create_provider);
        backToProfileButton = findViewById(R.id.btn_return_profile_screen);
        Log.i(TAG_PROVIDER_SINGUP, "finished initView() ");
    }

    private void singUpNewProvider() {
        Log.i(TAG_PROVIDER_SINGUP, "singUpNewProvider()");
        displayErrorToUserIfThereIsOne();

        if (!checkIfInputValid()) {
            final String companyName = providerCompanyName.getText().toString();
            final String profession = providerProfession.getText().toString();
            final String phone = providerPhoneNumber.getText().toString();
            final String address = providerAddress.getText().toString();
            Provider provider = new Provider(companyName, profession, phone, address);
            String id = UsersUtils.getInstance().getCurrentUserUid();
            //TODO sing up to server
//            ref = FirebaseDatabase.getInstance().getReference().child("providers");
//            ref.child("providers").child(UsersUtils.getInstance().getCurrentUserUid()).setValue(provider);
            try {
                ProviderDataRepository.getInstance().createNewProviderInApp(id, provider);
            }catch (Exception e){
                Toast.makeText(getBaseContext(), "Sing Up Didn't Complete duo to " + e, Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getBaseContext(), "Sing Up Successfully\nRedirecting Please wait...", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void backToProfile() {
        Log.i(TAG_PROVIDER_SINGUP, "backToProfile() ");
        if (!isFormClear()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("If you Quit now you data wont be saved")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private boolean checkIfInputValid() {
        Log.i("Checking Valid psua", "clicked on providerButton");
        Log.i("asd", "company " + checkIfEmpty(providerCompanyName));
        Log.i("asd", "profes " + checkIfEmpty(providerProfession));
        Log.i("asd", "address " + checkIfEmpty(providerAddress));
        Log.i("asd", "phone " + checkIfEmpty(providerPhoneNumber));

        return checkIfEmpty(providerCompanyName) && checkIfEmpty(providerProfession) && checkIfEmpty(providerPhoneNumber) && !isPhoneValid(providerPhoneNumber.getText().toString()) && checkIfEmpty(providerAddress);
    }

    private void displayErrorToUserIfThereIsOne() {
        if (checkIfEmpty(providerCompanyName))
            providerCompanyName.setError("You must fill Provider Name");
        if (checkIfEmpty(providerProfession))
            providerProfession.setError("You must fill Profession for your business");
        if (checkIfEmpty(providerAddress))
            providerAddress.setError("You must add Your Business Address");
        if (checkIfEmpty(providerPhoneNumber))
            providerPhoneNumber.setError("You must fill Phone Number");
        else if (isPhoneValid(providerPhoneNumber.getText().toString()) && providerPhoneNumber.getText().toString().length() != 10)
            providerPhoneNumber.setError("Phone Number is Incorrect");
    }


    private boolean isPhoneValid(String phoneNumber) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber) || Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private boolean isFormClear() {
        return checkIfEmpty(providerCompanyName) && checkIfEmpty(providerProfession) && checkIfEmpty(providerPhoneNumber) && checkIfEmpty(providerAddress);
    }


    public EditText getProviderCompanyName() {
        return providerCompanyName;
    }

    public void setProviderCompanyName(EditText providerCompanyName) {
        this.providerCompanyName = providerCompanyName;
    }

    public EditText getProviderProfession() {
        return providerProfession;
    }

    public void setProviderProfession(EditText providerProfession) {
        this.providerProfession = providerProfession;
    }

    public EditText getProviderPhoneNumber() {
        return providerPhoneNumber;
    }

    public void setProviderPhoneNumber(EditText providerPhoneNumber) {
        this.providerPhoneNumber = providerPhoneNumber;
    }

    public EditText getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(EditText providerAddress) {
        this.providerAddress = providerAddress;
    }
}

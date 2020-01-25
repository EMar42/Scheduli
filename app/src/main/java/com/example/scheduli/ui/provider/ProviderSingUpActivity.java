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

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.utils.UsersUtils;

public class ProviderSingUpActivity extends BaseMenuActivity {

    private static final String TAG_PROVIDER_SINGUP = "Provider Sing-Up";
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;



    private EditText providerCompanyName, providerProfession, providerPhoneNumber, providerEmailAddress;
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
        providerEmailAddress = findViewById(R.id.ed_provider_email);
        providerProfilePicture = findViewById(R.id.im_provider_pic);
        singUpProviderButton = findViewById(R.id.btn_sign_up_create_provider);
        backToProfileButton = findViewById(R.id.btn_return_profile_screen);
        Log.i(TAG_PROVIDER_SINGUP, "finished initView() ");
    }

    private void singUpNewProvider() {
        Log.i(TAG_PROVIDER_SINGUP, "singUpNewProvider()");
        displayErrorToUserIfThereIsOne();

        if (checkIfInputValid()) {
            final String companyName = providerCompanyName.getText().toString();
            final String profession = providerProfession.getText().toString();
            final String phone = providerPhoneNumber.getText().toString();
            final String email = providerEmailAddress.getText().toString();

            //TODO sing up to server

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
        return checkIfEmpty(providerCompanyName) && checkIfEmpty(providerProfession) && checkIfEmpty(providerPhoneNumber) && !isPhoneValid(providerPhoneNumber.getText().toString()) && checkIfEmpty(providerEmailAddress) && !isEmailValid(providerEmailAddress.getText().toString());
    }

    private void displayErrorToUserIfThereIsOne() {
        if (checkIfEmpty(providerCompanyName))
            providerCompanyName.setError("You must fill Provider Name");
        if (checkIfEmpty(providerProfession))
            providerProfession.setError("You must fill Profession for your business");
        if (checkIfEmpty(providerEmailAddress))
            providerEmailAddress.setError("You must add Email Address");
        else if (!isEmailValid(providerEmailAddress.getText().toString()))
            providerEmailAddress.setError("Your Email Address Is Incorrect form");
        if (checkIfEmpty(providerPhoneNumber))
            providerPhoneNumber.setError("You must fill Phone Number");
        else if (isPhoneValid(providerPhoneNumber.getText().toString()) && providerPhoneNumber.getText().toString().length() != 10)
            providerPhoneNumber.setError("Phone Number is Incorrect");
    }


    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneValid(String phoneNumber) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber) || Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private boolean isFormClear() {
        return checkIfEmpty(providerCompanyName) && checkIfEmpty(providerProfession) && checkIfEmpty(providerPhoneNumber) && checkIfEmpty(providerEmailAddress);
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

    public EditText getProviderEmailAddress() {
        return providerEmailAddress;
    }

    public void setProviderEmailAddress(EditText providerEmailAddress) {
        this.providerEmailAddress = providerEmailAddress;
    }
}

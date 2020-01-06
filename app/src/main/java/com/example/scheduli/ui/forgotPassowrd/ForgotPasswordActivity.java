package com.example.scheduli.ui.forgotPassowrd;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;
import com.example.scheduli.utils.UsersUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private UsersUtils usersUtils;
    private Button sendPassword, cancelRecovery;
    private EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usersUtils = UsersUtils.getInstance();
        initView();

        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPasswordToEmail();
            }
        });

        cancelRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void recoverPasswordToEmail() {
        //TODO implement
        //TODO Add notification on success of password sending
    }

    private void initView() {
        sendPassword = findViewById(R.id.btn_forgot_pass_send);
        cancelRecovery = findViewById(R.id.btn_forgot_pass_cancel);
        userEmail = findViewById(R.id.et_forgot_pass_email);
    }


    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
}

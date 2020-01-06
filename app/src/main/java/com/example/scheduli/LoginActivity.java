package com.example.scheduli;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.Utils.UsersUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button createAccount, loginToApp, forgotPassword;
    private UsersUtils usersUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usersUtils = UsersUtils.getInstance();
        initView();


    }

    private void initView() {
        userEmail = findViewById(R.id.et_login_email);
        userPassword = findViewById(R.id.et_login_password);
        createAccount = findViewById(R.id.btn_login_signup);
        loginToApp = findViewById(R.id.btn_login_to_account);
        forgotPassword = findViewById(R.id.btn_login_forgot_password);
    }


}

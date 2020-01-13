package com.example.scheduli.ui.login;

import android.content.Intent;
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
import com.example.scheduli.data.UserDataRepository;
import com.example.scheduli.ui.forgotPassowrd.ForgotPasswordActivity;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.ui.signup.SignupActivity;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_TAG = "Login Activity";

    private EditText userEmail, userPassword;
    private Button createAccount, loginToApp, forgotPassword;
    private UsersUtils usersUtils;
    private FirebaseAuth.AuthStateListener userLoginStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usersUtils = UsersUtils.getInstance();

        checkLoginStatus();

        initView();


        //add login button action
        loginToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserToApp();
            }
        });

        //add forgotPassword button navigation
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //add sign-up button action
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        //setup login for user handler after sign-up.
        userLoginStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = UsersUtils.getInstance().getCurrentUser();
                if (user != null) {
                    Log.i(LOGIN_TAG, "Got user form sign up activity logging in");
                    loginToMainActivity(user);
                }
            }
        };
    }

    private void loginToMainActivity(FirebaseUser user) {
        String uid = user.getUid();
        //TODO add sound on login
        //TODO implemennt intent change once user logged in to the app from sign-up.
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("LOGGED_IN_USER_UID", uid);
        UserDataRepository.getInstance().keepInSync(uid);
        startActivity(intent);
        finish();
    }

    private void checkLoginStatus() {
        FirebaseUser user = UsersUtils.getInstance().getCurrentUser();
        if (user != null) {
            Log.i(LOGIN_TAG, "Entering app user already logged in");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("LOGGED_IN_USER_UID", user.getUid() + " " + user.getDisplayName());
            loginToMainActivity(user);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        userEmail = findViewById(R.id.et_login_email);
        userPassword = findViewById(R.id.et_login_password);
        createAccount = findViewById(R.id.btn_login_signup);
        loginToApp = findViewById(R.id.btn_login_to_account);
        forgotPassword = findViewById(R.id.btn_login_forgot_password);
    }

    private void loginUserToApp() {
        if (checkIfEmpty(userEmail))
            userEmail.setError("Email cannot be empty");
        else if (!isEmailValid(userEmail.getText().toString()))
            userEmail.setError("Email is not in the correct format");

        if (checkIfEmpty(userPassword))
            userPassword.setError("Password cannot be empty");

        if (!checkIfEmpty(userEmail) && !checkIfEmpty(userPassword) && isEmailValid(userEmail.getText().toString())) {

            usersUtils.getFireBaseAuth().signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.e(LOGIN_TAG, "Failed to login with email " + userEmail.getText().toString());
                        Toast.makeText(LoginActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(LOGIN_TAG, "Succesful login to the application using " + userEmail.getText().toString());
                        FirebaseUser user = usersUtils.getFireBaseAuth().getCurrentUser();
                        loginToMainActivity(user);
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
}

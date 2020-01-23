package com.example.scheduli.ui.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.BuildConfig;
import com.example.scheduli.R;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.forgotPassowrd.ForgotPasswordActivity;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.ui.signup.SignupActivity;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_TAG = "Login Activity";

    private EditText userEmail, userPassword;
    private Button createAccount, loginToApp, forgotPassword;
    private UsersUtils usersUtils;
    private FirebaseAuth.AuthStateListener userLoginStateListener;
    private Snackbar snackbar;

    //FOR TESTING PERFORMANCE
    private void enableStrictMode() {
        if (BuildConfig.DEBUG) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableStrictMode();

        setContentView(R.layout.activity_login);
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

        checkLoginStatus();
    }

    protected void loginToMainActivity(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //if bugs happen on login clear this
        startActivity(intent);
        finish();
    }

    private void checkLoginStatus() {
        this.loginToApp.setEnabled(false);
        this.forgotPassword.setEnabled(false);
        this.createAccount.setEnabled(false);
        snackbar = Snackbar.make(findViewById(R.id.login_constraint_layout), getText(R.string.attempting_to_login).toString(), BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
        new LoginTask().execute();
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

            UsersUtils.getInstance().getFireBaseAuth().signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.e(LOGIN_TAG, "Failed to login with email " + userEmail.getText().toString());
                        MediaPlayer player = MediaPlayer.create(LoginActivity.this, R.raw.computer_error);
                        player.start();
                        loginToApp.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake));
                        Toast.makeText(LoginActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(LOGIN_TAG, "Succesful login to the application using " + userEmail.getText().toString());
                        new LoginTask().execute();
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

    class LoginTask extends AsyncTask<Void, Void, FirebaseUser> {
        private ProgressBar progressBar;


        @Override
        protected void onPreExecute() {
            progressBar = findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected FirebaseUser doInBackground(Void... voids) {
            Log.i(LOGIN_TAG, "Checking logged user in a task");

            FirebaseUser user = UsersUtils.getInstance().getCurrentUser();
            if (user != null) {
                UserDataRepository.getInstance().keepInSync(user.getUid());
                return user;
            }

            return null;
        }

        @Override
        protected void onPostExecute(FirebaseUser user) {
            if (user != null) {
                loginToMainActivity(user);
            }

            //restore button functionality
            LoginActivity.this.createAccount.setEnabled(true);
            LoginActivity.this.loginToApp.setEnabled(true);
            LoginActivity.this.forgotPassword.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            snackbar.dismiss();
        }
    }
}

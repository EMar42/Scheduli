package com.example.scheduli.ui.signup;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;

public class SignupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
}

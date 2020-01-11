package com.example.scheduli.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (findViewById(R.id.settings_fragment_layout) != null) {
            if (savedInstanceState != null)
                return;

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_fragment_layout, new SettingsFragment())
                    .commit();
        }
    }


}

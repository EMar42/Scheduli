package com.example.scheduli;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.login.LoginActivity;
import com.example.scheduli.ui.settings.SettingsActivity;
import com.example.scheduli.utils.UsersUtils;

public class BaseMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_settings:
                startAppSettings();
                return true;
            case R.id.menu_action_logout:
                logoutFromApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutFromApp() {
        UsersUtils.getInstance().logout();
        Intent returnToLoginIntent = new Intent(this, LoginActivity.class);
        returnToLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        UserDataRepository.getInstance().clearEventsOfAppointments();
        startActivity(returnToLoginIntent);
    }

    private void startAppSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}

package com.example.scheduli.ui.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.ui.SearchProvider.SearchProviderActivity;
import com.example.scheduli.ui.profile.ProfileFragment;
import com.example.scheduli.ui.viewAppointments.AppointmentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends BaseMenuActivity {

    BottomNavigationView bottomNavigationView;
    private Toolbar mainToolbar;
    private FloatingActionButton searchProviderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_appointments:
                        openFragment(AppointmentFragment.newInstance());
                        return true;
                    case R.id.navigation_profile:
                        openFragment(ProfileFragment.newInstance());
                        return true;
                    default:
                        return false;
                }
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(AppointmentFragment.newInstance());

        searchProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivity();
            }
        });
    }

    private void initView() {
        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        searchProviderButton = findViewById(R.id.floating_search_provider);
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void searchActivity() {
        Intent intent = new Intent(this, SearchProviderActivity.class);
        startActivity(intent);
    }
}


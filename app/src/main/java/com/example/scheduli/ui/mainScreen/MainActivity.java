package com.example.scheduli.ui.mainScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.SearchProvider.SearchProviderActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends BaseMenuActivity {

    BottomNavigationView bottomNavigationView;
    private Toolbar mainToolbar;
    private FloatingActionButton searchProviderButton;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        searchProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivity();
            }
        });

        //TODO Check performance impact
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(getString(R.string.allowAppointmentLimitingKey), false)) {
            String limitValue = preferences.getString(getString(R.string.appointmentsCountKey), "100");

            UserDataRepository.getInstance().setLimitAmount(Integer.parseInt(limitValue));
        }

    }

    private void initView() {
        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        searchProviderButton = findViewById(R.id.floating_search_provider);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_appointments)
                .build();
        navController = Navigation.findNavController(this, R.id.main_fragment_container);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    public void searchActivity() {
        Intent intent = new Intent(this, SearchProviderActivity.class);
        startActivity(intent);
    }

}


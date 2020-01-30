package com.example.scheduli.ui.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.Sessions;
import com.example.scheduli.data.WorkDay;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.SearchProvider.SearchProviderActivity;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        /***** IN CASE OF DATA LOST (providers DB)              *****/
        /***** inserting full provider snippet                  *****/
        /***** under the current user connected (current uid):  *****/
        /***** use this func:                                   *****/
//        insertNewProvider(); //contain full provider snippet





        initView();
        searchProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivity();
            }
        });

    }

    private void insertNewProvider() {
        //create new provider :
        Map<String, WorkDay> workingDays = new HashMap<String, WorkDay>() {{ // work
        put("3", new WorkDay(1546329600000l,1546329600000l));
        put("4", new WorkDay(1546329600000l,1546329600000l));
        }};

        Sessions s1 = new Sessions( 1581728400000l, 1546344000000l , true);
        s1.setUserUid("fakeID1");
        Sessions s2 = new Sessions( 1582268400000l,1546344000000l ,true );
        s2.setUserUid("fakeID2");
        Sessions s3 = new Sessions(1581775200000l,1546344000000l ,true );
        s3.setUserUid("fakeID3");
        Sessions s4 = new Sessions( 1582279200000l,1546344000000l,false );
        s4.setUserUid("fakeID4");
        final ArrayList<Sessions> sessions1 = new ArrayList<>();
        sessions1.add(s1);
        sessions1.add(s2);
        sessions1.add(s3);
        sessions1.add(s4);

        Sessions c1 = new Sessions( 1580716800000l, 1546344000000l , true);
        c1.setUserUid("fakeID1");
        Sessions c2 = new Sessions( 1580724000000l,1546344000000l ,true );
        c2.setUserUid("fakeID2");
        Sessions c3 = new Sessions(1580803200000l,1546344000000l ,true );
        c3.setUserUid("fakeID3");
        Sessions c4 = new Sessions( 1580778000000l,1546344000000l,true );
        c4.setUserUid("fakeID4");

        final ArrayList<Sessions> sessions2 = new ArrayList<>();
        sessions2.add(c1);
        sessions2.add(c2);
        sessions2.add(c3);
        sessions2.add(c4);

        Map<String, ArrayList<Sessions>> dailySessions = new HashMap<String , ArrayList<Sessions>>(){{
            put("21-02-2020", sessions1);
            put("22-02-2020", sessions2);
        }}; // key is a date (day/month/year).

        Service service1 = new Service("Service1" ,45f,20, workingDays, dailySessions);
        Service service2 = new Service("Service2" ,40f,40, workingDays, dailySessions);

        ArrayList<Service> services = new ArrayList<>();
        services.add(service1);
        services.add(service2);

        Provider provider = new Provider("https://firebasestorage.googleapis.com/v0/b/scheduli-b1643.appspot.com/o/providerImages%2Fflooop.png?alt=media&token=7696e79e-b50e-4476-bdcb-b76008d60614", "Pitsutsiya","Manches", "1478529634","123 fake street, Ashkelon",services);

        ProviderDataRepository.getInstance().createNewProviderInApp(UsersUtils.getInstance().getCurrentUserUid(), provider);
    }


    private void initView() {
        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);

        searchProviderButton = findViewById(R.id.floating_search_provider);
        navController = Navigation.findNavController(this, R.id.main_fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    public void searchActivity() {
        Intent intent = new Intent(this, SearchProviderActivity.class);
        startActivity(intent);
    }


}


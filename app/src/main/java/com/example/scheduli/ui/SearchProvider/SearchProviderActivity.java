package com.example.scheduli.ui.SearchProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.ProvidersAdapter;
import com.example.scheduli.data.joined.JoinedProvider;
import com.example.scheduli.ui.BookingAppointment.BookingAppointmentActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchProviderActivity extends BaseMenuActivity implements ProvidersAdapter.OnProviderListener {

    private static final String TAG_SEARCH_ACT = "SearchProviderActivity";

    private BottomNavigationView bottomNavigationView;

    private Toolbar mainToolbar;
    private AppBarConfiguration appBarConfiguration;
    private EditText searchField;
    private ImageButton searchBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayout;
    private List<Provider> providersList;
    private ProvidersAdapter adapter;
    private DatabaseReference ref;

    private JoinedProvider joinedProvider = new JoinedProvider();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initView();
        init();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchField.getText().toString();
                if (!TextUtils.isEmpty(search)) {
                    firebaseProviderSearchByCompany(search);

                    if (providersList.size() < 1) {
                        firebaseProviderSearchByProfession(search);
                    }
                } else {
                    getAllProviders();
                    Toast.makeText(SearchProviderActivity.this, "Empty search...", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void initView() {
        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_appointments)
                .build();
    }

    private void init() {
        searchField = (EditText) findViewById(R.id.editText_search_field);
        searchBtn = (ImageButton) findViewById(R.id.search_provider_button);
        recyclerView = (RecyclerView) findViewById(R.id.search_results_list);

        providersList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance().getReference().child("providers");

        getAllProviders();
        mLayout = new LinearLayoutManager(this);
        adapter = new ProvidersAdapter(providersList, this);

        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(adapter);
    }

    private void firebaseProviderSearchByProfession(String profession) {

        Query query = ref.orderByChild("profession").equalTo(profession);
        query.addListenerForSingleValueEvent(eventListener);

    }


    private void firebaseProviderSearchByCompany(String company) {

        Query query = ref.orderByChild("companyName").equalTo(company);

        query.addListenerForSingleValueEvent(eventListener);
    }

    //select * from providers
    private void getAllProviders() {
        ref.addListenerForSingleValueEvent(eventListener);

    }


    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            providersList.clear();

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Provider provider = snapshot.getValue(Provider.class);
                    if(provider.getServices()!= null) {
                        if(provider.getServices().size()>0) {
                            providersList.add(provider);
                            Log.d(TAG_SEARCH_ACT, "ADDED: " + provider.getCompanyName());
                        }
                    }
                }

                if (!providersList.isEmpty()) {
                    recyclerView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(SearchProviderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onProviderClick(int position) {

        Log.d(TAG_SEARCH_ACT, "onProviderClick: clicked");

        final Provider provider = providersList.get(position);

        joinedProvider = new JoinedProvider(provider);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("providers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                    //The key is by company name.
                    if (provider.getCompanyName().contains(item_snapshot.child("companyName").getValue().toString())) {
                        Log.d(TAG_SEARCH_ACT, "User Choose: " + item_snapshot.toString());

                        joinedProvider.setPid(item_snapshot.getKey());

                        if (joinedProvider.getPid() != null && joinedProvider.getCompanyName() != null) {
                            Intent intent = new Intent(SearchProviderActivity.this, BookingAppointmentActivity.class);
                            intent.putExtra("companyName", provider.getCompanyName());
                            intent.putExtra("pid", joinedProvider.getPid());
                            intent.putExtra("provider", provider);
                            joinedProvider.setPid(null);
                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select provider", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG_SEARCH_ACT, "onProviderClick: onCancelled called ");
            }
        });
    }
}
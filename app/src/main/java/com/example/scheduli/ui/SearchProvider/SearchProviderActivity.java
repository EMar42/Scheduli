package com.example.scheduli.ui.SearchProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.ProvidersAdapter;
import com.example.scheduli.data.joined.JoinedProvider;
import com.example.scheduli.ui.BookingAppointment.BookingAppointmentActivity;
import com.example.scheduli.utils.UsersUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchProviderActivity extends BaseMenuActivity implements ProvidersAdapter.OnProviderListener{

    BottomNavigationView bottomNavigationView;
    private Toolbar mainToolbar;
    private FloatingActionButton searchProviderButton;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private static final String TAG_SEARCH_ACT = "SearchProviderActivity";
    private EditText searchField;
    private ImageButton searchBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayout;
    private List<Provider> providersList;
    ProvidersAdapter adapter;
    DatabaseReference ref;
    private static String PID = null;

   private JoinedProvider joinedProvider = new JoinedProvider();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        providersList = new ArrayList<>();

        initView();
        init();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchField.getText().toString();
                if(!TextUtils.isEmpty(search)) {
//                    ref = FirebaseDatabase.getInstance().getReference().child("providers");
                    firebaseProviderSearchByCompany(search);

                    if (providersList.size() < 1) {
//                        ref = FirebaseDatabase.getInstance().getReference().child("providers");

                        firebaseProviderSearchByProfession(search);
                    }
                }
                else {
                    getAllProviders();
                    Toast.makeText(SearchProviderActivity.this, "Empty search...", Toast.LENGTH_SHORT).show();

                }
            }
        });
//        getAllProviders();

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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance().getReference().child("providers");

        mLayout = new LinearLayoutManager(this);
        getAllProviders();
        adapter = new ProvidersAdapter(providersList, this);

        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(adapter);
    }

    private void firebaseProviderSearchByProfession(String profession) {

        Query query = ref.orderByChild("profession").equalTo(profession);
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    private void firebaseProviderSearchByCompany(String company){

        Query query = ref.orderByChild("companyName").equalTo(company);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    //select * from providers
    private void getAllProviders() {
        ref.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            providersList.clear();

            if(dataSnapshot.exists()){

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Provider provider = snapshot.getValue(Provider.class);
                    providersList.add(provider);

                }

                if(!providersList.isEmpty()) {
//                    adapter = new ProvidersAdapter(providersList);
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

                        if (joinedProvider.getPid()!=null && joinedProvider.getCompanyName()!= null) {
                            Intent intent = new Intent(SearchProviderActivity.this, BookingAppointmentActivity.class);
                            //TODO: refatoring data transaction
                            intent.putExtra("companyName", provider.getCompanyName());
                            intent.putExtra("pid", joinedProvider.getPid());
                            intent.putExtra("provider", provider);
                            System.out.println("Got provider list of dailySessions: " + provider.getServices().get(1).getDailySessions()); // TEST
                            joinedProvider.setPid(null);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "null pid", Toast.LENGTH_LONG).show();
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
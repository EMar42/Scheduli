package com.example.scheduli.ui.SearchProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.ProvidersAdapter;
import com.example.scheduli.ui.BookingAppointment.BookingAppointmentActivity;
import com.example.scheduli.utils.UsersUtils;
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

public class SearchProviderActivity extends AppCompatActivity implements ProvidersAdapter.OnProviderListener{

    private static final String TAG_SEARCH_ACT = "SearchProviderActivity";
    private EditText searchField;
    private ImageButton searchBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayout;
    private List<Provider> providersList;
    UsersUtils usersUtils;
    ProvidersAdapter adapter;
     DatabaseReference ref;
     private static String PID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        providersList = new ArrayList<>();

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
        getAllProviders();

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
//                    Provider provider = new Provider();
//                    String id = UsersUtils.getInstance().getCurrentUserUid();
//                    provider.setCompanyName(snapshot.child(id).getValue(Provider.class).getCompanyName());

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


    /*
    TODO: complete dynamic search
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        Menu item = (Menu) menu.findItem(R.id.action_search);
//
//        SearchView searchView = (SearchView) item;
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
    /*
    *
    *
    */

    @Override
    public void onProviderClick(int position) {

        Log.d(TAG_SEARCH_ACT, "onProviderClick: clicked");

        final Provider provider = providersList.get(position);
        final String[] uid = new String[1];

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("providers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {

                    if(provider.getCompanyName().contains(item_snapshot.child("companyName").getValue().toString())) {

                    Log.d( TAG_SEARCH_ACT, "User Choose: " + item_snapshot.toString());
                        uid[0] = item_snapshot.getKey();
                        System.out.println("ID: " + uid[0]); //TEST

                        Intent intent = new Intent(SearchProviderActivity.this, BookingAppointmentActivity.class);
                        intent.putExtra("companyName", provider.getCompanyName());
                        intent.putExtra("pid", uid[0]);
                        startActivity(intent);
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


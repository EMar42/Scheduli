package com.example.scheduli.ui.SearchProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.ProvidersAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchProviderActivity extends AppCompatActivity {

    private EditText searchField;
    private ImageView searchBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayout;




    private List<Provider> providersList;

    ProvidersAdapter adapter;

     DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);



        searchField = (EditText)findViewById(R.id.editText_search_field);
        searchBtn = (ImageButton)findViewById(R.id.search_provider_button);


        recyclerView = (RecyclerView) findViewById(R.id.search_results_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        providersList = new ArrayList<>();
        adapter = new ProvidersAdapter(this, providersList);
        recyclerView.setAdapter(adapter);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll();

            }
        });
        providersList.add(new Provider("111","sdf"));
        providersList.add(new Provider("222","sdf"));
        providersList.add(new Provider("333","sdf"));

        showAll();


    }



    //select * from providers
    private void showAll() {
        ref = FirebaseDatabase.getInstance().getReference("providers");
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

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void addProvider(){

        Provider provider = new Provider("company","pro");

        ref.child("providers").setValue(provider);


    }
}
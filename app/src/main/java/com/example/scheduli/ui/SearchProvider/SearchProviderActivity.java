package com.example.scheduli.ui.SearchProvider;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchProviderActivity extends AppCompatActivity {

    private EditText searchField;
    private ImageButton searchBtn;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        providersList = new ArrayList<>();
//        providersList.add(new Provider(R.drawable.ic_person, "Bar", "sdf"));
//        providersList.add(new Provider(R.drawable.ic_person, "Shuster", "sdf"));
//        providersList.add(new Provider(R.drawable.ic_person, "Evgeny", "sdf"));

        searchField = (EditText) findViewById(R.id.editText_search_field);
        searchBtn = (ImageButton) findViewById(R.id.search_provider_button);
        recyclerView = (RecyclerView) findViewById(R.id.search_results_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mLayout = new LinearLayoutManager(this);
        mAdapter = new ProvidersAdapter(providersList);


        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);

        ref = FirebaseDatabase.getInstance().getReference().child("providers");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchField.getText().toString();
                if(!TextUtils.isEmpty(search)) {
                    ref = FirebaseDatabase.getInstance().getReference().child("providers");
                    firebaseProviderSearchByCompany(search);

                    if (providersList.size() < 1) {
                        ref = FirebaseDatabase.getInstance().getReference().child("providers");

                        firebaseProviderSearchByProfession(search);
                    }
                }
                else {
              Toast.makeText(SearchProviderActivity.this, "Empty search...", Toast.LENGTH_SHORT).show();

                }
            }
        });
        showAll();

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
    private void showAll() {
        ref.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            providersList.clear();
//            Toast.makeText(SearchProviderActivity.this, "change called", Toast.LENGTH_SHORT).show();

            if(dataSnapshot.exists()){

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                Toast.makeText(SearchProviderActivity.this, "enter for", Toast.LENGTH_SHORT).show();
                    Provider provider = snapshot.getValue(Provider.class);
                    providersList.add(provider);
                }
                if(!providersList.isEmpty()) {
                    adapter = new ProvidersAdapter(providersList);
                    recyclerView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(SearchProviderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };


}

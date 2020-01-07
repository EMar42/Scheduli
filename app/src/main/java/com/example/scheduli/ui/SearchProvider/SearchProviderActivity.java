package com.example.scheduli.ui.SearchProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.User;
import com.example.scheduli.utils.UsersUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchProviderActivity extends AppCompatActivity {

    private EditText searchField;
    private ImageView searchBtn;
    private RecyclerView resultsList;

    private List<Provider> providersList;

     DatabaseReference ref;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);


        searchField = (EditText)findViewById(R.id.editText_search_field);
        searchBtn = (ImageButton)findViewById(R.id.search_provider_button);

        resultsList = (RecyclerView) findViewById(R.id.search_results_list);

        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        providersList = new ArrayList<>();

        //select * from users
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


}

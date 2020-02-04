package com.example.scheduli.ui.provider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.ServiceAdapter;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.ui.BookingAppointment.BookingAppointmentActivity;
import com.example.scheduli.ui.service.AddServiceActivity;
import com.example.scheduli.utils.UsersUtils;

import java.util.ArrayList;

public class ProviderActivity extends BaseMenuActivity {


    private RecyclerView.LayoutManager mLayout;
    private ServiceAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Service> services;
    private Provider provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        initView();

        mAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (position == services.size()-1){
                    Intent intent = new Intent(getBaseContext(), AddServiceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void initView() {
        recyclerView = findViewById(R.id.rv_provider_services);
        Intent intent = getIntent();
        provider = intent.getParcelableExtra("provider");
        services = new ArrayList<>();
        services = provider.getServices();
        services.add(new Service());
        mLayout = new GridLayoutManager(this,2);
        mAdapter = new ServiceAdapter(this, services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);



    }

}

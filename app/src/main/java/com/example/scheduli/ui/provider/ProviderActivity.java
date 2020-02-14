package com.example.scheduli.ui.provider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.ServiceAdapter;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.ui.BookingAppointment.BookingAppointmentActivity;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.ui.service.AddServiceActivity;
import com.example.scheduli.utils.UsersUtils;

import java.util.ArrayList;

public class ProviderActivity extends BaseMenuActivity {

    private static final String TAG_PROVIDER_ACTIVITY = "Provider Activity";

    private RecyclerView.LayoutManager mLayout;
    private ServiceAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Service> services;
    private Button backButton;
    private Provider provider;
    private Service service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        initView();

        mAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == services.size() - 1) {
                    Intent intent = new Intent(getBaseContext(), AddServiceActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), AddServiceActivity.class);
                    intent.putExtra("service", services.get(position));
                    startActivity(intent);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        recyclerView = findViewById(R.id.rv_provider_services);
        backButton = findViewById(R.id.btn_back_addservice);
        Intent intent = getIntent();
        provider = intent.getParcelableExtra("provider");
        services = new ArrayList<>();
        //services = intent.getParcelableExtra("service");

        try {
            services = provider.getServices();
            if (services == null) {
                services = new ArrayList<>();
                services.add(new Service());
            } else
                services.add(new Service());
        } catch (Exception e) {
            Log.i(TAG_PROVIDER_ACTIVITY, "no service currently");
        }
//
//        if (services == null || services.size() == 0) {
//            services = new ArrayList<>();
//            services.add(new Service());
//        }


        mLayout = new GridLayoutManager(this, 2);
        mAdapter = new ServiceAdapter(this, services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
    }

}

package com.example.scheduli.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.scheduli.R;
import com.example.scheduli.data.Service;

public class SetServiceScheduleActivity extends AppCompatActivity {

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_service_schedule);

        initView();

    }









    private void initView(){

        Intent intent = getIntent();
        service = intent.getParcelableExtra("service");

    }
}

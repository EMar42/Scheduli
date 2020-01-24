package com.example.scheduli.ui.provider;

import android.os.Bundle;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;

public class ProviderActivity extends BaseMenuActivity {

    public static ProviderActivity newInstance() {
        return new ProviderActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_singup);


    }
}

package com.example.scheduli.ui.mainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.scheduli.R;
import com.example.scheduli.ui.SearchProvider.SearchProviderActivity;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button searchProviderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchProviderButton = (Button) findViewById(R.id.searchProviderButton);


        searchProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = searchProviderButton.getSolidColor();

                searchActivity();
            }
        });


    }


    public void searchActivity() {
        Intent intent = new Intent(this, SearchProviderActivity.class);
        startActivity(intent);
    }
}


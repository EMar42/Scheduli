package com.example.scheduli.ui.SearchProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.scheduli.R;

public class SearchProviderActivity extends AppCompatActivity {

    private EditText searchField;
    private ImageView searchBtn;

    private RecyclerView resultsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);

        searchField = (EditText)findViewById(R.id.editText_search_field);
        searchBtn = (ImageButton)findViewById(R.id.search_provider_button);

        resultsList = (RecyclerView) findViewById(R.id.search_results_list);

    }
}

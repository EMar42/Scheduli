package com.example.scheduli.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.ProviderViewHolder> {


    private Context mCtx;
    private List<Provider> providerList;

    public ProvidersAdapter(Context mCtx, List<Provider> providerList) {
        this.mCtx = mCtx;
        this.providerList = providerList;
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.activity_search_provider, parent, false);
        return new ProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        Provider provider = providerList.get(position);
        holder.textViewName.setText(provider.companyName);
        holder.textViewGenre.setText("profession: " + provider.profession);
        holder.textViewAge.setText("id: " + provider.uid);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    class ProviderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewGenre, textViewAge, textViewCountry;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);

//            textViewName = itemView.findViewById(R.id.text_view_name);
//            textViewGenre = itemView.findViewById(R.id.text_view_genre);
//            textViewAge = itemView.findViewById(R.id.text_view_age);
//            textViewCountry = itemView.findViewById(R.id.text_view_country);
        }
    }
}
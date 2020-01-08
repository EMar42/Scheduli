package com.example.scheduli.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;

import java.util.List;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.ProviderViewHolder> {


    public ImageView imageView;
    private Context mCtx;
    private List<Provider> providerList;
    public TextView companyName;
    public TextView profession;

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
        holder.textViewProfession.setText("profession: " + provider.profession);
        holder.textViewId.setText("id: " + provider.uid);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }




    class ProviderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewProfession, textViewId;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewProfession = itemView.findViewById(R.id.text_view_profession);
            textViewId = itemView.findViewById(R.id.text_view_id);
        }
    }
}
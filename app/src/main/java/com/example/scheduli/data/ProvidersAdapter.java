package com.example.scheduli.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    private List<Provider> providerList;


//    public ImageView imageView;
//    private Context mCtx;
//    public TextView companyName;
//    public TextView profession;

    public ProvidersAdapter( List<Provider> providerList) {
        this.providerList = providerList;
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        Provider provider = providerList.get(position);

        holder.profileImg.setImageResource(provider.getImageResource());
        holder.textViewName.setText(provider.getCompanyName());
        holder.textViewProfession.setText("profession: " + provider.getProfession());
        holder.textViewId.setText("id: " + provider.uid);
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }




    public static class ProviderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewProfession, textViewId;
        ImageView profileImg;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.provider_profile_img);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewProfession = itemView.findViewById(R.id.text_view_profession);
            textViewId = itemView.findViewById(R.id.text_view_id);
        }
    }


}
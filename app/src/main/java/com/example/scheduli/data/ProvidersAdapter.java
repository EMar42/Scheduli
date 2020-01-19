package com.example.scheduli.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;

import java.util.List;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.ProviderViewHolder>{// implements Filterable {

    private  List<Provider> providerList;
    private  List<Provider> providerListAll; // for dynamic search
    private OnProviderListener listener;


    public ProvidersAdapter(List<Provider> providerList, OnProviderListener onProviderListener) {
        this.providerList = providerList;
        this.providerListAll = providerList;
        this.listener = onProviderListener;
    }


    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ProviderViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        Provider provider = providerList.get(position);

        holder.profileImg.setImageResource(R.drawable.ic_person);
        holder.textViewCompanyName.setText(provider.getCompanyName());
        holder.textViewProfession.setText("profession: " + provider.getProfession());
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    // for dynamic search
//    @Override
//    public Filter getFilter() {
//        return filter;
//    }


    public static class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewCompanyName, textViewProfession;
        ImageView profileImg;
        View view;

        OnProviderListener onProviderListener;//

//..
        public ProviderViewHolder(@NonNull View itemView, OnProviderListener onProviderListener) {
            super(itemView);

            view = itemView;
            profileImg = itemView.findViewById(R.id.provider_profile_img);
            textViewCompanyName = itemView.findViewById(R.id.text_view_companyName);
            textViewProfession = itemView.findViewById(R.id.text_view_profession);
            this.onProviderListener = onProviderListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProviderListener.onProviderClick(getAdapterPosition());
        }
        //..
    }

    /**
     * on click recycle view listener
     */
    public interface OnProviderListener{
        void onProviderClick(int position);
    }

    //TODO: immplement dynamic search (OPT)
    // *********************** Dynamic search *************************

//    Filter filter = new Filter() {
//
//        // Run on background thread
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            List<Provider> filteredList = new ArrayList<>();
//
//            if(charSequence.toString().isEmpty()){
//                filteredList.addAll(providerListAll);
//            }else {
//                for(Provider provider : providerList){
//                    if(provider.companyName.contains(charSequence.toString())){
//                        filteredList.add(provider);
//                    }
//                }
//            }
//
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = filteredList;
//
//
//            return filterResults;
//        }
//
//        //Run on ui thread
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            providerList.clear();
//            providerList.addAll((Collection<? extends Provider>) filterResults);
//            notifyDataSetChanged();
//
//        }
//    };
    // *********************** Dynamic search *************************


}
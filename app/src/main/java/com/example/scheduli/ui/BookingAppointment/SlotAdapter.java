package com.example.scheduli.ui.BookingAppointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.scheduli.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {


    private List<Date> slotsList;
    private int selectedItem = -1;
    private Context context;
    private OnItemClickListener mListener;

    public SlotAdapter(Context context, List<Date> slotsList) {

        this.context = context;
        this.slotsList = slotsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Date currentItem = slotsList.get(position);
        long hour = (currentItem.getTime()  / (1000 * 60 * 60)) % 24;
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        holder.slotTime.setText(dateFormat.format(currentItem));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final int previousItem = selectedItem;
                selectedItem = position;
                mListener.onItemClick(position);
                notifyItemChanged(previousItem);
                notifyItemChanged(position);

            }
        });
        if (selectedItem == position ) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryServiceLight));
            holder.slotTime.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return slotsList.size();
    }

    public interface OnItemClickListener{
        void  onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView slotTime;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slotTime = (TextView) itemView.findViewById(R.id.slot_time);
            cardView = itemView.findViewById(R.id.slot_card);
        }
    }
}

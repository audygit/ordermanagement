package com.android.ordermanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;


public class TransportListAdapter extends RecyclerView.Adapter<TransportListAdapter.MyViewHolder>{

    private List<String> estimates;
    private Context mContext;
    private ViewClickListener listener;

    public TransportListAdapter(List<String> estimates, Context mContext, ViewClickListener viewClickListener) {
        this.estimates = estimates;
        this.mContext = mContext;
        listener = viewClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_list_item, parent, false);
        return new MyViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String currentEstimate = estimates.get(position);
        holder.address.setText(currentEstimate);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewClick(v, currentEstimate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return estimates.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView address;
        private LinearLayout cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.address = (TextView) itemView.findViewById(R.id.address_text);
            this.cardView = (LinearLayout) itemView.findViewById(R.id.card_view);
        }
    }

    public interface ViewClickListener {
        void onViewClick(View view, String currentEstimate);
    }
}

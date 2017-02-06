package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ordermanagement.Models.Order;

import java.util.ArrayList;

/**
 * Created by audyf on 2/5/2017.
 */
public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.MyVH> {
    private Context mContext;
    private ArrayList<Order> orders;
    public OrdersListAdapter(Context mContext, ArrayList<Order> orders) {
        this.mContext = mContext;
        this.orders = orders;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setProviders(ArrayList<Order> providers) {
        this.orders = providers;
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        Order provider=orders.get(position);
        holder.customerName.setText(provider.getCustomerName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView customerName;
        public MyVH(View convertView) {
            super(convertView);
            customerName= (TextView) convertView.findViewById(R.id.name);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,NewOrderActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

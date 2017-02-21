package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.SalesOrder;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by audyf on 2/21/2017.
 */
public class OrdersListAdapter  extends RecyclerView.Adapter<OrdersListAdapter.MyVH> {
    private Context mContext;
    private ArrayList<SalesOrder> salesOrders;
    private String companyId;
    public OrdersListAdapter(Context mContext, ArrayList<SalesOrder> salesOrders) {
        this.mContext = mContext;
        this.salesOrders = salesOrders;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        SalesOrder order=salesOrders.get(position);
//        holder.pic.setText(provider.getName().substring(0,1).toUpperCase());
//        Glide.with(mContext).load(order.getImage()).placeholder(R.drawable.background1).error(R.drawable.background1).into(holder.pic);
        holder.customerName.setText(order.getCustomerName());
        holder.last.setText(order.getId());
    }

    @Override
    public int getItemCount() {
        return salesOrders.size();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public ArrayList<SalesOrder> getSalesOrders() {
        return salesOrders;
    }

    public void setSalesOrders(ArrayList<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders;
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView customerName;
//        private CircularImageView pic;
        private TextView last;
        public MyVH(View convertView) {
            super(convertView);

            customerName= (TextView) convertView.findViewById(R.id.name);
            last= (TextView) convertView.findViewById(R.id.last);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        SalesOrder order = salesOrders.get(getAdapterPosition());
                        Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                        intent.putExtra("order", order);
                        intent.putExtra("company",companyId);
                        intent.putExtra("distributor",true);
                        mContext.startActivity(intent);
                    }

            });
        }
    }
}


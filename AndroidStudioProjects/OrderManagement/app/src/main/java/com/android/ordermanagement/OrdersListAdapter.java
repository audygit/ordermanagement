package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Product;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setOrders(ArrayList<SalesOrder> orders) {
        this.salesOrders = orders;
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        SalesOrder order=salesOrders.get(position);
        holder.orderNo.setText("Order No: "+order.getId());
        Product product=order.getProducts().get(0);
        holder.orderText.setText("Product Name: "+product.getName()+"("+product.getQuantity()+")");
        if (position%2==0){
            holder.cont.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }else {
            holder.cont.setBackgroundColor(ContextCompat.getColor(mContext,R.color.grey));
        }
        holder.amount.setText(String.valueOf(order.getTotalAmount()));
//        holder.date.setText(String.valueOf(product.getAmount()));
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
        private TextView orderNo;
        private TextView orderText;
        private RelativeLayout cont;
        private TextView date;
        private TextView amount;
        public MyVH(View convertView) {
            super(convertView);
            amount= (TextView) convertView.findViewById(R.id.amount);
            cont= (RelativeLayout) convertView.findViewById(R.id.layout);
            orderNo= (TextView) convertView.findViewById(R.id.orderNo);
            orderText= (TextView) convertView.findViewById(R.id.product_text);
            date= (TextView) convertView.findViewById(R.id.delivery_date);
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


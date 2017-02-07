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

import com.android.ordermanagement.Models.Order;
import com.android.ordermanagement.Models.Product;

import java.util.ArrayList;

/**
 * Created by audyf on 2/7/2017.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyVH> {
    private Context mContext;
    private ArrayList<Order> orders;

    public OrdersAdapter(Context mContext, ArrayList<Order> orders) {
        this.mContext = mContext;
        this.orders = orders;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        Order order=orders.get(position);
        holder.orderNo.setText("Order No: "+order.getId());
        Product product=order.getProducts().get(0);
        holder.orderText.setText("Product Name: "+product.getName()+"("+product.getQuantity()+" "+product.getUnit()+")");
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
        return orders.size();
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
                    Intent intent=new Intent(mContext,NewOrderActivity.class);
                    intent.putExtra("view",true);
                    intent.putExtra("order",orders.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}


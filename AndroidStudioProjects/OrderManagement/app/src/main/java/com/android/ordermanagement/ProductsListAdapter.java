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
import com.android.ordermanagement.Models.Product;

import java.util.ArrayList;

/**
 * Created by challa on 2/5/2017.
 */
public class ProductsListAdapter  extends RecyclerView.Adapter<ProductsListAdapter.MyVH> {
    private Context mContext;
    private ArrayList<Product> products;
    private boolean viewOnly;
    private String orderId;

    public ProductsListAdapter(Context mContext, ArrayList<Product> products,boolean viewOnly, String orderId) {
        this.mContext = mContext;
        this.products = products;
        this.viewOnly=viewOnly;
        this.orderId = orderId;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setProviders(ArrayList<Product> providers) {
        this.products = providers;
    }
    public void setViewOnly(boolean viewOnly){
        this.viewOnly = viewOnly;
    }
    @Override
    public void onBindViewHolder(MyVH holder, final int position) {
        Product product=products.get(position);
        holder.name.setText(product.getName());
        holder.quantity.setText(String.valueOf(product.getActualQuantity()));
        holder.amount.setText(String.valueOf(product.getAmount()));
        if(viewOnly)
            holder.delete.setVisibility(View.GONE);
        else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NewOrderActivity)mContext).removeProduct(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView quantity;
        private TextView amount;
        private ImageView delete;
        public MyVH(View convertView) {
            super(convertView);
            name= (TextView) convertView.findViewById(R.id.products_name);
            quantity= (TextView) convertView.findViewById(R.id.quantity);
            amount= (TextView) convertView.findViewById(R.id.amount);
            delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnly) {
                        Intent intent = new Intent(mContext, ProductActivity.class);
                        intent.putExtra("product",products.get(getAdapterPosition()));
                        intent.putExtra("order",orderId);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}


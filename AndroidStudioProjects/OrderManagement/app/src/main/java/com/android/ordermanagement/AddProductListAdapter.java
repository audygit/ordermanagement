package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ordermanagement.Models.Product;
import com.android.ordermanagement.Models.ProductListItem;

import java.util.ArrayList;

/**
 * Created by challa on 2/5/2017.
 */
public class AddProductListAdapter extends RecyclerView.Adapter<AddProductListAdapter.MyVH> {
    private Context mContext;
    private ArrayList<ProductListItem> products;

    public AddProductListAdapter(Context mContext, ArrayList<ProductListItem> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_product, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setProviders(ArrayList<ProductListItem> providers) {
        this.products = providers;
    }
    @Override
    public void onBindViewHolder(MyVH holder, final int position) {
        ProductListItem product=products.get(position);
        holder.name.setText(product.getName());
        holder.amount.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView amount;
        public MyVH(View convertView) {
            super(convertView);
            name= (TextView) convertView.findViewById(R.id.products_name);
            amount= (TextView) convertView.findViewById(R.id.amount);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ProductListActivity)mContext).setProduct(getAdapterPosition());
                }
            });
        }
    }
}


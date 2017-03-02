package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Order;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by challa on 2/5/2017.
 */
public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.MyVH> {
    private Context mContext;
    private ArrayList<Customer> customers;
    private boolean isDistributor=false;
    private String order;
    private String tax;
    private String companyId;
    private boolean flag=false;
    private String prefix;

    public CustomersListAdapter(Context mContext, ArrayList<Customer> customers,boolean isDistributor, String order,boolean flag) {
        this.mContext = mContext;
        this.order = order;
        this.customers = customers;
        this.isDistributor=isDistributor;
        this.flag=flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setCustomers(ArrayList<Customer> providers) {
        this.customers = providers;
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        Customer provider=customers.get(position);
//        holder.pic.setText(provider.getName().substring(0,1).toUpperCase());
        Glide.with(mContext).load(provider.getImage()).placeholder(R.drawable.background1).error(R.drawable.background1).into(holder.pic);
        holder.customerName.setText(provider.getName());
        holder.last.setText(provider.getId());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setTax(String tax){
        this.tax = tax;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView customerName;
        private CircularImageView pic;
        private TextView last;
        public MyVH(View convertView) {
            super(convertView);
            pic= (CircularImageView) convertView.findViewById(R.id.pic);
            customerName= (TextView) convertView.findViewById(R.id.name);
            last= (TextView) convertView.findViewById(R.id.last);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag){

                    }else {
                        if (isDistributor) {
                            Customer customer = customers.get(getAdapterPosition());
                            Intent intent = new Intent(mContext, CustomerListActivity.class);
                            intent.putExtra("company", customer.getId());
                            intent.putExtra("tax", customer.getTax());
                            intent.putExtra("prefix", customer.getPrefix());
//                        intent.putExtra("customer", customer);
                            mContext.startActivity(intent);
                        } else {
                            Customer customer = customers.get(getAdapterPosition());
                            Intent intent = new Intent(mContext, AddnewProductActivity.class);
                            intent.putExtra("customer", customer);
                            intent.putExtra("company", companyId);
                            intent.putExtra("tax", tax);
                            intent.putExtra("prefix", prefix);
                            intent.putExtra("count", order);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}

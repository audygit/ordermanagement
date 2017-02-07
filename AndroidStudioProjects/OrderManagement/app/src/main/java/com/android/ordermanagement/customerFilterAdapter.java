package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.ordermanagement.Models.Customer;

import java.util.ArrayList;

/**
 * Created by challa on 2/7/2017.
 */
class CustomerFilterAdapter extends RecyclerView.Adapter<CustomerFilterAdapter.MyVH> {
    private Context mContext;
    private ArrayList<Customer> customers;
    private ArrayList<Integer> selectedCustomers;
    private HandleSelections handleSelections;
    public CustomerFilterAdapter(Context mContext, ArrayList<Customer> customers) {
        this.mContext = mContext;
        this.customers = customers;
    }

    public HandleSelections getHandleSelections() {
        return handleSelections;
    }

    public void setHandleSelections(HandleSelections handleSelections) {
        this.handleSelections = handleSelections;
    }

    public  interface HandleSelections{
        public void onFiltersChanged(int id );
    }
    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, null);
        MyVH vh = new MyVH(view);
        return vh;
    }

    public void setCustomers(ArrayList<Customer> providers) {
        this.customers = providers;
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        Customer provider=customers.get(position);
        holder.customerName.setText(provider.getName());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        private TextView customerName;
        private CheckBox checkBox;
        public MyVH(View convertView) {
            super(convertView);
            checkBox= (CheckBox) convertView.findViewById(R.id.check_box);
            customerName= (TextView) convertView.findViewById(R.id.name);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }else {
                        checkBox.setChecked(true);
                    }
                    Customer customer=customers.get(getAdapterPosition());
                    handleSelections.onFiltersChanged(customer.getId());

                }
            });
        }
    }
}


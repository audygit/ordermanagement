package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ordermanagement.Models.Customer;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    private ArrayList<Customer> customers=new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        getCustomers();
    }
    private void  getCustomers(){
        Customer customer=new Customer(1,"Hari Provision Stores");
        customers.add(customer);
        customers.add(new Customer(2,"Anjaneya Condiments"));
        customers.add(new Customer(3,"Mahalakshmi Wholesalers"));
        customers.add(new Customer(4,"Ambica Store"));
        setup();
    }
    private void setup(){
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter=new CustomersListAdapter(CustomerListActivity.this,customers);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerListActivity.this));
        recyclerView.setAdapter(adapter);
    }
}

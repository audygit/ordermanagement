package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ordermanagement.Models.Order;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    private ArrayList<Order> orders=new ArrayList<>();
    private RecyclerView recyclerView;
    private OrdersListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        getOrders();
    }
    private void  getOrders(){
        Order order=new Order(1,"123","Customer1");
        orders.add(order);
        orders.add(new Order(2,"113","Customer2"));
        orders.add(new Order(3,"121","Customer3"));
        orders.add(new Order(4,"122","Customer4"));
        setup();
    }
    private void setup(){
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter=new OrdersListAdapter(CustomerListActivity.this,orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerListActivity.this));
        recyclerView.setAdapter(adapter);
    }
}

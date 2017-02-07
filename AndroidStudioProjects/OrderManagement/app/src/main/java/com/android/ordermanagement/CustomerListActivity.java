package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.ordermanagement.Models.Customer;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    private ArrayList<Customer> customers=new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersListAdapter adapter;
    private ImageButton search;
    private EditText searchFld;
    private View toolbar;
    private ImageButton close;
    private LinearLayout searchCont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        search= (ImageButton) findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        searchFld= (EditText) findViewById(R.id.searchFld);
        searchCont= (LinearLayout) findViewById(R.id.search_container);
        close= (ImageButton) findViewById(R.id.cancel);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
                searchCont.setVisibility(View.VISIBLE);
            }
        });
        searchFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    search(s.toString());
                }else {
                    adapter.setCustomers(customers);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getCustomers();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.VISIBLE);
                searchCont.setVisibility(View.GONE);
                adapter.setCustomers(customers);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void search(String s){
        ArrayList<Customer> searchedCustomers=new ArrayList<>();
        for (Customer c:customers){
            if (c.getName().toLowerCase().contains(s.toLowerCase())){
                searchedCustomers.add(c);
            }
        }
        adapter.setCustomers(searchedCustomers);
        adapter.notifyDataSetChanged();
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

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
    private TextView head;
    private LinearLayout searchCont;
    private TextView count;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        search= (ImageButton) findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        toolbar=findViewById(R.id.toolbar);
        head = (TextView)findViewById(R.id.head);
        count = (TextView)findViewById(R.id.count);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        head.setText("Customers");
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
        Customer customer=new Customer(1,"Hari Provision Stores", "http://www.filmibeat.com/img/popcorn/profile_photos/mahesh-babu-20140227172800-5310.jpg");
        customers.add(customer);
        customers.add(new Customer(2,"Anjaneya Condiments", "http://www.tollymasala.com/TollyAdmin/gossips/ramcharan-dubbing-rights.jpg"));
        customers.add(new Customer(3,"Mahalakshmi Wholesalers", "http://www.aptoday.com/pic_news/2014/11/18/naga-5170.jpg"));
        customers.add(new Customer(4,"Ambica Store", "http://4.bp.blogspot.com/-aaL_q5XVNDI/Uc0ggCLStnI/AAAAAAAAAjE/3Vn9oI6H9CY/s460/Pawan-Kalyan-006.jpg"));
        setup();
    }
    private void setup(){
        count.setText(customers.size()+" Customers");
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter=new CustomersListAdapter(CustomerListActivity.this,customers);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerListActivity.this));
        recyclerView.setAdapter(adapter);
    }
}

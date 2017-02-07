package com.android.ordermanagement;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Order;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity implements CustomerFilterAdapter.HandleSelections{

    private ArrayList<Order> pendingOrders=new ArrayList<>();
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private ImageButton filters;
    private DrawerLayout drawerLayout;
    private RecyclerView customerListView;
    private ArrayList<Integer> selectedCustomers=new ArrayList<>();
    private  ArrayList<Customer> customers=new ArrayList<>();
    private CustomerFilterAdapter filterAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView= (RecyclerView) findViewById(R.id.pending_orders);
        ordersAdapter=new OrdersAdapter(PendingOrdersActivity.this,pendingOrders);
        filters= (ImageButton) findViewById(R.id.filters);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        customerListView= (RecyclerView) findViewById(R.id.customers_list);
        filterAdapter=new CustomerFilterAdapter(PendingOrdersActivity.this,customers);
        customerListView.setLayoutManager(new LinearLayoutManager(PendingOrdersActivity.this));
        customerListView.setAdapter(filterAdapter);
        filterAdapter.setHandleSelections(PendingOrdersActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrdersActivity.this));
        recyclerView.setAdapter(ordersAdapter);
        getOrders();
    }
    private void getOrders(){
        try {
//            JSONObject jsonObject=new JSONObject("{\"result\":{\"orders\": [{\"id\": 1, \"customer_id\":1, \"customer_name\": \"Hari Provision Stores\", \"total_amount\": 180248.85, \"products\": [{\"id\": 1, \"name\" : \"chocolate 44 GRAMS\", \"quantity\": 40,\"billed_quantity\":35, \"unit\": \"cases\", \"amount\": 48882}], \"service_tax\": 12., \"vat\":21629}]}}");
            JSONObject jsonObject=new JSONObject("{\"result\":{\"orders\":[{\"id\":1,\"customer_id\":1,\"customer_name\":\"Hari Provision Stores\"," +
                    "\"total_amount\":180248.85,\"products\":[{\"id\":1,\"name\":\"AMBICA MAHALAKSHMI DHOOP 44 GRAMS\",\"quantity\":40,\"unit\":\"cases\"," +
                    "\"amount\":48882},{\"id\":2,\"name\":\"AMBICA ATHISAYA DHOOP 12 CONES (BOX)\",\"quantity\":20,\"unit\":\"cases\",\"amount\":984}," +
                    "{\"id\":3,\"name\":\"AMBICA ALL DAYS 7IN1 - 60 STICKS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":20},{\"id\":4,\"name\":\"AMBICA NEW GULAB 70 GRAMS\"," +
                    "\"quantity\":5,\"unit\":\"cases\",\"amount\":144.65}],\"service_tax\":12.26,\"vat\":21629},{\"id\": 2,\"customer_id\":23,\"customer_name\":\"Anjaneya Condiments\"," +
                    "\"total_amount\":328358.40,\"products\":[{\"id\":5,\"name\":\"AMBICA NEW BANGARAM 75 GRAMS(BOX)\",\"quantity\":10,\"unit\":\"cases\",\"amount\":27825.60}," +
                    "{\"id\":2,\"name\":\"AMBICAATHISAYADHOOP12CONES(BOX)\",\"quantity\":10,\"unit\":\"cases\",\"amount\":492},{\"id\":6,\"name\":\"AmbicaNityaPoojaSparsha(Box)-1Grams\"," +
                    "\"quantity\":70,\"unit\":\"cases\",\"amount\":153568.80},{\"id\":1,\"name\":\"AMBICAMAHALAKSHMIDHOOP44GRAMS\",\"quantity\":40,\"unit\":\"cases\",\"amount\":48882}]," +
                    "\"service_tax\":12.26,\"vat\":39403},{\"id\": 3,\"cusoomer_id\":75,\"customer_name\":\"MahalakhsmiWholesalers\",\"total_amount\":96929.35," +
                    "\"products\":[{\"id\":7,\"name\":\"AMBICASUBHALAGNAMPOOJA8STICKS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":12411.75}," +
                    "{\"id\":8,\"name\":\"AMBICAJAVVAJI70GRAMS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":7810}," +
                    "{\"id\":1,\"name\":\"AMBICAMAHALAKSHMIDHOOP44GRAMS\",\"quantity\":40,\"unit\":\"cases\",\"amount\":48882}," +
                    "{\"id\":4,\"name\":\"AMBICANEWGULAB70GRAMS\",\"quantity\":10,\"unit\":\"cases\",\"amount\":27825.60}],\"service_tax\":12.26,\"vat\":11631.5}]," +
                    "\"customers\":[{\"id\":1,\"name\":\"HariProvisionStores\"},{\"id\":23,\"name\":\"AnjaneyaCondiments\"},{\"id\":75,\"name\":\"MahalakshmiWholesalers\"}]}}");
            JSONObject job= jsonObject.getJSONObject("result");
           JSONArray jarray= job.getJSONArray("orders");
            for (int i=0;i<jarray.length();i++){
                Gson gson=new Gson();
                Order order=gson.fromJson(jarray.getJSONObject(i).toString(),Order.class);
                pendingOrders.add(order);
            }
            JSONArray cusArray=job.getJSONArray("customers");
            for (int i=0;i<cusArray.length();i++){
                Gson gson=new Gson();
                Customer customer=gson.fromJson(cusArray.getJSONObject(i).toString(),Customer.class);
                customers.add(customer);
                selectedCustomers.add(customer.getId());
            }
            ordersAdapter.setOrders(pendingOrders);
            ordersAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFiltersChanged(int id) {
        if (selectedCustomers.contains(Integer.valueOf(id))){
            selectedCustomers.remove(Integer.valueOf(id));
        }else {
            selectedCustomers.add(Integer.valueOf(id));
        }
        ArrayList <Order> orders=new ArrayList<>();
        for (Order order:pendingOrders){
            if (selectedCustomers.contains(Integer.valueOf(order.getCustomerId()))){
                orders.add(order);
            }else {

            }
        }
        ordersAdapter.setOrders(orders);
        ordersAdapter.notifyDataSetChanged();
    }
}



package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ordermanagement.Models.Order;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity {

    private ArrayList<Order> pendingOrders=new ArrayList<>();
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView= (RecyclerView) findViewById(R.id.pending_orders);
        ordersAdapter=new OrdersAdapter(PendingOrdersActivity.this,pendingOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrdersActivity.this));
        recyclerView.setAdapter(ordersAdapter);
        getOrders();
    }
    private void getOrders(){
        try {
            JSONObject jsonObject=new JSONObject("{\"result\":{\"orders\": [{\"id\": 1, \"customer_id\":001, \"customer_name\": \"Hari Provision Stores\", \"total_amount\": 180248.85, \"products\": [{\"id\": 1, \"name\" : \"chocolate 44 GRAMS\", \"quantity\": 40,\"billed_quantity\":35, \"unit\": \"cases\", \"amount\": 48882}], \"service_tax\": 12.00, \"vat\":21629}]}}");
//            JSONObject jsonObject=new JSONObject("{“result”:{“orders”: [{“id: 1”, “customer_id”:001, “customer_name”: “Hari Provision Stores”, “total_amount”: 180248.85, “products”: [{“id”: 1, “name” : “AMBICA MAHALAKSHMI DHOOP 44 GRAMS”, “quantity”: 40, “unit”: “cases”, “amount”: 48882},{“id”: 2, “name” : “AMBICA ATHISAYA DHOOP 12 CONES (BOX)”, “quantity”: 20, “unit”: “cases”, “amount”: 98400},{“id”: 3, “name” : “AMBICA ALL DAYS 7IN1 - 60 STICKS”, “quantity”: 5, “unit”: “cases”, “amount”: 20,166.85},{“id”: 4, “name” : “AMBICA NEW GULAB 70 GRAMS”, “quantity”: 5, “unit”: “cases”, “amount”: 14004.65}]}, “service_tax”: 12.00, “vat”:21629},{“id: 2”, “customer_id”: 0023, “customer_name”: “Anjaneya Condiments”, “total_amount”: 328358.40, “products”: [{“id”: 5, “name” : “AMBICA NEW BANGARAM 75 GRAMS(BOX)”, “quantity”: 10, “unit”: “cases”, “amount”: 27825.60},  {“id”: 2, “name” : ““AMBICA ATHISAYA DHOOP 12 CONES (BOX)”, “quantity”: 10, “unit”: “cases”, “amount”: 49200},{“id”: 6, “name” : “Ambica Nitya Pooja Sparsha(Box)-100 Grams”, “quantity”: 70, “unit”: “cases”, “amount”: 153568.80},{“id”: 1, “name” : “AMBICA MAHALAKSHMI DHOOP 44 GRAMS”, “quantity”: 40, “unit”: “cases”, “amount”: 48882}]}, “service_tax”: 12.00, “vat”:39403.00}, {“id: 3”, “cusoomer_id”: 078, “customer_name”: “Mahalakhsmi Wholesalers”, “total_amount”: 96929.35, “products”: [{“id”: 7, “name” : “AMBICA SUBHALAGNAM POOJA 8 STICKS”, “quantity”: 5, “unit”: “cases”, “amount”: 12411.75},{“id”: 8, “name” : “AMBICA JAVVAJI 70 GRAMS”, “quantity”: 5, “unit”: “cases”, “amount”: 7810},{“id”: 1, “name” : “AMBICA MAHALAKSHMI DHOOP 44 GRAMS”, “quantity”: 40, “unit”: “cases”, “amount”: 48882},{“id”: 4, “name” : “AMBICA NEW GULAB 70 GRAMS”, “quantity”: 10, “unit”: “cases”, “amount”: 27825.60}]}, “service_tax”: 12.00, “vat”:11631.5}" +
//                    "] , “customers”: [{“id”: 1, “name”: “Hari Provision Stores”}, {“id”: 1, “name”: “Anjaneya Condiments”}, {“id”: 1, “name”: “Mahalakshmi Wholesalers”}]}}");
           JSONObject job= jsonObject.getJSONObject("result");
           JSONArray jarray= job.getJSONArray("orders");
            for (int i=0;i<jarray.length();i++){
                Gson gson=new Gson();
                Order order=gson.fromJson(jarray.getJSONObject(i).toString(),Order.class);
                pendingOrders.add(order);
            }
            ordersAdapter.setOrders(pendingOrders);
            ordersAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

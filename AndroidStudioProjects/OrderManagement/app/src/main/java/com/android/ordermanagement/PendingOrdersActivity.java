package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

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
    private TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView= (RecyclerView) findViewById(R.id.pending_orders);
        count = (TextView)findViewById(R.id.count);
        ordersAdapter=new OrdersAdapter(PendingOrdersActivity.this,pendingOrders);
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
                    "\"service_tax\":12.26,\"vat\":39403},{\"id\": 3,\"customer_id\":078,\"customer_name\":\"MahalakhsmiWholesalers\",\"total_amount\":96929.35," +
                    "\"products\":[{\"id\":7,\"name\":\"AMBICASUBHALAGNAMPOOJA8STICKS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":12411.75}," +
                    "{\"id\":8,\"name\":\"AMBICAJAVVAJI70GRAMS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":7810}," +
                    "{\"id\":1,\"name\":\"AMBICAMAHALAKSHMIDHOOP44GRAMS\",\"quantity\":40,\"unit\":\"cases\",\"amount\":48882}," +
                    "{\"id\":4,\"name\":\"AMBICANEWGULAB70GRAMS\",\"quantity\":10,\"unit\":\"cases\",\"amount\":27825.60}],\"service_tax\":12.26,\"vat\":11631.5}]," +
                    "\"customers\":[{\"id\":1,\"name\":\"HariProvisionStores\"},{\"id\":1,\"name\":\"AnjaneyaCondiments\"}," +
                    "{\"id\":1,\"name\":\"MahalakshmiWholesalers\"}]}}");
            JSONObject job= jsonObject.getJSONObject("result");
           JSONArray jarray= job.getJSONArray("orders");
            for (int i=0;i<jarray.length();i++){
                Gson gson=new Gson();
                Order order=gson.fromJson(jarray.getJSONObject(i).toString(),Order.class);
                pendingOrders.add(order);
            }
            ordersAdapter.setOrders(pendingOrders);
            count.setText(pendingOrders.size()+" Orders");
            ordersAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}



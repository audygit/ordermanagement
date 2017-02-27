package com.android.ordermanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Order;
import com.android.ordermanagement.Models.Product;
import com.android.ordermanagement.Models.SalesOrder;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity implements CustomerFilterAdapter.HandleSelections{

    private ArrayList<SalesOrder> orders=new ArrayList<>();
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private ImageButton filters;
//    private DrawerLayout drawerLayout;
//    private RecyclerView customerListView;
    private TextView head;
    private ArrayList<String> selectedCustomers=new ArrayList<>();
    private ArrayList<Customer> customers=new ArrayList<>();
//    private CustomerFilterAdapter filterAdapter;
    private int type;
    private TextView count;
    private boolean isPending=true;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView= (RecyclerView) findViewById(R.id.pending_orders);
        count = (TextView)findViewById(R.id.count);
        ordersAdapter=new OrdersAdapter(PendingOrdersActivity.this,orders);
        filters= (ImageButton) findViewById(R.id.filters);
        filters.setVisibility(View.GONE);
//        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
//        filters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.RIGHT);
//            }
//        });
        head= (TextView) findViewById(R.id.head);
        type=getIntent().getIntExtra("type",0);
        if (type==2){
            isPending=false;
            head.setText("Completed Orders");
        }
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        customerListView= (RecyclerView) findViewById(R.id.customers_list);
//        filterAdapter=new CustomerFilterAdapter(PendingOrdersActivity.this,customers);
//        customerListView.setLayoutManager(new LinearLayoutManager(PendingOrdersActivity.this));
//        customerListView.setAdapter(filterAdapter);
//        filterAdapter.setHandleSelections(PendingOrdersActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrdersActivity.this));
        recyclerView.setAdapter(ordersAdapter);
        getOrders();
    }
    private void getOrders(){
        JSONObject params = new JSONObject();
        String url = URLUtils.SALES_DASHBOARD;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String company = preferences.getString("salesCode", "");
        try {
            params.put("SalesMenCode",String.valueOf(company) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("SalesMen_Pending_Sales");


                            if(array.length()>0){
                                for (int i=0;i<array.length();i++) {
                                    String orderNo = array.getJSONObject(i).getString("Order_No");
                                    SalesOrder sOrder=new SalesOrder();
                                    boolean newOder=true;
                                    int position=0;
                                    for (SalesOrder order:orders){
                                        if (order.getId().equalsIgnoreCase(orderNo)){
                                            sOrder=order;
                                            newOder=false;
                                            break;
                                        }else {
                                            position=position+1;
                                        }
                                    }
                                    if (newOder){
                                        double amount=array.getJSONObject(i).getDouble("Total_Order_Amount");
                                        String cusName= array.getJSONObject(i).getString("Customer_Name");
                                        String date=array.getJSONObject(i).getString("Order_Date");
                                        double taxAm=array.getJSONObject(i).getDouble("Tax_Amount");
                                        SalesOrder salesOrder=new SalesOrder(orderNo,date,cusName,amount);
                                        salesOrder.setTaxAmount(taxAm);
                                        salesOrder.setTotalAmount(amount);
                                        salesOrder.setDestination(array.getJSONObject(i).getString("Destination"));
                                        salesOrder.setId(array.getJSONObject(i).getString("Order_No"));
                                        salesOrder.setSalesmenCode(array.getJSONObject(i).getString("Salesmen_Code"));
                                        salesOrder.setSaleOrderType(array.getJSONObject(i).getString("SaleOrdertype"));
                                        salesOrder.setTransporter(array.getJSONObject(i).getString("Transporter"));
                                        salesOrder.setStatus(array.getJSONObject(i).getString("Status"));
                                        salesOrder.setTotalQtyCases(array.getJSONObject(i).getDouble("Total_Qty_In_Cases"));
                                        salesOrder.setTotalQtyPack(array.getJSONObject(i).getDouble("Total_Qty_In_Packs"));
                                        salesOrder.setTotalQtyKgs(array.getJSONObject(i).getDouble("Total_Qty_In_Kgs"));
                                        salesOrder.setTaxpercent(array.getJSONObject(i).getDouble("Tax_Percentage"));
                                        salesOrder.setTaxClass(array.getJSONObject(i).getString("Tax_Class"));
                                        salesOrder.setTaxAmount(array.getJSONObject(i).getDouble("Tax_Amount"));
                                        salesOrder.setSubTotal(array.getJSONObject(i).getDouble("Sub_Toal"));
                                        salesOrder.setDiscountType(array.getJSONObject(i).getString("Discounttype"));
                                        salesOrder.setDate(array.getJSONObject(i).getString("Order_Date"));
                                        salesOrder.setUserType(array.getJSONObject(i).getString("User_Type"));
                                        sOrder=salesOrder;
                                    }
                                    ArrayList<Product> products= sOrder.getProducts();
                                    if (products==null){
                                        products=new ArrayList<>();
                                    }

                                    String itemCode = array.getJSONObject(i).getString("Item_Code");
                                    String itemName= array.getJSONObject(i).getString("Item_Name");
                                    int qc= array.getJSONObject(i).getInt("Qty_In_Cases");
                                    int qu= array.getJSONObject(i).getInt("Qty_In_Units");
                                    double qp= array.getJSONObject(i).getDouble("Qty_In_Packs");
                                    double weight=array.getJSONObject(i).getDouble( "Weight_In_Kgs");
                                    double price= Double.parseDouble(array.getJSONObject(i).getString("Price"));
                                    double billedQuantity=array.getJSONObject(i).getDouble("Billd_Qty");
                                    double actualQuantity=array.getJSONObject(i).getDouble("Actual_Qty");
                                    String rate= String.valueOf(array.getJSONObject(i).getDouble("Rate"));
                                    String uom= array.getJSONObject(i).getString("UOM");
                                    double amount=array.getJSONObject(i).getDouble("Amount");
                                    Product product=new Product(itemCode,itemName,qc,qu,qp,price,amount,weight,actualQuantity,billedQuantity,rate,uom);
                                    if (newOder) {
                                        products.add(product);
                                        sOrder.setProducts(products);
                                        if (sOrder.getStatus().equalsIgnoreCase("created")&&isPending) {
                                            orders.add(sOrder);
                                        }else {
                                            if (sOrder.getStatus().equalsIgnoreCase("approved")&&!isPending) {
                                                orders.add(sOrder);
                                            }
                                        }
                                    }else {
                                        products.add(product);
                                        sOrder.setProducts(products);
                                        orders.remove(position);
                                        orders.add(sOrder);
                                    }
                                }
                                ordersAdapter.setOrders(orders);
                                count.setText(orders.size()+" Orders");
                                ordersAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PendingOrdersActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/xml";
            }
        };
        int socketTimeout = 15000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postQuestionRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(this).addToRequestQueue(postQuestionRequest);

    }
//    private void getOrders(){
//        try {
////            JSONObject jsonObject=new JSONObject("{\"result\":{\"orders\": [{\"id\": 1, \"customer_id\":1, \"customer_name\": \"Hari Provision Stores\", \"total_amount\": 180248.85, \"products\": [{\"id\": 1, \"name\" : \"chocolate 44 GRAMS\", \"quantity\": 40,\"billed_quantity\":35, \"unit\": \"cases\", \"amount\": 48882}], \"service_tax\": 12., \"vat\":21629}]}}");
//            JSONObject jsonObject=new JSONObject("{\"result\":{\"orders\":[{\"id\":1,\"customer_id\":1,\"customer_name\":\"Hari Provision Stores\"," +
//                    "\"total_amount\":180248.85,\"products\":[{\"id\":1,\"name\":\"AMBICA MAHALAKSHMI DHOOP 44 GRAMS\",\"quantity\":40,\"unit\":\"cases\"," +
//                    "\"amount\":48882},{\"id\":2,\"name\":\"AMBICA ATHISAYA DHOOP 12 CONES (BOX)\",\"quantity\":20,\"unit\":\"cases\",\"amount\":984}," +
//                    "{\"id\":3,\"name\":\"AMBICA ALL DAYS 7IN1 - 60 STICKS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":20},{\"id\":4,\"name\":\"AMBICA NEW GULAB 70 GRAMS\"," +
//                    "\"quantity\":5,\"unit\":\"cases\",\"amount\":144.65}],\"service_tax\":12.26,\"vat\":21629},{\"id\": 2,\"customer_id\":23,\"customer_name\":\"Anjaneya Condiments\"," +
//                    "\"total_amount\":328358.40,\"products\":[{\"id\":5,\"name\":\"AMBICA NEW BANGARAM 75 GRAMS(BOX)\",\"quantity\":10,\"unit\":\"cases\",\"amount\":27825.60}," +
//                    "{\"id\":2,\"name\":\"AMBICAATHISAYADHOOP12CONES(BOX)\",\"quantity\":10,\"unit\":\"cases\",\"amount\":492},{\"id\":6,\"name\":\"AmbicaNityaPoojaSparsha(Box)-1Grams\"," +
//                    "\"quantity\":70,\"unit\":\"cases\",\"amount\":153568.80},{\"id\":1,\"name\":\"AMBICAMAHALAKSHMIDHOOP44GRAMS\",\"quantity\":40,\"unit\":\"cases\",\"amount\":48882}]," +
//                    "\"service_tax\":12.26,\"vat\":39403},{\"id\": 3,\"customer_id\":75,\"customer_name\":\"MahalakhsmiWholesalers\",\"total_amount\":96929.35," +
//                    "\"products\":[{\"id\":7,\"name\":\"AMBICASUBHALAGNAMPOOJA8STICKS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":12411.75}," +
//                    "{\"id\":8,\"name\":\"AMBICAJAVVAJI70GRAMS\",\"quantity\":5,\"unit\":\"cases\",\"amount\":7810}," +
//                    "{\"id\":1,\"name\":\"AMBICAMAHALAKSHMIDHOOP44GRAMS\",\"quantity\":40,\"unit\":\"cases\",\"amount\":48882}," +
//                    "{\"id\":4,\"name\":\"AMBICANEWGULAB70GRAMS\",\"quantity\":10,\"unit\":\"cases\",\"amount\":27825.60}],\"service_tax\":12.26,\"vat\":11631.5}]," +
//                    "\"customers\":[{\"id\":1,\"name\":\"HariProvisionStores\"},{\"id\":23,\"name\":\"AnjaneyaCondiments\"},{\"id\":75,\"name\":\"MahalakshmiWholesalers\"}]}}");
//            JSONObject job= jsonObject.getJSONObject("result");
//           JSONArray jarray= job.getJSONArray("orders");
//            for (int i=0;i<jarray.length();i++){
//                Gson gson=new Gson();
//                Order order=gson.fromJson(jarray.getJSONObject(i).toString(),Order.class);
//                pendingOrders.add(order);
//            }
//            JSONArray cusArray=job.getJSONArray("customers");
//            for (int i=0;i<cusArray.length();i++){
//                Gson gson=new Gson();
//                Customer customer=gson.fromJson(cusArray.getJSONObject(i).toString(),Customer.class);
//                customers.add(customer);
//                selectedCustomers.add(customer.getId());
//            }
//            ordersAdapter.setOrders(pendingOrders);
//            count.setText(pendingOrders.size()+" Orders");
//            ordersAdapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onFiltersChanged(String id) {
        if (selectedCustomers.contains(id)){
            selectedCustomers.remove(id);
        }else {
            selectedCustomers.add(id);
        }
        ArrayList <SalesOrder> orders=new ArrayList<>();
        for (SalesOrder order:orders){
            if (selectedCustomers.contains(order.getCustomerName())){
                orders.add(order);
            }else {

            }
        }
        ordersAdapter.setOrders(orders);
        ordersAdapter.notifyDataSetChanged();
    }
}



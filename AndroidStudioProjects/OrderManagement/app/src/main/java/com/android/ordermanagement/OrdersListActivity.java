package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Order;
import com.android.ordermanagement.Models.Product;
import com.android.ordermanagement.Models.SalesOrder;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<SalesOrder> orders=new ArrayList<>();
    private OrdersListAdapter adapter;
    private TextView head;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        head= (TextView) findViewById(R.id.head);
        head.setText("Select Order");
        back= (ImageButton) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter=new OrdersListAdapter(OrdersListActivity.this,orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrdersListActivity.this));
        recyclerView.setAdapter(adapter);
        getOrders();
    }

    @Override
    public void onBackPressed() {
    }

    private void getOrders(){
        JSONObject params = new JSONObject();
        String url = URLUtils.GET_ORDERS;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String company = preferences.getString("company", "");
        try {
            params.put("Creation_Company",String.valueOf(company) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Sales_Details");


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
                                    String uom= array.getJSONObject(i).getString("Uom");
                                    double amount=array.getJSONObject(i).getDouble("Amount");
                                    Product product=new Product(itemCode,itemName,qc,qu,qp,price,amount,weight,actualQuantity,billedQuantity,rate,uom);
                                    if (newOder) {
                                        products.add(product);
                                        sOrder.setProducts(products);
                                        orders.add(sOrder);
                                    }else {
                                        products.add(product);
                                        sOrder.setProducts(products);
                                        orders.remove(position);
                                        orders.add(sOrder);
                                    }
                                }
                                adapter.setSalesOrders(orders);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(OrdersListActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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
}

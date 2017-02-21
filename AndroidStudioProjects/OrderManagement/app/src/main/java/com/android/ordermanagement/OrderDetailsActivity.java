package com.android.ordermanagement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrderDetailsActivity  extends AppCompatActivity {

    private ArrayList<Product> products=new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsListAdapter productsListAdapter;
    private TextView svt;
    private Button confirm;
    private Button edit;
    private TextView vatView;
    private TextView total;
    private ImageButton add;
    private boolean viewOnly;

    private TextView head;
    private ImageView back;
    private String orderString;
//    private Customer customer;
    private String date;
//    private String company;
    private boolean isDistributor;
    private ProgressDialog progressDialog;
    private SalesOrder salesOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        isDistributor=getIntent().getBooleanExtra("distributor",false);
        final Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(calendar1.getTimeZone());
        date = dateFormat.format(calendar1.getTime());
        final Date date = calendar1.getTime();
        recyclerView= (RecyclerView) findViewById(R.id.products_list);
        svt= (TextView) findViewById(R.id.service_tax);
        add= (ImageButton) findViewById(R.id.add);
        vatView= (TextView) findViewById(R.id.vat);
        confirm= (Button) findViewById(R.id.confirm);
        edit= (Button) findViewById(R.id.edit);
        total= (TextView) findViewById(R.id.total);
        head= (TextView) findViewById(R.id.head);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        if (viewOnly){
//            add.setVisibility(View.GONE);
//            confirm.setVisibility(View.GONE);
//            order= (Order) getIntent().getSerializableExtra("order");
//            if (order==null){
//                products=new ArrayList<>();
//            }else {
//                products=order.getProducts();
//            }
//            orderString ="Order No A0014"+order.getId();
//            total.setText(String.valueOf(order.getTotalAmount()));
//            edit.setVisibility(View.GONE);
//            svt.setText(String.valueOf(order.getServiceTax()));
//            vatView.setText(String.valueOf(order.getVat()));
//        }else {
//            orderString = "New Order for "+getIntent().getExtras().getString("name");
//            Product product= (Product) getIntent().getSerializableExtra("product");
//            products.add(product);
//            edit.setVisibility(View.VISIBLE);
//            setTotal();
//            vatView.setVisibility(View.GONE);
//        }
//        if (isDistributor){
            add.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            confirm.setText("Send Invoice");
             salesOrder= (SalesOrder) getIntent().getSerializableExtra("order");
            orderString ="Order No "+salesOrder.getId();
            total.setText(String.valueOf(salesOrder.getTotalAmount()));
            svt.setText(String.valueOf(salesOrder.getTaxAmount()));
            if (salesOrder==null){
                products=new ArrayList<>();
            }else {
                products=salesOrder.getProducts();
            }
//        }
        head.setText(orderString);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(NewOrderActivity.this,AddnewProductActivity.class);
//                intent.putExtra("flag",true);
//                intent.putExtra("customer",customer);
//                intent.putExtra("name",getIntent().getExtras().getString("name"));
//                startActivityForResult(intent,22);
//            }
//        });
        productsListAdapter=new ProductsListAdapter(OrderDetailsActivity.this,products,true, orderString);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
        recyclerView.setAdapter(productsListAdapter);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showDialogue("Sending Invoice!");
                    try {
                        sendInvoice();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        });

    }

    public void  sendInvoice()throws JSONException {
        JSONObject params = new JSONObject();
        Gson gson = new Gson();
        String url = URLUtils.INVOICE;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String code = preferences.getString("company", "");
        String user = preferences.getString("user", "");
        String role = preferences.getString("role", "");
        JSONObject temp = new JSONObject();
        temp.put("CustomerCode", code);
        temp.put("UserName", user);
        temp.put("UserType", role);
//        temp.put("SalesExecutiveName", customer.getSalesOrderType());
//        temp.put("SaleOrderType", customer.getSalesExecutiveName());
        temp.put("DispatchThrough", "Ap2132423");
        temp.put("Transport", "00001");
//        temp.put("Destination", "00001");
//        temp.put("DiscountType", "00001");
//        temp.put("TaxClass", "AP TS SALES");
        temp.put("InvNo", "ELU/4");
        temp.put("CreationCompany", code);
        temp.put("InvDate", date);
//        temp.put("CreationCompany", company);
        temp.put("TotalQtyInCases", "1");
//        temp.put("TotoalQtyInUnits", "11");
//        temp.put("TotalQtyInPackets", "132");
//        temp.put("TotalQtyInKgs", "1.90");
        temp.put("SubTotal", "200");
        temp.put("TaxPercentage", "1");
        temp.put("TaxAmount", String.valueOf(salesOrder.getTaxAmount()));
        temp.put("TotalAmount", String.valueOf(salesOrder.getTotalAmount()));
        temp.put("Status", "Created");
//        temp.put("UserType", role);
//        temp.put("UserName", user);

        params.put("HeaderDetails", temp);
        params.put("ItemDetails", gson.toJson(products));


        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialogue();
                        showDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                showDialog();
//                error.printStackTrace();
//                Toast.makeText(NewOrderActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

    public void showDialog(){
        Dialog dialog = new Dialog(OrderDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invoice_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        Button ok = (Button)dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isDistributor) {
                    Intent intent = new Intent(OrderDetailsActivity.this, OrdersListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
            }
        });
    }


    public void setTotal(){
        double totalAmount=0;
        for(Product product: products) {
            totalAmount += product.getAmount();
        }
        double service = totalAmount * 0.05;
        svt.setText(String.format("%.2f", service));
        total.setText(String.format("%.2f", totalAmount + service));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==22&&resultCode==RESULT_OK){
//            Product product= (Product) data.getSerializableExtra("product");
//            products.add(product);
//            setTotal();
//            productsListAdapter.setProviders(products);
//            productsListAdapter.setViewOnly(true);
//            productsListAdapter.notifyDataSetChanged();
//        }
//    }
    //    private void getProducts(){
//        products.add(new Product(1,"Desire",20,"cases",245000));
//        products.add(new Product(2,"Passion",25,"cases",2145000));
//
//        double totalAmount=0;
//        for (Product p:products){
//            totalAmount=totalAmount+p.getAmount();
//        }
//
//    }
    public void dismissDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialogue(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(OrderDetailsActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    protected  void  onPause(){
        super.onPause();
        dismissDialogue();
    }

}


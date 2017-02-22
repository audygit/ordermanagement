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
import android.widget.Toast;

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Order;
import com.android.ordermanagement.Models.Product;
import com.android.ordermanagement.Models.ProductListItem;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NewOrderActivity extends AppCompatActivity {

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
    private Order order;
    private TextView head;
    private ImageView back;
    private String orderString;
    private Customer customer;
    private String date;
    private String company;
    private ProgressDialog progressDialog;
    private String count;
    private double taxPercent;
    private String taxClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        company=getIntent().getStringExtra("company");
        count = getIntent().getStringExtra("count");
        final Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(calendar1.getTimeZone());
        date = dateFormat.format(calendar1.getTime());
        final Date date = calendar1.getTime();
        viewOnly=getIntent().getBooleanExtra("view",false);
        customer = (Customer) getIntent().getExtras().getSerializable("customer");
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
        if (viewOnly){
            add.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            order= (Order) getIntent().getSerializableExtra("order");
            if (order==null){
                products=new ArrayList<>();
            }else {
                products=order.getProducts();
            }
            orderString ="Order No A0014"+order.getId();
            total.setText(String.valueOf(order.getTotalAmount()));
            edit.setVisibility(View.GONE);
            svt.setText(String.valueOf(order.getServiceTax()));
            vatView.setText(String.valueOf(order.getVat()));
        }else {
            orderString = "New Order for "+getIntent().getExtras().getString("name");
            Product product= (Product) getIntent().getSerializableExtra("product");
            products.add(product);
            edit.setVisibility(View.VISIBLE);
            vatView.setVisibility(View.GONE);
        }
        head.setText(orderString);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewOrderActivity.this,AddnewProductActivity.class);
                intent.putExtra("flag",true);
                intent.putExtra("customer",customer);
                intent.putExtra("name",getIntent().getExtras().getString("name"));
                startActivityForResult(intent,22);
            }
        });
        productsListAdapter=new ProductsListAdapter(NewOrderActivity.this,products,true, orderString);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
        recyclerView.setAdapter(productsListAdapter);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue("Placing order!");
                try {
                    postOrder();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsListAdapter.setViewOnly(false);
                productsListAdapter.notifyDataSetChanged();
            }
        });
        showDialogue("Please wait!");
        getTaxInfo();

    }

    public void getTaxInfo(){
        String url = " http://loginwebservice.laksanasoft.com/LoginWebService.asmx/TAXDetails";
        JSONObject params = new JSONObject();
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String companyId = preferences.getString("company", "");
        try {
            params.put("Creation_Company", company);
            params.put("Customer_Id", companyId);
            params.put("Tax_Class", "VAT");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("SalesExecutive_Service");
                            JSONObject tax = results.getJSONObject(0);
                            taxClass = tax.getString("TaxClass");
                            if("VAT".equals(taxClass))
                                taxPercent = Double.valueOf(tax.getString("VATPer"));
                            else if("CST".equals(taxClass))
                                taxPercent = Double.valueOf(tax.getString("CSTPer"));
                            dismissDialogue();
                            setTotal();
                        } catch (JSONException e) {
                            dismissDialogue();
                            Toast.makeText(NewOrderActivity.this, "Error retrieving taxes!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(NewOrderActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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


    public void postOrder() throws JSONException {
        JSONObject params = new JSONObject();
        double subTotal = 0;
        double taxAmount = 0;
        double taxPerentage = 0;
        double total = 0;
        int cases = 0;
        int units = 0;
        int packets = 0;
        float weight = 0;
        for(Product temp : products){
            subTotal += temp.getAmount();
            cases += temp.getQuantity();
            weight += temp.getWeightInKgs();
        }
        units = cases*11;
        packets = cases*132;
        taxAmount = total*taxPerentage/100;
        total = subTotal+taxAmount;

        Gson gson = new Gson();
        String url = URLUtils.POST_ORDER;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String code = preferences.getString("company", "");
        String user = preferences.getString("user", "");
        String role = preferences.getString("role", "");
        String salesType = preferences.getString("salesType", "");
        JSONObject temp = new JSONObject();
        temp.put("CustomerCode", code);
        temp.put("UserName", user);
        temp.put("UserType", role);
        temp.put("SalesExecutiveName", customer.getSalesOrderType());
        temp.put("SaleOrderType", customer.getSalesExecutiveName());
        temp.put("DispatchThrough", "Ap2132423");
        temp.put("Transport", "00001");
        temp.put("Destination", customer.getCity());
        temp.put("DiscountType", "00001");
//        temp.put("TaxClass", "AP TS SALES");
        temp.put("InvNo", salesType.substring(0,3)+"/"+count);
        temp.put("CreationCompany", company);
        temp.put("InvDate", date);
//        temp.put("CreationCompany", company);
        temp.put("OrderDate", date);
        temp.put("TotalQtyInCases", String.valueOf(cases));
        temp.put("TotoalQtyInUnits", String.valueOf(units));
        temp.put("TotalQtyInPackets", String.valueOf(packets));
        temp.put("TotalQtyInKgs", String.valueOf(weight));
        temp.put("SubTotal", String.valueOf(subTotal));
        temp.put("TaxPercentage", String.valueOf(taxPerentage));
        temp.put("TaxAmount", String.valueOf(taxAmount));
        temp.put("TotalAmount", String.valueOf(total));
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
        Dialog dialog = new Dialog(NewOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_popup);
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
                Intent intent=new Intent(NewOrderActivity.this,OrdersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void removeProduct(int position){
        products.remove(position);
        setTotal();
        productsListAdapter.setProviders(products);
        productsListAdapter.setViewOnly(true);
        productsListAdapter.notifyDataSetChanged();
    }

    public void setTotal(){
        double totalAmount=0;
        for(Product product: products) {
            totalAmount += product.getAmount();
        }
        double service = totalAmount * taxPercent/100;
        svt.setText(String.format("%.2f", service));
        total.setText(String.format("%.2f", totalAmount + service));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==22&&resultCode==RESULT_OK){
            Product product= (Product) data.getSerializableExtra("product");
            products.add(product);
            setTotal();
            productsListAdapter.setProviders(products);
            productsListAdapter.setViewOnly(true);
            productsListAdapter.notifyDataSetChanged();
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
             Product   product = (Product) data.getExtras().getSerializable("product");
                int pos=data.getIntExtra("position",0);
                products.remove(pos);
                products.add(pos,product);
                productsListAdapter.setProviders(products);
                productsListAdapter.notifyDataSetChanged();
            }
        }
    }
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
            progressDialog = new ProgressDialog(NewOrderActivity.this);
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
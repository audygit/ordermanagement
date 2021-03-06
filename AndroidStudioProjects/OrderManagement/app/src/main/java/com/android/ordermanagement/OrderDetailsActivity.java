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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String invNo;
    private LinearLayout taxLayout;
    private EditText taxE;
    private double tax=0.0;
    private double taxAmount;
    private double totalAmount;
    private String inv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        isDistributor=getIntent().getBooleanExtra("distributor",false);
        invNo=getIntent().getStringExtra("invNo");
        final Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        taxLayout = (LinearLayout)findViewById(R.id.tax_layout);
        if(isDistributor)
            taxLayout.setVisibility(View.VISIBLE);
        taxE = (EditText)findViewById(R.id.tax);

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
            taxE.setText(String.valueOf(salesOrder.getTaxpercent()));
            taxE.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()))
                        tax = 0.0;
                    else{
                        tax = Double.valueOf(s.toString());
                    }
                    taxAmount = salesOrder.getSubTotal()*tax/100;
                    totalAmount = salesOrder.getSubTotal() + taxAmount;
                    total.setText(String.valueOf(totalAmount));
                    svt.setText(String.valueOf(taxAmount));
                }
            });
        totalAmount = salesOrder.getTotalAmount();
        taxAmount = salesOrder.getTaxAmount();
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
                        getNewData();


            }
        });

    }
    private void getNewData() {
        JSONObject params = new JSONObject();
        String url = URLUtils.GET_CUSTOMERS_LIST;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String comp = preferences.getString("company", "");
        try {
            params.put("Creation_Company", String.valueOf(comp));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                            JSONArray res = response.getJSONArray("Dist_Dash_Sales");
                            JSONArray inv = response.getJSONArray("Invoice_No");
                            JSONArray ord = response.getJSONArray("Order_No");
                            JSONArray transport = response.getJSONArray("Transport_Details");
                            ArrayList<String> tr = new ArrayList<>();
                            for(int i=0;i<transport.length();i++){
                                tr.add(transport.getJSONObject(i).getString("Transporter_Name"));
                            }
                            JSONArray sales = response.getJSONArray("SaleOrderType_Details");
                            String salesType = ((JSONObject) sales.get(0)).getString("Salestype_Name");
                            String prefix = ((JSONObject) sales.get(0)).getString("Prefix");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("salesType", salesType);
                            editor.putString("prefix", prefix);
                            invNo = ((JSONObject) inv.get(0)).getString("Invoice_No");
                            editor.putString("inv",invNo);
                            Gson gson = new Gson();
                            editor.putString("transport", gson.toJson(tr));
                            boolean commit = editor.commit();

                            JSONObject te = (JSONObject) res.get(0);
//                                weeklyCount=res.getInt("weekly_count");
//                                weeklyTotal=res.getInt("weekly_total");
//                                monthlyCount=res.getInt("monthly_count");
//                                monthlyTotal=res.getInt("monthly_total");
//                                yearlyCount=res.getInt("yearly_count");
//                                yearlyTotal=res.getInt("yearly_total");
//                                pendingCount=res.getInt("pending_count");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            sendInvoice();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(DashBoardActivity.this);
//                        viewPager.setAdapter(pagerAdapter);
//                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                            @Override
//                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                            }
//
//                            @Override
//                            public void onPageSelected(int position) {
//                                if (position == 0) {
//                                    first.setImageResource(R.drawable.circle);
//                                    second.setImageResource(R.drawable.grey_circle);
//                                } else if (position == 1) {
//                                    first.setImageResource(R.drawable.grey_circle);
//                                    second.setImageResource(R.drawable.circle);
//                                } else if (position == 2) {
//                                    first.setImageResource(R.drawable.grey_circle);
//                                    second.setImageResource(R.drawable.grey_circle);
//                                }
//                            }
//
//                            @Override
//                            public void onPageScrollStateChanged(int state) {
//
//                            }
//                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(OrderDetailsActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
            }
        }) {
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
    public void  sendInvoice()throws JSONException {
        JSONObject params = new JSONObject();
        Gson gson = new Gson();
        String url = URLUtils.INVOICE;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String code = preferences.getString("company", "");
        String user = preferences.getString("user", "");
        String prefix = preferences.getString("prefix", "");
        String invN = preferences.getString("inv", "");
        String custId = preferences.getString("customerId", "");
        JSONObject temp = new JSONObject();
        temp.put("CustomerCode", salesOrder.getCustomerId());
        temp.put("UserName", user);
        temp.put("UserType", "Customer");
//        temp.put("SalesExecutiveName", salesOrder.getSalesmenCode());
//        temp.put("SaleOrderType", salesOrder.getSaleOrderType());
        temp.put("DispatchThrough", "Ap2132423");
//        temp.put("Transport", salesOrder.getTransporter());
//        temp.put("Destination", salesOrder.getDestination());
//        temp.put("DiscountType", salesOrder.getDiscountType());
//        temp.put("TaxClass", "AP TS SALES");
        temp.put("InvNo", prefix+invN);
        inv=prefix+invN;
        temp.put("OrderNo", salesOrder.getId());
        temp.put("CreationCompany", custId);
        temp.put("InvDate", date);
//        temp.put("CreationCompany", company);
        temp.put("TotalQtyInCases", salesOrder.getTotalQtyCases());
//        temp.put("TotoalQtyInUnits", salesOrder.getTotalQtyCases()*11);
//        temp.put("TotalQtyInPackets", salesOrder.getTotalQtyPack());
//        temp.put("TotalQtyInKgs", salesOrder.getTotalQtyKgs());
        temp.put("SubTotal", salesOrder.getSubTotal());
        temp.put("TaxPercentage", tax);
        temp.put("TaxAmount", String.valueOf(taxAmount));
        temp.put("TotalAmount", String.valueOf(totalAmount));
        temp.put("Status", "Created");
//        temp.put("UserType", role);
//        temp.put("UserName", user);

        params.put("HeaderDetails", temp);
        JSONArray returnProducts= new JSONArray();
        for(Product pro :products){
            JSONObject t = new JSONObject();
            t.put("ItemCode", pro.getId());
            t.put("ItemName", pro.getName());
            t.put("QtyInCases", pro.getQuantity());
            t.put("quantityUts", pro.getQuantityUts());
            t.put("quantityPkgs", pro.getQuantityPkgs());
            t.put("Price", pro.getPrice());
            t.put("Amount", pro.getAmount());
            t.put("WeightInKgs", pro.getWeightInKgs());
            t.put("ActualQty", pro.getActualQuantity());
            t.put("BilledQty", pro.getBilledQuantity());
            t.put("Rate", pro.getPrice());
            t.put("Per", pro.getUom());
            returnProducts.put(t);
        }
        params.put("ItemDetails", returnProducts);


        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialogue();
//                        try {
////                            inv = response.getString("Inv_No");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        showDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                Toast.makeText(OrderDetailsActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();

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
        TextView textView = (TextView)dialog.findViewById(R.id.order_num);
        textView.setText("Invoice No: "+inv);
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
        double sub=0;
        for(Product product: products) {
            sub += product.getAmount();
        }
        taxAmount = sub*tax/100;
        totalAmount = sub + taxAmount;
        total.setText(String.valueOf(totalAmount));
        svt.setText(String.valueOf(taxAmount));
        double service = totalAmount * tax/100 ;
        svt.setText(String.format("%.2f",service));
        total.setText(String.format("%.2f", totalAmount + service));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Product   product = (Product) data.getExtras().getSerializable("product");
                int pos=data.getIntExtra("position",0);
                products.remove(pos);
                products.add(pos,product);
                productsListAdapter.setProviders(products);
                productsListAdapter.notifyDataSetChanged();
                setTotal();
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


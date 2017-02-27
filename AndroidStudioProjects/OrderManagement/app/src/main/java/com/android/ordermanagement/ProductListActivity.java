package com.android.ordermanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Customer;
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

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ArrayList<ProductListItem> products=new ArrayList<>();
    private RecyclerView recyclerView;
    private AddProductListAdapter adapter;
    private ImageButton search;
    private EditText searchFld;
    private View toolbar;
    private TextView head;
    private LinearLayout searchCont;
    private ImageView back;
    private String company;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        search= (ImageButton) findViewById(R.id.search);
        company=getIntent().getStringExtra("company");
        search.setVisibility(View.VISIBLE);
        toolbar=findViewById(R.id.toolbar);
        head = (TextView)findViewById(R.id.head);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        head.setText("Select Product");
        toolbar.setVisibility(View.VISIBLE);
        searchFld= (EditText) findViewById(R.id.searchFld);
        searchCont= (LinearLayout) findViewById(R.id.search_container);
        search.setVisibility(View.GONE);
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
                if (s.length()>=0) {
                    search(s.toString());
                }else {
                    adapter.setProviders(products);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        showDialogue("Getting products");
        getCustomers();
    }
    private void search(String s){
        ArrayList<ProductListItem> searchedCustomers=new ArrayList<>();
        for (ProductListItem c:products){
            if (c.getName().toLowerCase().contains(s.toLowerCase())){
                searchedCustomers.add(c);
            }
        }

        adapter.setProviders(searchedCustomers);
        adapter.notifyDataSetChanged();
    }

    private void getCustomers() {
        JSONObject params = new JSONObject();
        String url = URLUtils.GET_PRODUCTS_LIST;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String name = preferences.getString("user", "");
        try {
            params.put("Creation_Company", company);
            params.put("Customer_Name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            JSONArray results = response.getJSONArray("Item_Details");
                            for (int i = 0; i < results.length(); i++) {
                                ProductListItem temp = gson.fromJson(results.getJSONObject(i).toString(), ProductListItem.class);
                                products.add(temp);
                            }
                            dismissDialogue();
                            setup();
                        } catch (JSONException e) {
                            dismissDialogue();
                            Toast.makeText(ProductListActivity.this, "Error retrieving customers!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(ProductListActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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
//        Product product = new Product();
//        product.setId("00001");
//        product.setName("AMBICA ALL DAYS");
//        product.setPrice(33);
//        product.setQuantity(1);
//        product.setQuantityUts(12);
//        product.setQuantityPkgs(132);
//        product.setWeightInKgs(60.20);
//        product.setActualQuantity(11.10);
//        product.setBilledQuantity(11.0);
//        products.add(p);
//        dismissDialogue();
//        setup();
    }

    private void setup(){
        recyclerView= (RecyclerView) findViewById(R.id.recycle_items);
        adapter=new AddProductListAdapter(ProductListActivity.this,products);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
        recyclerView.setAdapter(adapter);
    }

    public void dismissDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialogue(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ProductListActivity.this);
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

    public void setProduct(int position){
        ProductListItem p = products.get(position);
            Intent intent = new Intent();
            intent.putExtra("product", p);
            setResult(RESULT_OK, intent);
            overridePendingTransition(R.anim.no_change, R.anim.slide_down);
            finish();
    }

}

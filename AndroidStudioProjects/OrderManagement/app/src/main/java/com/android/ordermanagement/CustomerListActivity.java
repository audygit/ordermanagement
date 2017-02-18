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
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Customer;
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

public class CustomerListActivity extends AppCompatActivity {

    private ArrayList<Customer> customers=new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersListAdapter adapter;
    private ImageButton search;
    private EditText searchFld;
    private View toolbar;
    private TextView head;
    private LinearLayout searchCont;
    private TextView count;
    private ImageView back;
    private ProgressDialog progressDialog;
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
                    adapter.setCustomers(customers);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        showDialogue("Getting customers");
        getCustomers();
    }
    private void search(String s){
        ArrayList<Customer> searchedCustomers=new ArrayList<>();
        for (Customer c:customers){
            if (c.getName().toLowerCase().contains(s.toLowerCase())){
                searchedCustomers.add(c);
            }
        }

        if(searchedCustomers.size()==1)
            count.setText(searchedCustomers.size()+" Customer");
        else
            count.setText(searchedCustomers.size()+" Customers");
        adapter.setCustomers(searchedCustomers);
        adapter.notifyDataSetChanged();
    }

    private void getCustomers() {
        JSONObject params = new JSONObject();
        String url = URLUtils.GET_CUSTOMERS_LIST;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String company = preferences.getString("company", "");
        try {
            params.put("Creation_Company", company);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            JSONArray results = response.getJSONArray("Customer_Details");
                            for (int i = 0; i < results.length(); i++) {
                                Customer temp = gson.fromJson(results.getJSONObject(i).toString(), Customer.class);
                                customers.add(temp);
                            }
                            for(Customer cust: customers){
                                JSONArray one = response.getJSONArray("SaleOrderType_Details");
                                JSONObject two = (JSONObject) one.get(0);

                                cust.setSalesOrderType(two.getString("SaleOrdertypeID"));
                                cust.setSalesExecutiveName(two.getString("Salestype_Name"));
                            }
                            dismissDialogue();
                            setup();
                        } catch (JSONException e) {
                            dismissDialogue();
                            Toast.makeText(CustomerListActivity.this, "Error retrieving customers!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(CustomerListActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

    private void setup(){
        if(customers.size()==1)
            count.setText(customers.size()+" Customer");
        else
            count.setText(customers.size()+" Customers");
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter=new CustomersListAdapter(CustomerListActivity.this,customers);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerListActivity.this));
        recyclerView.setAdapter(adapter);
    }

    public void dismissDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialogue(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(CustomerListActivity.this);
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

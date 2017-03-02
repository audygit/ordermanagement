package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText nameFld;
    private EditText addressFld;
    private EditText areaFls;
    private EditText mobileFld;
    private EditText contactPersonFld;
    private EditText TINFld;
    private EditText PANFld;
    private Button add;
    private String name;
    private String address;
    private String area;
    private String mobile;
    private String TIN;
    private String PAN;
    private String contactPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        nameFld= (EditText) findViewById(R.id.name);
        addressFld= (EditText) findViewById(R.id.address);
        areaFls= (EditText) findViewById(R.id.area);
        mobileFld= (EditText) findViewById(R.id.mobile);
        TINFld= (EditText) findViewById(R.id.tin);
        PANFld= (EditText) findViewById(R.id.pan);
        contactPersonFld= (EditText) findViewById(R.id.contactPerson);
        add= (Button) findViewById(R.id.add_customer);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameFld.getText().toString()!=null&&!nameFld.getText().toString().isEmpty()){
                    if (addressFld.getText().toString()!=null&&!addressFld.getText().toString().isEmpty()){
                        if (areaFls.getText().toString()!=null&&!areaFls.getText().toString().isEmpty()){
                            if (mobileFld.getText().toString()!=null&&!mobileFld.getText().toString().isEmpty()){
                                if (TINFld.getText().toString()!=null&&!TINFld.getText().toString().isEmpty()){
                                    if (PANFld.getText().toString()!=null&&!PANFld.getText().toString().isEmpty()){
                                        if (contactPersonFld.getText().toString()!=null&&!contactPersonFld.getText().toString().isEmpty()){
                                            name=nameFld.getText().toString();
                                            address=addressFld.getText().toString();
                                            area=areaFls.getText().toString();
                                            TIN=TINFld.getText().toString();
                                            PAN=PANFld.getText().toString();
                                            mobile=mobileFld.getText().toString();
                                            contactPerson=contactPersonFld.getText().toString();
                                            addContact();

                                        }else {
                                            Toast.makeText(AddCustomerActivity.this,"Contact Person can not be empty",Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(AddCustomerActivity.this,"PAN number can not be empty",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(AddCustomerActivity.this,"TIN number can not be empty",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(AddCustomerActivity.this,"Mobile no can not be empty",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(AddCustomerActivity.this,"Area can not be empty",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(AddCustomerActivity.this,"Address can not be empty",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(AddCustomerActivity.this,"Name can not be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void  addContact(){
        JSONObject dtails=new JSONObject();
        JSONObject params = new JSONObject();
        String url = URLUtils.ADD_CUSTOMER;
        try {
            SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
            String user = preferences.getString("user", "");
            String comp = preferences.getString("id", "");
            params.put("Customer_Name",name);
//            params.put("CustomerCode",name);
            params.put("CreationCompany",String.valueOf(comp));
            params.put("Address1", address);
            params.put("Area", area);
            params.put("ContactPerson", contactPerson);
            params.put("MobileNo", mobile);
            params.put("TINNO",TIN);
            params.put("PANNO", PAN);
            params.put("UserName",user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dtails.put("CustomerDetals",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, dtails,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onBackPressed();
//                        try {
//                            JSONArray array = response.getJSONArray("User_Details");
//                            if(array.length()>0) {
//                            }
//                        } catch (JSONException e) {
//                            Toast.makeText(AddCustomerActivity.this, "Error posting!", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddCustomerActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

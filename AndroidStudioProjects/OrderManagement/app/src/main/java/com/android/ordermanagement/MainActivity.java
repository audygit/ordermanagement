package com.android.ordermanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private boolean isDistributor;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        if(preferences.getAll().size()==0) {
            setContentView(R.layout.activity_main);
            userName = (EditText) findViewById(R.id.userName);
            password = (EditText) findViewById(R.id.password);
            login = (Button) findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String passwordStr = password.getText().toString();
                    if (!TextUtils.isEmpty(passwordStr)) {
                        showDialogue("logging in!");
                        login();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            if(preferences.contains("customerId")){
                Intent mainIntent = new Intent(MainActivity.this, DashBoardActivity.class);
                startActivity(mainIntent);
                finish();
            }else{
                Intent mainIntent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    private void getSalesExe(String comp, String temp){
        JSONObject params = new JSONObject();
        String url = URLUtils.SALES_DETAILS_EXE;
        try {
            params.put("Emp_Name", temp);
            params.put("Creation_Company",String.valueOf(comp) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialogue();
                        try {
                            JSONArray array = response.getJSONArray("SalesExecutive_Service");
                            if(array.length()>0){
                                String temp = array.getJSONObject(0).getString("Area_Responsible");
                                String salesCode = array.getJSONObject(0).getString("Salesmen_Code");
                                SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("city", temp);
                                editor.putString("salesCode", salesCode);
                                boolean commit = editor.commit();

                                if(commit){
//                                    Intent mainIntent = new Intent(MainActivity.this, OrdersActivity.class);
//                                    startActivity(mainIntent);
//                                    finish();
                                      Intent mainIntent = new Intent(MainActivity.this, OrdersActivity.class);
                                    startActivity(mainIntent);
                                    finish();

                                }
                            }
                        } catch (JSONException e) {
                            dismissDialogue();
                            Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

    private void login() {
        JSONObject params = new JSONObject();
        String url = URLUtils.LOGIN;
        try {
            params.put("User_Name1", userName.getText().toString());
            params.put("Password1", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("User_Details");
                            if(array.length()>0) {
                                try {
                                    if (array.getJSONObject(0).getString("UserRole") != null) {
                                        String temp = array.getJSONObject(0).getString("UserName");
                                        String role = array.getJSONObject(0).getString("UserRole");
                                        String comp = array.getJSONObject(0).getString("CompanyId");
                                        String emp = array.getJSONObject(0).getString("Emp_Name");
                                        String salesType = array.getJSONObject(0).getString("Sale_Type");
                                        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("user", temp);
                                        editor.putString("role", role);
                                        editor.putString("company", comp);
                                        editor.putString("emp", emp);
                                        editor.putString("salesType", salesType);
                                        boolean commit = editor.commit();
                                        if (commit) {
                                            getSalesExe(comp,emp);
                                        }
                                    }
                                    else {
                                        String temp = array.getJSONObject(0).getString("User_Name");
                                        String role = array.getJSONObject(0).getString("Customer_Type");
                                        if (role.equalsIgnoreCase("Distributor")){
                                            isDistributor=true;
                                        }
                                        String comp = array.getJSONObject(0).getString("Creation_Company");
                                        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("user", temp);
                                        editor.putString("role", role);
                                        editor.putString("company", comp);
                                        boolean commit = editor.commit();
                                        if (commit) {
                                            Intent mainIntent = new Intent(MainActivity.this, DashBoardActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                    }

                                } catch (JSONException ex) {
                                    String temp = array.getJSONObject(0).getString("User_Name");
                                    String role = array.getJSONObject(0).getString("Customer_Type");
                                    String customerId = array.getJSONObject(0).getString("Customer_Id");
                                    String tax = array.getJSONObject(0).getString("Tax_Class");
                                    String name = array.getJSONObject(0).getString("Customer_Name");
                                    if (role.equalsIgnoreCase("Distributor")){
                                        isDistributor=true;
                                    }
                                    String comp = array.getJSONObject(0).getString("Creation_Company");
                                    SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("user", temp);
                                    editor.putString("role", role);
                                    editor.putString("customerId", customerId);
                                    editor.putString("company", comp);
                                    editor.putString("name", name);
                                    editor.putString("tax", tax);
                                    boolean commit = editor.commit();
                                    if (commit) {
                                        Intent mainIntent = new Intent(MainActivity.this, DashBoardActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            dismissDialogue();
                            Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

    public void dismissDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialogue(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity.this);
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
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
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName= (EditText) findViewById(R.id.userName);
        password= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStr=password.getText().toString();
                if (!TextUtils.isEmpty(passwordStr)){
                    showDialogue("logging in!");
                    login();
                }else {
                    Toast.makeText(MainActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login() {
        JSONObject params = new JSONObject();
        String url = URLUtils.LOGIN;
        try {
            params.put("User_Name1", "laksana");
            params.put("Password1", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postQuestionRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialogue();
                        try {
                            JSONArray array = response.getJSONArray("userDetails");
                            if(array.length()>0){
                                String temp = array.getJSONObject(0).getString("UserName");
                                String role = array.getJSONObject(0).getString("UserRole");
                                String comp = array.getJSONObject(0).getString("CompanyId");
                                SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user", temp);
                                editor.putString("role", role);
                                editor.putString("company", comp);
                                boolean commit = editor.commit();
                                if(commit){
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
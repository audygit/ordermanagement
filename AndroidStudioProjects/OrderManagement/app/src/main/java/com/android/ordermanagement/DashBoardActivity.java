package com.android.ordermanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DashBoardActivity extends AppCompatActivity {

    private int weeklyCount;
    private int weeklyTotal;
    private int monthlyCount;
    private int monthlyTotal;
    private int yearlyCount;
    private int yearlyTotal;
    private int pendingCount;
    private int pendingOrder;
    private int completeOrder;

    //    private TextView count;
//    private TextView target;
    private TextView week;
    private TextView month;
    private TextView year;

    private ImageView first;
    private ImageView second;
    private ImageView third;

    private TextView pending;
    private TextView completed;
    private TextView newOrder;
    private ImageButton ham;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    //    private TextView subText;
    private ProgressDialog progressDialog;
    private String fromDate;
    private String toDate;
    private TextView name;
    private String invNo;
    private String ordNo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ham = (ImageButton) findViewById(R.id.ham);
        name = (TextView) findViewById(R.id.name);
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "");
        name.setText(user);
        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setupMenu();
        setup();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupMenu() {
        ListView mMainList = (ListView) findViewById(R.id.main_list);
        final String[] menuList = {"Home", "Logout"};
        final Integer[] menuIcons = {R.drawable.ham, R.drawable.ham, R.drawable.ham, R.drawable.ham, R.drawable.ham};
        DrawerItemCustomAdapter adapter3 = new DrawerItemCustomAdapter(DashBoardActivity.this, R.layout.home_menu_item_list, menuList, menuIcons);
        mMainList.setAdapter(adapter3);
        mMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    SharedPreferences preferences = getSharedPreferences("USER_PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(DashBoardActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setup() {
        first = (ImageView) findViewById(R.id.first);
        second = (ImageView) findViewById(R.id.second);
//        target= (TextView) findViewById(R.id.target);
//        count= (TextView) findViewById(R.id.count);

        month = (TextView) findViewById(R.id.month);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.image_click));
                viewPager.setCurrentItem(0);
            }
        });
        year = (TextView) findViewById(R.id.year);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.image_click));
                viewPager.setCurrentItem(1);
            }
        });
        pending = (TextView) findViewById(R.id.pending);
        completed = (TextView) findViewById(R.id.completed);
        newOrder = (TextView) findViewById(R.id.newOrder);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, OrdersListActivity.class);
                intent.putExtra("inv", invNo);
                startActivity(intent);
            }
        });
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue("please wait!");
                getNewData();
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, OrdersListActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("inv", invNo);
                startActivity(intent);
            }
        });
        final Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
//                calendar1.add(Calendar.DATE, 1);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.HOUR, 0);
        final Date date = calendar1.getTime();
        getData();
    }


    //    private void  getData(){
//
//    }
    private void getData() {
        JSONObject params = new JSONObject();
        String url = URLUtils.GET_CUSTOMERS_LIST;
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String comp = preferences.getString("customerId", "");
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

//                            SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                            JSONArray res = response.getJSONArray("Dist_Dash_Sales");
//                            JSONArray inv = response.getJSONArray("Invoice_No");
//                            JSONArray ord = response.getJSONArray("Order_No");
//                            JSONArray transport = response.getJSONArray("Transport_Details");
//                            ArrayList<String> tr = new ArrayList<>();
//                            for(int i=0;i<transport.length();i++){
//                                tr.add(transport.getJSONObject(i).getString("Transporter_Name"));
//                            }
//                            JSONArray sales = response.getJSONArray("SaleOrderType_Details");
//                            String salesType = ((JSONObject) sales.get(0)).getString("Salestype_Name");
//                            String prefix = ((JSONObject) sales.get(0)).getString("Prefix");
//                            SharedPreferences.Editor editor = preferences.edit();
//                            editor.putString("salesType", salesType);
//                            editor.putString("prefix", prefix);
//
//                            Gson gson = new Gson();
//                            editor.putString("transport", gson.toJson(tr));
//                            boolean commit = editor.commit();
//                            invNo = ((JSONObject) inv.get(0)).getString("Invoice_No");
//                            ordNo = ((JSONObject) ord.get(0)).getString("Order_No");
                            JSONObject te = (JSONObject) res.get(0);
//                                weeklyCount=res.getInt("weekly_count");
//                                weeklyTotal=res.getInt("weekly_total");
//                                monthlyCount=res.getInt("monthly_count");
//                                monthlyTotal=res.getInt("monthly_total");
//                                yearlyCount=res.getInt("yearly_count");
//                                yearlyTotal=res.getInt("yearly_total");
//                                pendingCount=res.getInt("pending_count");
                            pendingOrder = te.getInt("Pending_Orders");
                            completeOrder = te.getInt("Completed_Orders");
                            completed.setText(String.valueOf(completeOrder));
                            pending.setText(String.valueOf(pendingOrder));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissDialogue();
                        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(DashBoardActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (position == 0) {
                                    first.setImageResource(R.drawable.circle);
                                    second.setImageResource(R.drawable.grey_circle);
                                } else if (position == 1) {
                                    first.setImageResource(R.drawable.grey_circle);
                                    second.setImageResource(R.drawable.circle);
                                } else if (position == 2) {
                                    first.setImageResource(R.drawable.grey_circle);
                                    second.setImageResource(R.drawable.grey_circle);
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialogue();
                error.printStackTrace();
                Toast.makeText(DashBoardActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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

                            Gson gson = new Gson();
                            editor.putString("transport", gson.toJson(tr));
                            boolean commit = editor.commit();
                            invNo = ((JSONObject) inv.get(0)).getString("Invoice_No");
                            ordNo = ((JSONObject) ord.get(0)).getString("Order_No");
                            JSONObject te = (JSONObject) res.get(0);
//                                weeklyCount=res.getInt("weekly_count");
//                                weeklyTotal=res.getInt("weekly_total");
//                                monthlyCount=res.getInt("monthly_count");
//                                monthlyTotal=res.getInt("monthly_total");
//                                yearlyCount=res.getInt("yearly_count");
//                                yearlyTotal=res.getInt("yearly_total");
//                                pendingCount=res.getInt("pending_count");
                            pendingOrder = te.getInt("Pending_Orders");
                            completeOrder = te.getInt("Completed_Orders");
                            completed.setText(String.valueOf(completeOrder));
                            pending.setText(String.valueOf(pendingOrder));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissDialogue();
                        Intent intent = new Intent(DashBoardActivity.this, AddnewProductActivity.class);
                        SharedPreferences preferences = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                        String companyId = preferences.getString("company", "");
                        intent.putExtra("company", companyId);
                        intent.putExtra("ord", ordNo);
                        intent.putExtra("type", 1);
                        intent.putExtra("isDistributor", true);
                        startActivity(intent);
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
                Toast.makeText(DashBoardActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DashBoard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
//        private void  getData(){
//
//    }
//    private void getData(){
//        try {
//            JSONObject obj=new JSONObject(" {\"results\": {\"weekly_count\": 0, \"weekly_total\": 0,\"monthly_count\": 0, \"monthly_total\": 0,\"yearly_count\": 0, \"yearly_total\": 0, \"pending_count\":0, \"pending_order\": 0, \"completed_order\":0}}");
//            JSONObject res=obj.getJSONObject("results");
//            weeklyCount=res.getInt("weekly_count");
//            weeklyTotal=res.getInt("weekly_total");
//            monthlyCount=res.getInt("monthly_count");
//            monthlyTotal=res.getInt("monthly_total");
//            yearlyCount=res.getInt("yearly_count");
//            yearlyTotal=res.getInt("yearly_total");
//            pendingCount=res.getInt("pending_count");
//            pendingOrder=res.getInt("pending_order");
//            completeOrder=res.getInt("completed_order");
//            completed.setText(String.valueOf(completeOrder));
//            pending.setText(String.valueOf(pendingOrder));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        CustomPagerAdapter pagerAdapter=new CustomPagerAdapter(DashBoardActivity.this);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position==0){
//                    first.setImageResource(R.drawable.circle);
//                    second.setImageResource(R.drawable.grey_circle);
//                    third.setImageResource(R.drawable.grey_circle);
//                }else if (position==1){
//                    first.setImageResource(R.drawable.grey_circle);
//                    second.setImageResource(R.drawable.circle);
//                    third.setImageResource(R.drawable.grey_circle);
//                }else if (position==2){
//                    first.setImageResource(R.drawable.grey_circle);
//                    second.setImageResource(R.drawable.grey_circle);
//                    third.setImageResource(R.drawable.circle);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            View view = View.inflate(collection.getContext(), R.layout.pager_item, null);
            TextView count = (TextView) view.findViewById(R.id.count);
            TextView target = (TextView) view.findViewById(R.id.target);
            TextView subText = (TextView) view.findViewById(R.id.subtext);
            collection.addView(view, 0);
            if (position == 0) {
                count.setText(String.valueOf(monthlyCount) + "/");
                target.setText(String.valueOf(monthlyTotal));
                subText.setText("This Month");
            } else if (position == 1) {
                count.setText(String.valueOf(yearlyCount) + "/");
                target.setText(String.valueOf(yearlyTotal));
                subText.setText("This Year");
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    //    home screen : {“results”: {“weekly_count”: 13, “weekly_total”: 30,“monthly_count”: 13, “monthly_total”: 30,“yearly_count”: 13, “yearly_total”: 30, “pending_count”:20, “pending_order”: 23, “completed_order”:45}}
//}
    public void dismissDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialogue(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(DashBoardActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialogue();
    }
}

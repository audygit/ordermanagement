package com.android.ordermanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.ordermanagement.Models.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrdersActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        ham= (ImageButton) findViewById(R.id.ham);
        name= (TextView) findViewById(R.id.name);
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

    }
    private void setupMenu(){
        ListView mMainList= (ListView) findViewById(R.id.main_list);
        final String[] menuList={"Home","Distributors","Customers","Sales Persons","Logout"};
        final Integer[] menuIcons = { R.drawable.ham,R.drawable.ham,R.drawable.ham,R.drawable.ham,R.drawable.ham};
        DrawerItemCustomAdapter adapter3 = new DrawerItemCustomAdapter(OrdersActivity.this, R.layout.home_menu_item_list, menuList,menuIcons);
        mMainList.setAdapter(adapter3);
        mMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==4){
                    SharedPreferences preferences = getSharedPreferences("USER_PREFS", 0);
                    preferences.edit().clear();
                    Intent intent=new Intent(OrdersActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void setup(){
        first= (ImageView) findViewById(R.id.first);
        second= (ImageView) findViewById(R.id.second);
        third= (ImageView) findViewById(R.id.third);
//        target= (TextView) findViewById(R.id.target);
//        count= (TextView) findViewById(R.id.count);
        week= (TextView) findViewById(R.id.week);
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(OrdersActivity.this, R.anim.image_click));
                viewPager.setCurrentItem(0);
            }
        });
        month= (TextView) findViewById(R.id.month);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(OrdersActivity.this, R.anim.image_click));
                viewPager.setCurrentItem(1);
            }
        });
        year= (TextView) findViewById(R.id.year);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(OrdersActivity.this, R.anim.image_click));
                viewPager.setCurrentItem(2);
            }
        });
        pending= (TextView) findViewById(R.id.pending);
        completed= (TextView) findViewById(R.id.completed);
        newOrder= (TextView) findViewById(R.id.newOrder);
        viewPager= (ViewPager) findViewById(R.id.pager);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrdersActivity.this,PendingOrdersActivity.class);
                startActivity(intent);
            }
        });
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrdersActivity.this,DistributorsActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrdersActivity.this,PendingOrdersActivity.class);
                intent.putExtra("type",2);
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
    private void getData(){
        try {
            JSONObject obj=new JSONObject(" {\"results\": {\"weekly_count\": 0, \"weekly_total\": 0,\"monthly_count\": 0, \"monthly_total\": 0,\"yearly_count\": 0, \"yearly_total\": 0, \"pending_count\":0, \"pending_order\": 0, \"completed_order\":0}}");
            JSONObject res=obj.getJSONObject("results");
            weeklyCount=res.getInt("weekly_count");
            weeklyTotal=res.getInt("weekly_total");
            monthlyCount=res.getInt("monthly_count");
            monthlyTotal=res.getInt("monthly_total");
            yearlyCount=res.getInt("yearly_count");
            yearlyTotal=res.getInt("yearly_total");
            pendingCount=res.getInt("pending_count");
            pendingOrder=res.getInt("pending_order");
            completeOrder=res.getInt("completed_order");
            completed.setText(String.valueOf(completeOrder));
            pending.setText(String.valueOf(pendingOrder));
        } catch (JSONException e) {
            e.printStackTrace();
        }
      CustomPagerAdapter pagerAdapter=new CustomPagerAdapter(OrdersActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    first.setImageResource(R.drawable.circle);
                    second.setImageResource(R.drawable.grey_circle);
                    third.setImageResource(R.drawable.grey_circle);
                }else if (position==1){
                    first.setImageResource(R.drawable.grey_circle);
                    second.setImageResource(R.drawable.circle);
                    third.setImageResource(R.drawable.grey_circle);
                }else if (position==2){
                    first.setImageResource(R.drawable.grey_circle);
                    second.setImageResource(R.drawable.grey_circle);
                    third.setImageResource(R.drawable.circle);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            View view=View.inflate(collection.getContext(),R.layout.pager_item,null);
            TextView count=(TextView) view.findViewById(R.id.count);
            TextView target= (TextView) view.findViewById(R.id.target);
            TextView subText= (TextView) view.findViewById(R.id.subtext);
            collection.addView(view,0);
            if (position==0) {
                count.setText(String.valueOf(weeklyCount) + "/");
                target.setText(String.valueOf(weeklyTotal));
                subText.setText("This Week");
            }else if (position==1){
                count.setText(String.valueOf(monthlyCount)+"/");
                target.setText(String.valueOf(monthlyTotal));
                subText.setText("This Month");
            }else if (position==2){
                subText.setText("This Year");
                count.setText(String.valueOf(yearlyCount)+"/");
                target.setText(String.valueOf(yearlyTotal));
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 3;
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
            progressDialog = new ProgressDialog(OrdersActivity.this);
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

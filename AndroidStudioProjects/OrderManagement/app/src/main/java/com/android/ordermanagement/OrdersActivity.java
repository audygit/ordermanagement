package com.android.ordermanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ordermanagement.Models.Order;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ViewPager viewPager;

//    private TextView subText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setup();

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
                Intent intent=new Intent(OrdersActivity.this,CustomerListActivity.class);
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
        getData();
    }
    private void getData(){
        try {
            JSONObject obj=new JSONObject(" {\"results\": {\"weekly_count\": 13, \"weekly_total\": 30,\"monthly_count\": 36, \"monthly_total\": 120,\"yearly_count\": 586, \"yearly_total\": 3000, \"pending_count\":20, \"pending_order\": 23, \"completed_order\":45}}");
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
}

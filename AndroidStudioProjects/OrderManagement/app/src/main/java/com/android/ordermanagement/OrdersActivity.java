package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrdersActivity extends AppCompatActivity {

    private int weeklyCount=13;
    private int weeklyTotal=20;
    private int monthlyCount=37;
    private int monthlyTotal=50;
    private int yearlyCount=37;
    private int yearlyTotal=180;
    private int pendingCount=13;
    private int pendingOrder=13;
    private int completeOrder=20;

    private TextView count;
    private TextView target;
    private TextView week;
    private TextView month;
    private TextView year;

    private TextView pending;
    private TextView completed;
    private TextView newOrder;

    private TextView subText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setup();

    }
    private void setup(){
        target= (TextView) findViewById(R.id.target);
        count= (TextView) findViewById(R.id.count);
        week= (TextView) findViewById(R.id.week);
        month= (TextView) findViewById(R.id.month);
        year= (TextView) findViewById(R.id.year);
        pending= (TextView) findViewById(R.id.pending);
        completed= (TextView) findViewById(R.id.completed);
        newOrder= (TextView) findViewById(R.id.newOrder);
        count.setText(String.valueOf(weeklyCount)+"/");
        target.setText(String.valueOf(weeklyTotal));
        subText= (TextView) findViewById(R.id.subtext);
        subText.setText("This Week");
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText(String.valueOf(weeklyCount)+"/");
                target.setText(String.valueOf(weeklyTotal));
                subText.setText("This Week");
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText(String.valueOf(monthlyCount)+"/");
                target.setText(String.valueOf(monthlyTotal));
                subText.setText("This Month");
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subText.setText("This Year");
                count.setText(String.valueOf(yearlyCount)+"/");
                target.setText(String.valueOf(yearlyTotal));
            }
        });
        pending.setText(String.valueOf(pendingOrder));
        completed.setText(String.valueOf(completeOrder));
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrdersActivity.this,CustomerListActivity.class);
                startActivity(intent);
            }
        });
    }



//    home screen : {“results”: {“weekly_count”: 13, “weekly_total”: 30,“monthly_count”: 13, “monthly_total”: 30,“yearly_count”: 13, “yearly_total”: 30, “pending_count”:20, “pending_order”: 23, “completed_order”:45}}
//}
}

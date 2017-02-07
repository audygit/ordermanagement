package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.ordermanagement.Models.Product;

public class ProductActivity extends AppCompatActivity {

    private Product product;

    private TextView name;
    private TextView type;
    private TextView quantity;
    private TextView bilquan;
    private TextView amount;
    private TextView total;
    private TextView head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product= (Product) getIntent().getSerializableExtra("product");
        name= (TextView) findViewById(R.id.name);
        type= (TextView) findViewById(R.id.type);
        quantity= (TextView) findViewById(R.id.quantity);
        bilquan= (TextView) findViewById(R.id.billed);
        amount= (TextView) findViewById(R.id.amount);
        total= (TextView) findViewById(R.id.total);
        head= (TextView) findViewById(R.id.head);
        head.setText(product.getName());
        name.setText(product.getName());
        type.setText(product.getUnit());
        quantity.setText(String.valueOf(product.getQuantity()));
        bilquan.setText(String.valueOf(product.getBilledQuantity()));
        amount.setText(String.valueOf(product.getAmount()));
        total.setText(String.valueOf(product.getBilledQuantity()*product.getAmount()));
    }
}

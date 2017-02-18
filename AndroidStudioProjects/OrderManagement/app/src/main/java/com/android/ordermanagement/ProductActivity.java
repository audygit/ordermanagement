package com.android.ordermanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.ordermanagement.Models.Product;

public class ProductActivity extends AppCompatActivity {

    private Product product;

    private TextView name;
    private TextView quantity;
    private TextView bilquan;
    private TextView actquan;
    private TextView price;
    private TextView uom;
    private TextView units;
    private TextView packs;
    private TextView weight;
    private TextView amount;
    private TextView total;
    private ImageButton back;
    private TextView head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product= (Product) getIntent().getSerializableExtra("product");
        name= (TextView) findViewById(R.id.name);
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        quantity= (TextView) findViewById(R.id.quantity_fld);
        bilquan= (TextView) findViewById(R.id.billed_fld);
        actquan= (TextView) findViewById(R.id.actual_fld);
        price= (TextView) findViewById(R.id.price);
        uom= (TextView) findViewById(R.id.uom);
        weight= (TextView) findViewById(R.id.weight);
        units= (TextView) findViewById(R.id.units);
        packs= (TextView) findViewById(R.id.packs);
        amount= (TextView) findViewById(R.id.amount);
        total= (TextView) findViewById(R.id.total);
        head= (TextView) findViewById(R.id.head);
        name.setText(product.getName());
        head.setText(getIntent().getExtras().getString("order"));
        quantity.setText(String.valueOf(product.getQuantity()));
        bilquan.setText(String.valueOf(product.getBilledQuantity()));
        actquan.setText(String.valueOf(product.getActualQuantity()));
        price.setText(String.valueOf(product.getPrice()));
        uom.setText(String.valueOf(12));
        units.setText(String.valueOf(product.getQuantityUts()));
        packs.setText(String.valueOf(product.getQuantityPkgs()));
        weight.setText(String.valueOf(product.getWeightInKgs()));
        total.setText(String.valueOf(product.getAmount()));

    }
}

package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.ordermanagement.Models.Product;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {

    private ArrayList<Product> products=new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsListAdapter productsListAdapter;
    private double serviceTax=234;
    private double vat=672;
    private TextView svt;
    private Button confirm;
    private TextView vatView;
    private TextView total;
    private ImageButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        recyclerView= (RecyclerView) findViewById(R.id.products_list);
        svt= (TextView) findViewById(R.id.service_tax);
        add= (ImageButton) findViewById(R.id.add);
        vatView= (TextView) findViewById(R.id.vat);
        confirm= (Button) findViewById(R.id.confirm);
        total= (TextView) findViewById(R.id.total);
        getProducts();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewOrderActivity.this,AddnewProductActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getProducts(){
        products.add(new Product(1,"Desire",20,"cases",245000));
        products.add(new Product(2,"Passion",25,"cases",2145000));
        productsListAdapter=new ProductsListAdapter(NewOrderActivity.this,products);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
        recyclerView.setAdapter(productsListAdapter);
        double totalAmount=0;
        for (Product p:products){
            totalAmount=totalAmount+p.getAmount();
        }
        totalAmount=totalAmount+serviceTax+vat;
        total.setText(String.valueOf(totalAmount));
        svt.setText(String.valueOf(serviceTax));
        vatView.setText(String.valueOf(vat));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewOrderActivity.this,CustomerListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

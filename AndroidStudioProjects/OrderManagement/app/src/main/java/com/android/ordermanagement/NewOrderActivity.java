package com.android.ordermanagement;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ordermanagement.Models.Order;
import com.android.ordermanagement.Models.Product;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {

    private ArrayList<Product> products=new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsListAdapter productsListAdapter;
    private TextView svt;
    private Button confirm;
    private Button edit;
    private TextView vatView;
    private TextView total;
    private ImageButton add;
    private boolean viewOnly;
    private Order order;
    private TextView head;
    private double totalAmount;
    private ImageView back;
    private String orderString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        viewOnly=getIntent().getBooleanExtra("view",false);
        recyclerView= (RecyclerView) findViewById(R.id.products_list);
        svt= (TextView) findViewById(R.id.service_tax);
        add= (ImageButton) findViewById(R.id.add);
        vatView= (TextView) findViewById(R.id.vat);
        confirm= (Button) findViewById(R.id.confirm);
        edit= (Button) findViewById(R.id.edit);
        total= (TextView) findViewById(R.id.total);
        head= (TextView) findViewById(R.id.head);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (viewOnly){
            add.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            order= (Order) getIntent().getSerializableExtra("order");
            if (order==null){
                products=new ArrayList<>();
            }else {
                products=order.getProducts();
            }
            orderString ="Order No A0014"+order.getId();
            total.setText(String.valueOf(order.getTotalAmount()));
            edit.setVisibility(View.GONE);
            svt.setText(String.valueOf(order.getServiceTax()));
            vatView.setText(String.valueOf(order.getVat()));
        }else {
            orderString = "New Order for "+getIntent().getExtras().getString("name");
            Product product= (Product) getIntent().getSerializableExtra("product");
            products.add(product);
            edit.setVisibility(View.VISIBLE);
            double totalPrice = 0;
            double vat = 0;
            double service = 0;
            for(Product temp : products){
                totalPrice += temp.getAmount();
            }
            service = totalPrice*0.05;
            svt.setText(String.format("%.2f", service));
            total.setText(String.format("%.2f", totalPrice+vat+service));
            vatView.setText(String.valueOf(vat));
            vatView.setVisibility(View.GONE);
        }
        head.setText(orderString);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewOrderActivity.this,AddnewProductActivity.class);
                intent.putExtra("flag",true);
                intent.putExtra("name",getIntent().getExtras().getString("name"));
                startActivityForResult(intent,22);
            }
        });
        productsListAdapter=new ProductsListAdapter(NewOrderActivity.this,products,true, orderString);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
        recyclerView.setAdapter(productsListAdapter);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(NewOrderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_popup);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                Button ok = (Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(NewOrderActivity.this,OrdersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsListAdapter.setViewOnly(false);
                productsListAdapter.notifyDataSetChanged();
            }
        });

    }

    public void removeProduct(int position){
        products.remove(position);
        productsListAdapter.setProviders(products);
        productsListAdapter.setViewOnly(true);
        productsListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==22&&resultCode==RESULT_OK){
            Product product= (Product) data.getSerializableExtra("product");
            products.add(product);
            totalAmount=totalAmount+product.getAmount();
            productsListAdapter.setProviders(products);
            productsListAdapter.setViewOnly(true);
            productsListAdapter.notifyDataSetChanged();
        }
    }
    //    private void getProducts(){
//        products.add(new Product(1,"Desire",20,"cases",245000));
//        products.add(new Product(2,"Passion",25,"cases",2145000));
//
//        double totalAmount=0;
//        for (Product p:products){
//            totalAmount=totalAmount+p.getAmount();
//        }
//
//    }
}

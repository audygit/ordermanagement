package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Product;

public class ProductActivity extends AppCompatActivity {

    private Product product;

    private TextView name;
    private EditText quantity;
    private TextView bilquan;
    private TextView actquan;
    private TextView price;
    private TextView uom;
    private TextView units;
    private TextView packs;
    private TextView weight;
//    private TextView amount;
    private TextView total;
    private ImageButton back;
    private TextView head;
    private Button save;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        pos=getIntent().getIntExtra("position",0);
        product= (Product) getIntent().getSerializableExtra("product");
        if (product.getPer()==0){
            product.setPer(1);
        }
        name= (TextView) findViewById(R.id.name);
        save= (Button) findViewById(R.id.add);
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        quantity= (EditText) findViewById(R.id.quantity_fld);
        bilquan= (TextView) findViewById(R.id.billed_fld);
        actquan= (TextView) findViewById(R.id.actual_fld);
        price= (TextView) findViewById(R.id.price);
        uom= (TextView) findViewById(R.id.uom);
        weight= (TextView) findViewById(R.id.weight);
        units= (TextView) findViewById(R.id.units);
        packs= (TextView) findViewById(R.id.packs);
        total= (TextView) findViewById(R.id.amount_fld);
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
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(s.toString())) {
                    int q = Integer.parseInt(s.toString());
                    bilquan.setText(String.valueOf(q * 11));
                    double first = product.getQuantity() / product.getPer();
                    double second = q * first;
                    double j = 0;
                    int i =0;
                    double actual = 0;
                    if (Integer.valueOf(product.getQuantity()) == 1) {
                        actual = second + q * product.getQuantityUts();
                    } else {
                        if (second < 12)
                            j = second;
                        else {
                            i = (int) (second/12);
                            j = second%12;
                        }
                        actual = q*product.getQuantityUts()+i+j/10;
                    }
                    actquan.setText(String.format("%.2f", actual));
                    total.setText(String.valueOf(q * product.getQuantityUts() * product.getPrice()));
                    units.setText(String.valueOf(q * product.getQuantityUts()));
                    packs.setText(String.valueOf(q * product.getQuantityPkgs()));
                    weight.setText(String.format("%.2f", (q * Double.valueOf(product.getWeightInKgs()))));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    product.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    product.setActualQuantity(Double.valueOf(actquan.getText().toString()));
                    product.setWeightInKgs(Double.valueOf(weight.getText().toString()));
                    product.setAmount(Double.valueOf(total.getText().toString()));
                    product.setPrice(product.getPrice());
                    product.setPer(product.getPer());
                    product.setQuantityUts(Integer.valueOf(units.getText().toString()));
                    product.setQuantityPkgs(Double.valueOf(packs.getText().toString()));
                        product.setBilledQuantity(Double.valueOf(bilquan.getText().toString()));
//                        if (flag) {
                            Intent intent = new Intent();
                            intent.putExtra("product", product);
                            intent.putExtra("position", pos);
                            setResult(RESULT_OK, intent);
                            finish();
//
//                        } else {
//                            Intent intent = new Intent(AddnewProductActivity.this, NewOrderActivity.class);
//                            intent.putExtra("product", product);
//                            intent.putExtra("name", temp);
//                            intent.putExtra("count", intent.getExtras().getString("count"));
//                            intent.putExtra("company",company);
//                            intent.putExtra("customer", customer);
//                            startActivity(intent);
//                        }

            }
        });

    }
}

package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ordermanagement.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class AddnewProductActivity extends AppCompatActivity {

    private Button add;
    private TextView pNameFld;
    private EditText quantityFld;
    private EditText billedQuantityFld;
    private TextView amountFld;
    private double amount;
    private TextView head;
    private ImageButton back;
    private boolean flag;
    private Product producto;
    private String temp;
    private EditText price;
    private EditText uom;
    private TextView units;
    private TextView packs;
    private TextView weight;
    private EditText actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_product);
        add = (Button) findViewById(R.id.add);
        pNameFld = (TextView) findViewById(R.id.nameSpinner);
        pNameFld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddnewProductActivity.this, ProductListActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        price = (EditText)findViewById(R.id.price);
        uom = (EditText) findViewById(R.id.uom);
        units = (TextView) findViewById(R.id.units);
        packs = (TextView) findViewById(R.id.packs);
        weight = (TextView) findViewById(R.id.weight);
        quantityFld = (EditText) findViewById(R.id.quantity_fld);
        billedQuantityFld = (EditText) findViewById(R.id.billed_fld);
        actual = (EditText) findViewById(R.id.actual_fld);
        amountFld = (TextView) findViewById(R.id.amount_fld);
        head = (TextView) findViewById(R.id.head);
        temp = getIntent().getExtras().getString("name");
        head.setText("Add Product for " + temp);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        flag = getIntent().getBooleanExtra("flag", false);

        amountFld.setText(String.valueOf(amount));
        quantityFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(pNameFld.getText()) && !TextUtils.isEmpty(s.toString())) {
                    int q = Integer.parseInt(s.toString());
                    billedQuantityFld.setText(String.valueOf(q * 11));
                    actual.setText(String.valueOf(q*12));
                    amountFld.setText(String.valueOf(q * 11 * producto.getPrice()));
                    units.setText(String.valueOf(q * 11));
                    packs.setText(String.valueOf(q * 11 * 12));
                    weight.setText(String.format("%.2f", (q * 11 * 12 * producto.getWeightInKgs())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pNameFld.getText().toString())) {
                    Product product = new Product();
                    product.setName(pNameFld.getText().toString());
                    int actual = 0;
                    int billed = 0;
                    if (!TextUtils.isEmpty(quantityFld.getText().toString()))
                        actual = Integer.parseInt(quantityFld.getText().toString());
                    if (!TextUtils.isEmpty(billedQuantityFld.getText().toString()))
                        billed = Integer.parseInt(billedQuantityFld.getText().toString());
                        if (actual != 0) {
                            product.setQuantity(actual);
                            product.setActualQuantity(actual);
                            product.setWeightInKgs(Double.valueOf(weight.getText().toString()));
                            product.setAmount(Double.valueOf(amountFld.getText().toString()));
                            product.setPrice(product.getPrice());
                            product.setQuantityUts(Integer.valueOf(units.getText().toString()));
                            product.setQuantityPkgs(Integer.valueOf(packs.getText().toString()));
                            if (billed != 0) {
                                product.setBilledQuantity(billed);
                                if (flag) {
                                    Intent intent = new Intent();
                                    intent.putExtra("product", product);
                                    intent.putExtra("name", temp);
                                    setResult(RESULT_OK, intent);
                                    finish();

                                } else {
                                    Intent intent = new Intent(AddnewProductActivity.this, NewOrderActivity.class);
                                    intent.putExtra("product", product);
                                    intent.putExtra("name", temp);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(AddnewProductActivity.this, "Enter billed quantity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddnewProductActivity.this, "Enter quantity", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                    Toast.makeText(AddnewProductActivity.this, "Select a product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                producto = (Product) data.getExtras().getSerializable("product");
                pNameFld.setText(producto.getName());
                price.setText(String.valueOf(producto.getPrice()));
                uom.setText("12");
                quantityFld.setText("1");
            }
        }
    }
}

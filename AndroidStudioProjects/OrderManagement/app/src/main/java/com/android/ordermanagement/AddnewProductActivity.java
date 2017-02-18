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

import com.android.ordermanagement.Models.Customer;
import com.android.ordermanagement.Models.Product;
import com.android.ordermanagement.Models.ProductListItem;

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
    private ProductListItem producto;
    private String temp;
    private EditText price;
    private EditText uom;
    private TextView units;
    private TextView packs;
    private TextView weight;
    private EditText actualText;
    private Customer customer;

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
        actualText = (EditText) findViewById(R.id.actual_fld);
        amountFld = (TextView) findViewById(R.id.amount_fld);
        head = (TextView) findViewById(R.id.head);
        customer = (Customer) getIntent().getExtras().getSerializable("customer");
        temp = customer.getName();
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
                    double first = producto.getFreeQty() / producto.getPer();
                    double second = q * first;
                    double j = 0;
                    int i =0;
                    double actual = 0;
                    if (Integer.valueOf(producto.getFixedQty()) == 1) {
                        actual = second + q * producto.getUnits();
                    } else {
                        if (second < 12)
                            j = second;
                        else {
                            i = (int) (second/12);
                            j = second%12;
                        }
                        actual = q*producto.getUnits()+i+j/10;
                    }
                    actualText.setText(String.format("%.2f", actual));
                    amountFld.setText(String.valueOf(q * producto.getUnits() * producto.getPrice()));
                    units.setText(String.valueOf(q * producto.getUnits()));
                    packs.setText(String.valueOf(q * producto.getPacks()));
                    weight.setText(String.format("%.2f", (q * producto.getUnits()* Double.valueOf(producto.getWeight()))));
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
                    product.setId(producto.getCode());
                    product.setName(producto.getName());
                    product.setName(pNameFld.getText().toString());
                    int actual = 0;
                    double billed = 0;
                    if (!TextUtils.isEmpty(quantityFld.getText().toString()))
                        actual = Integer.parseInt(quantityFld.getText().toString());
                    if (!TextUtils.isEmpty(billedQuantityFld.getText().toString()))
                        billed = Double.valueOf(billedQuantityFld.getText().toString());
                        if (actual != 0) {
                            product.setQuantity(actual);
                            product.setActualQuantity(Double.valueOf(actualText.getText().toString()));
                            product.setWeightInKgs(Double.valueOf(weight.getText().toString()));
                            product.setAmount(Double.valueOf(amountFld.getText().toString()));
                            product.setPrice(producto.getPrice());
                            product.setPer(producto.getPer());
                            product.setQuantityUts(Integer.valueOf(units.getText().toString()));
                            product.setQuantityPkgs(Double.valueOf(packs.getText().toString()));
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
                                    intent.putExtra("customer", customer);
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
                producto = (ProductListItem) data.getExtras().getSerializable("product");
                pNameFld.setText(producto.getName());
                price.setText(String.valueOf(producto.getPrice()));
                uom.setText(producto.getUom());
                quantityFld.setText("");
            }
        }
    }
}


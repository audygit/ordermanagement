package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddnewProductActivity extends AppCompatActivity {

    private Button add;
    private EditText pNameFld;
    private EditText packageTypeFld;
    private EditText quantityFld;
    private EditText billedQuantityFld;
    private EditText amountFld;
    private TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_product);
        add= (Button) findViewById(R.id.add);
        pNameFld= (EditText) findViewById(R.id.name_fld);
        packageTypeFld= (EditText) findViewById(R.id.type_fld);
        quantityFld= (EditText) findViewById(R.id.quantity_fld);
        billedQuantityFld= (EditText) findViewById(R.id.billed_fld);
        amountFld= (EditText) findViewById(R.id.amount_fld);
        totalAmount= (TextView) findViewById(R.id.total);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddnewProductActivity.this,NewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.android.ordermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddnewProductActivity extends AppCompatActivity {

    private Button add;
    private EditText pNameFld;
    private EditText packageTypeFld;
    private EditText quantityFld;
    private EditText billedQuantityFld;
    private EditText amountFld;
    private EditText totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_product);
        add= (Button) findViewById(R.id.add);
        pNameFld= (EditText) findViewById(R.id.name_fld);

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

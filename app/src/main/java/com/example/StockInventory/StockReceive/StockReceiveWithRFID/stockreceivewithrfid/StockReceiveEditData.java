package com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.Util.Connection.Connection.ConnectionClass;
import com.example.Util.Connection.Connection.DatabaseName;
import com.example.StockInventory.R;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.example.Util.Connection.Scanner.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StockReceiveEditData extends AppCompatActivity {

    protected final String DocType = "SR1";
    private EditText edittext;
    Connection con;
    private ImageButton backbutton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock_receive_edit_data);
        TextView text = findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        String value1 =extras.getString("Value1");
        text.setText(value1);

        backbutton = findViewById(R.id.backbutton);
        Button Savebutton = findViewById(R.id.Save);
        edittext= findViewById(R.id.editTextTextPersonName);
        String result =extras.getString("Result");
        TextView inputtext = findViewById(R.id.editTextTextPersonName);
        builder = new AlertDialog.Builder(this);
        inputtext.setText(result);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StockReceive.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Button buttonqrcode = findViewById(R.id.buttonqrcode);
        buttonqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(getApplicationContext(), Scanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("DocType", DocType);
                i.putExtra("ID",value1 );
                startActivity(i);
            }
        });


        Savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().isEmpty() || edittext.getText().toString().isEmpty()){
                    Toast.makeText(StockReceiveEditData.this, "Data cannot be empty", Toast.LENGTH_LONG).show();

                }else if(!text.getText().toString().isEmpty() && !edittext.getText().toString().isEmpty()) {
                    String ItemCode = InventoryHelper.ItemCodeValidation(edittext.getText().toString());
                    if (ItemCode != null) {

                        InventorySaveData.savedata(text.getText().toString(), inputtext.getText().toString(), v, DocType);
                        addListenerOnButton();

                    } else {
                        edittext.requestFocus();
                        edittext.setError("Invalid ItemCode");
                    }
                }
            }
        });
    }


    public void addListenerOnButton() {
        final TextView text = findViewById(R.id.textView4);
        edittext = (EditText) findViewById(R.id.editTextTextPersonName);
        Button buttonSave = (Button) findViewById(R.id.Save);


        String value1=text.getText().toString();
        String value2 = edittext.getText().toString();
        final String data = value1;
        if (!value2.equals("")) {
            value1 = data.split("/")[0] + "/" + value2;
        }
        else {
            value1 = data.split("/")[0];
        }
        setData(data,value1);


        final String finalValue = value1;

        builder.setTitle("Stock Receive");
        builder.setMessage("Your data has been recorded");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(StockReceiveEditData.this, "Stock Receive Done", Toast.LENGTH_LONG).show();
                Intent i =new Intent(getApplicationContext(),StockReceive.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private void setData(String data,String data1) {
        data = data.split("/")[0];
        sharedPreferences = getSharedPreferences(data, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(data,data1);
        editor.commit();
    }
}
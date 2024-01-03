package com.example.StockInventory.StockAdjustment.withrfid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.StockInventory.R;
import com.example.StockInventory.StockAdjustment.page5;
import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.Util.Connection.FileHelper;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.example.Util.Connection.Scanner.Scanner;

import java.io.File;
import java.io.IOException;

public class editAdj extends AppCompatActivity {

    protected final String DocType = "SA1";
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adj_without_r_f_i_d);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView output = findViewById(R.id.SerialNo);
        EditText itemcode = findViewById(R.id.itemcode);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("Value1");
        output.setText(value);

        String output1 = extras.getString("ID");
        String value1 = extras.getString("Itemcode");
        if(output1 != null && value1 != null){
            output.setText(output1);
            itemcode.setText(value1);
        }


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Adjwithrfid.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Button buttonqrcode = findViewById(R.id.adjbuttonqrcode);
        buttonqrcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Scanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("DocType", DocType);
                i.putExtra("ID",output.getText().toString());
                startActivity(i);
            }
        });


        Button Savebutton = findViewById(R.id.Save);
        Savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(output.getText().toString().isEmpty() || itemcode.getText().toString().isEmpty()){
                    Toast.makeText(editAdj.this, "Data cannot be empty", Toast.LENGTH_LONG).show();

                }else if(!output.getText().toString().isEmpty() && !itemcode.getText().toString().isEmpty()) {

                    String ItemCode = InventoryHelper.ItemCodeValidation(itemcode.getText().toString());
                    if(ItemCode != null){
                        builder.setTitle("Stock Adjustment");
                        builder.setIcon(R.mipmap.ic_launcher_1);
                        builder.setMessage("Confirm to submit? \n\nRFID No: " + output.getText().toString() + "\n"
                                + "ItemCode: " + itemcode.getText().toString());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                InventorySaveData.savedata(output.getText().toString(), itemcode.getText().toString(), v, DocType);
                                addListenerOnButton();
                                Toast.makeText(editAdj.this, "Data be recorded", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), Adjwithrfid.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }else {
                        itemcode.requestFocus();
                        itemcode.setError("Invalid ItemCode");
                    }
                }
            }
        });
    }

    public void addListenerOnButton() {

        final TextView output = findViewById(R.id.SerialNo);
        final EditText itemcode = findViewById(R.id.itemcode);

        String value1= output.getText().toString();
        String value2 = itemcode.getText().toString();
        final String data = value1;

        value1 = data.split("/")[0] + "/" + value2;

        setData(data,value1);
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
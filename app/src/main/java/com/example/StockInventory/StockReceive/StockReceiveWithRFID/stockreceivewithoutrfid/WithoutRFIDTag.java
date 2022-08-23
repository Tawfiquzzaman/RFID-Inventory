package com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.NavigationDrawer.SetSerialNo;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.page1;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.StockReceiveEditData;
import com.example.Util.Connection.Connection.ConnectionClass;
import com.example.Util.Connection.Connection.DatabaseName;
import com.example.StockInventory.R;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.StockInventory.StockIssue.StockIssueSQLcmd;
import com.example.Util.Connection.FileHelper;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.example.Util.Connection.PrinterHelper.PrinterHelper;
import com.example.Util.Connection.Scanner.Scanner;

public class WithoutRFIDTag extends AppCompatActivity {

    protected final String DocType = "SR2";
    private ImageButton backbutton;
    Connection con;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_without_r_f_i_d_tag);

        TextView output = findViewById(R.id.SerialNo);
        backbutton = findViewById(R.id.backbutton);
        Button Savebutton = findViewById(R.id.Save);
        EditText itemcode = findViewById(R.id.itemcode);
        Button qrscanner = findViewById(R.id.buttonqrcode);
        EditText input = findViewById(R.id.itemcode);
        builder = new AlertDialog.Builder(this);

        File fileEvents = new File(WithoutRFIDTag.this.getFilesDir() + "/SerialNo/SerialNo");
        output.setText(FileHelper.readFile(fileEvents));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Itemcode");
            itemcode.setText(value);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), page1.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        qrscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Scanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("DocType", DocType);
                startActivity(i);
            }
        });

        Savebutton.setOnClickListener(new View.OnClickListener() {

            String db = DatabaseName.database_name;

            @Override
            public void onClick(View v) {
                if (output.getText().toString().isEmpty()) {

                    builder.setTitle("Error");
                    builder.setMessage("Please set your Serial No in 'Serial No Setting");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), SetSerialNo.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else if (itemcode.getText().toString().isEmpty()) {
                    Toast.makeText(WithoutRFIDTag.this, "Data cannot be empty", Toast.LENGTH_LONG).show();

                } else if (!output.getText().toString().isEmpty() && !itemcode.getText().toString().isEmpty()) {
                    String ItemCode = InventoryHelper.ItemCodeValidation(itemcode.getText().toString());
                    if (ItemCode != null) {

                        InventorySaveData.savedata(output.getText().toString(), itemcode.getText().toString(), v, DocType);
                        File file = new File(WithoutRFIDTag.this.getFilesDir(), "SerialNo");
                        String value = output.getText().toString();
                        try {
                            FileHelper.writeFile(file, value);
                            PrintHelper printHelper = new PrintHelper(getApplicationContext());
                            PrinterHelper.doPrint(printHelper);

                            builder.setTitle("Stock Receive");
                            builder.setMessage("Your data has been recorded");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(WithoutRFIDTag.this, "Stock Receive Done", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } catch (Exception e) {}
                    } else {
                        itemcode.requestFocus();
                        itemcode.setError("Invalid ItemCode");
                    }

                }
            }
        });
    }
}
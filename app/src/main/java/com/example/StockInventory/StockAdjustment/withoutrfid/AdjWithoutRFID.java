package com.example.StockInventory.StockAdjustment.withoutrfid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.StockInventory.R;
import com.example.StockInventory.StockAdjustment.page5;
import com.example.Util.Connection.FileHelper;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.example.Util.Connection.Scanner.Scanner;

import java.io.File;
import java.io.IOException;



public class AdjWithoutRFID extends AppCompatActivity {

    protected final String DocType = "SA2";
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

        File fileEvents = new File(AdjWithoutRFID.this.getFilesDir() + "/SerialNo/SerialNo");
        output.setText(FileHelper.readFile(fileEvents));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Itemcode");
            itemcode.setText(value);
        }


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), page5.class);
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
                    Toast.makeText(AdjWithoutRFID.this, "Data cannot be empty", Toast.LENGTH_LONG).show();

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
                                try {
                                    File file = new File(AdjWithoutRFID.this.getFilesDir(), "SerialNo");
                                    String value = output.getText().toString();
                                    FileHelper.writeFile(file, value);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(AdjWithoutRFID.this, "Data be recorded", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), page5.class);
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
}
package com.example.StockInventory.StockIssue.WithoutRFID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.StockInventory.R;
import com.example.StockInventory.StockIssue.SIsavedata;
import com.example.StockInventory.StockIssue.page3;
import com.example.Util.Connection.Scanner.Scanner;

public class stockissuewithoutrfid extends AppCompatActivity {

    private ImageButton backbutton;
    protected final String DocType = "SI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_stockissuewithoutrfid);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView output = findViewById(R.id.SerialNo);
        TextView itemcode = findViewById(R.id.itemcode);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ID");
            final String[] items = value.split("/");
            output.setText(items[0]);
            itemcode.setText(items[1]);
        }

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), page3.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Button qrscanner = findViewById(R.id.buttonqrcode);
        qrscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Scanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("DocType", DocType);
                startActivity(i);
            }
        });

        Button Savebutton = findViewById(R.id.SaveSIwithoutrfid);
        Savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(output.getText().toString().isEmpty() || itemcode.getText().toString().isEmpty()){
                    Toast.makeText(stockissuewithoutrfid.this, "Data cannot be empty", Toast.LENGTH_LONG).show();
                }else if(!output.getText().toString().isEmpty() && !itemcode.getText().toString().isEmpty()) {
                    builder.setTitle("Stock Issue");
                    builder.setIcon(R.mipmap.ic_launcher_1);
                    builder.setMessage("Confirm to submit? \n\nRFID No: " + output.getText().toString() + "\n"
                            + "ItemCode: " + itemcode.getText().toString());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            boolean value = SIsavedata.savedata(output.getText().toString(), itemcode.getText().toString(), v);
                            if (value == true){
                                Toast.makeText(stockissuewithoutrfid.this, "Data be recorded", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), page3.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);

                            }else{
                                Toast.makeText(stockissuewithoutrfid.this, "SQL Connection Fail", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), page3.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);

                            }
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
                }
            }
        });
    }
}
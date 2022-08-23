package com.example.StockInventory.StockIssue.WithRFID;


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
import com.example.Util.Connection.Scanner.Scanner;

import java.sql.Connection;


public class StockIssueEditData<countDigit> extends AppCompatActivity {

    private static boolean value;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock_issue_edit_data);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        Bundle extras = getIntent().getExtras();
        String data = extras.getString("Data");
        final String[] items = data.split("/");


        TextView id = findViewById(R.id.textView4);
        TextView itemcode = findViewById(R.id.itemcode);

        id.setText(items[0]);
        itemcode.setText(items[1]);

        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), stockissue.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });


        Button savebuttonStockIssue = findViewById(R.id.SaveStockIssue);
        savebuttonStockIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Stock Issue");
                builder.setIcon(R.mipmap.ic_launcher_1);
                builder.setMessage("Confirm to submit? \n\nRFID No: " + items[0] + "\n"
                        + "ItemCode: " + items[1]);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SIsavedata.savedata(id.getText().toString(), itemcode.getText().toString(), v);
                        Toast.makeText(StockIssueEditData.this, "Data be recorded", Toast.LENGTH_LONG).show();
                        finish();
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
        });
    }
}
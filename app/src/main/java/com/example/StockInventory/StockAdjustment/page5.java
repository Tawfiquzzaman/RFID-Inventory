package com.example.StockInventory.StockAdjustment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;
import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.StockInventory.StockAdjustment.withrfid.Adjwithrfid;
import com.example.StockInventory.StockIssue.WithRFID.stockissue;
import com.example.StockInventory.StockIssue.WithoutRFID.stockissuewithoutrfid;

public class page5 extends AppCompatActivity {

    private ImageButton backbutton;
    private Object CardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_page5);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnwithrfidtag);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Adjwithrfid.class);
                startActivity(i);
            }
        });


        contributeCard: CardView = findViewById(R.id.btnwithoutrfidtag);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), AdjWithoutRFID.class);
                startActivity(i);
            }
        });
    }
}
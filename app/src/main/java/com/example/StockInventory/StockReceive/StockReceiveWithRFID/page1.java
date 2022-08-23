package com.example.StockInventory.StockReceive.StockReceiveWithRFID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.StockReceive;

public class page1 extends AppCompatActivity {

    private ImageButton backbutton;
    private Object CardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_page1);

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
                Intent i =new Intent(getApplicationContext(), StockReceive.class);
                startActivity(i);
            }
        });


        contributeCard: CardView = findViewById(R.id.btnwithoutrfidtag);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), WithoutRFIDTag.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }
}
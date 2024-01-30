package com.example.StockInventory;

import static android.widget.Toast.LENGTH_SHORT;

import static com.ctaiot.ctprinter.ctpl.CTPL.Port.*;
import com.ctaiot.ctprinter.ctpl.CTPL;
import com.example.lc_print_sdk.PrintConfig;
import com.example.lc_print_sdk.PrintUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.NavigationDrawer.AboutUs;
import com.example.StockInventory.CodeGenerator.generatecode;
import com.example.StockInventory.ListItemCode.itemsetting;
import com.example.NavigationDrawer.SetSerialNo;
import com.example.StockInventory.StockAdjustment.page5;
import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.StockInventory.StockIssue.page3;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.page1;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.listAdapter;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.example.Util.Connection.Scanner.Scanner;
import com.google.android.material.navigation.NavigationView;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.StockReceive;

import java.io.IOException;


public class MainActivity extends AppCompatActivity{

    public static Context instance;
    private DrawerLayout mDrawer;
    private Object CardView;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    AlertDialog.Builder builder;



    protected final String DocType = "SI";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //PrintUtil printUtil = PrintUtil.getInstance(MainActivity.this);

        //PrintUtil.setPrintEventListener((PrintUtil.PrinterBinderListener) this);// Set listening

        //int return_distance = 0;
        //PrintUtil.setUnwindPaperLen(return_distance);// Set paper return distance
        //PrintUtil.printEnableMark(true);// Open Black Label
        //PrintUtil.printConcentration(25);// Set concentration
//
        //// Step 4: print text
        //PrintUtil.printText("Print text 1");
        //PrintUtil.printText("Print text 2");
        //PrintUtil.printText("Print text 3");
        // Step 5: paper feeding
        //printUtil.printGoToNextMark();//Paper feeding

        String DocType = "SR3";
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {  // Assuming keyCode 24 is for VOLUME_UP
            Intent i = new Intent(getApplicationContext(), Scanner.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.putExtra("DocType", DocType);
            startActivity(i);
            return true;  // consume event
        }
        return super.onKeyDown(keyCode, event);
    }

    //private boolean isScanning = false; // A flag to keep track if the scanner is currently scanning
//
    //@Override
    //public boolean onKeyUp(int keyCode, KeyEvent event) {
    //    Log.d("KeyTest", "Key code: " + keyCode);
    //    Toast.makeText(MainActivity.this, "Scanned Keycode is " + keyCode, Toast.LENGTH_SHORT).show();
//
    //    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
    //        isScanning = true;
    //        Intent i = new Intent(getApplicationContext(), Scanner.class);
    //        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
    //        i.putExtra("DocType", DocType);
    //        startActivity(i);
    //    } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && isScanning) {
    //        isScanning = false;
//
    //        // Assuming your scanner activity returns the result as an item code string
    //        String scannedItemCode = "HDPE"; // TODO: Retrieve this from the scanner result
    //        InventorySaveData.savedata("123", scannedItemCode,"SR2");
    //    }
    //    String scannedItemCode = "HDPE"; // TODO: Retrieve this from the scanner result
    //    InventorySaveData.savedata("123", scannedItemCode,"SR2");
//
    //    return super.onKeyUp(keyCode, event);
    //}



    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    Toast.makeText(MainActivity.this, "Keycode is "+keyCode, Toast.LENGTH_LONG).show();
    //    //Log.d("KeyTest", "Key code: " + keyCode);
    //    // ... rest of your code ...
    //    return super.onKeyDown(keyCode, event);
    //}

    //@Override
    //public boolean onKeyUp (int keyCode, KeyEvent event) {
    //    //Keycode 24 - volume
    //    //Keycode 4 - F1 ->no correct?
    //    //Keycode 19 - Up
    //    //keycode 20 - down
    //    //keycode 21 - right
    //    //keycode 22 - left
    //    Log.d("KeyTest", "Key code: " + keyCode);
    //    Toast.makeText(MainActivity.this, "Keycode is "+keyCode, Toast.LENGTH_SHORT).show();
    //    // You can also check for specific key events like this
    //    if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
    //        //Toast.makeText(MainActivity.this, "Data cannot be empty"+keyCode, Toast.LENGTH_LONG).show();
    //        Intent i = new Intent(getApplicationContext(), Scanner.class);
    //        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
    //        i.putExtra("DocType", DocType);
    //        startActivity(i);
    //    }
//
    //    return super.onKeyUp(keyCode, event);
    //}






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        

        //Navigation Drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);


        contributeCard: CardView = findViewById(R.id.btnpage1);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), page1.class);
                Intent i = new Intent(getApplicationContext(), WithoutRFIDTag.class);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage2);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), generatecode.class); //PRINT LABEL -> after here, will go res ->
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage3);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), page3.class);
                startActivity(i);

//                builder.setTitle("Coming Soon");
//                builder.setMessage("The Stock Issue still under development.");
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage4);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(getApplicationContext(), itemsetting.class);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage5);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), page5.class);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage6);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), MainActivity_2.class);
                startActivity(i);
            }
        });

         //Connect MainActivity_2.java
        //setContentView(R.layout.activity_main);
        //Button move = findViewById(R.id.moveActivity);
        //move.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(getApplicationContext(),"Tawfiq", Toast.LENGTH_LONG).show();
        //        //Intent intent = new Intent(MainActivity.this, MainActivity_2.class);
        //        //startActivity(intent);
        //    }
        //});
    }




    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent i =new Intent(getApplicationContext(), SetSerialNo.class);
                startActivity(i);
                break;
            case R.id.aboutus:
                Intent i2 =new Intent(getApplicationContext(), AboutUs.class);
                startActivity(i2);
                break;

            case R.id.moreinformation:
                String url = "https://www.prisma-tech4u.com/pages/pages_id/31465/";
                Intent i3 =new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i3);
                break;
        }
        mDrawer.closeDrawers();
    }
}












































































































































































































































































































































































































































































































































































































































































































































































































































































































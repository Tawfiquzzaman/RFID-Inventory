package com.example.storereceivetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.NavigationDrawer.AboutUs;
import com.example.storereceivetest.CodeGenerator.CodeGenerator;
import com.example.storereceivetest.CodeGenerator.generatecode;
import com.example.storereceivetest.itemsetting.itemsetting;
import com.example.NavigationDrawer.SetSerialNo;
import com.example.storereceivetest.stockreceive.page1;
import com.google.android.material.navigation.NavigationView;



public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawer;
    private Object CardView;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(this);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        contributeCard: CardView = findViewById(R.id.btnpage1);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), page1.class);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage2);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), generatecode.class);
                startActivity(i);
            }
        });

        contributeCard: CardView = findViewById(R.id.btnpage3);
        ((View) CardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Coming Soon");
                builder.setMessage("The Stock Issue still under development.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                String url = "https://www.prisma-tech4u.com/pages/about";
                Intent i3 =new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i3);
                break;
        }
        mDrawer.closeDrawers();
    }
}












































































































































































































































































































































































































































































































































































































































































































































































































































































































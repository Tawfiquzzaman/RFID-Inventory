package com.example.storereceivetest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StockReceiveEditData extends AppCompatActivity {

    TextView text;
    private EditText edittext;
    private Object value2;
    Spinner spinner;
    Connection con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_receive_edit_data);
        TextView text = findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        String value1 =extras.getString("Value1");
        text.setText(value1);

        edittext= findViewById(R.id.editTextTextPersonName);
        String result =extras.getString("Result");
        TextView inputtext = findViewById(R.id.editTextTextPersonName);
        inputtext.setText(result);

        spinner = (Spinner)findViewById(R.id.spinner);
        FillSpinner();

    }

    public void addListenerOnButton() {
//        Log.e("Save", "Function call" );
        final TextView text = findViewById(R.id.textView4);
        edittext = (EditText) findViewById(R.id.editTextTextPersonName);
        Button buttonSave = (Button) findViewById(R.id.Save);

//        Log.e("Save", "Function run" );
        String value1=text.getText().toString();
        String value2 = edittext.getText().toString();
        final String data = value1;
        if (!value2.equals("")) {
            value1 = data.split("/")[0] + "/" + value2;
//            Log.e("Save", value1);
        }
        else {
            value1 = data.split("/")[0];
//            Log.e("Save", value1);
        }
        setData(data,value1);
//        Log.e("Save", value1);

        final String finalValue = value1;

        //Call OkHttp get Request Function
        new Thread(new Runnable() {
            @Override
            public void run() {
                doGetRequest(finalValue);
            }
        }).start();

        Intent i =new Intent(getApplicationContext(),StockReceive.class);
        startActivity(i);
    }

    //Call Back MainActivity
    public void callFirstActivity(View view){
        Intent i =new Intent(getApplicationContext(),StockReceive.class);
        startActivity(i);
    }

    //Button Function
    public void onClick(View view){
        switch(view.getId()){
            case(R.id.buttonqrcode):
                //Call Scanner.java
                Bundle extras = getIntent().getExtras();
                String value1 =extras.getString("Value1");
                Intent i =new Intent(getApplicationContext(),Scanner.class);
                i.putExtra("Id",value1 );
                Log.d("qrcode", value1);
                startActivity(i);
                break;

            case(R.id.Save):
                addListenerOnButton();
                break;

//           case(R.id.buttonbarcode):
//               startActivity(new Intent(MainActivity2.this, ScannedBarcodeActivity.class));

        }
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

    //OkHttp get Request
    private void doGetRequest(String finalValue){
//        Log.d("OKHTTP","i ="+finalValue);
//        Log.d("OKHTTP", "Function called");
        value2 = edittext.getText().toString();
//        Log.d("OKHTTP", "itemcode =" + value2);
        String url = "http://192.168.0.165/iot_project/testsubmission.php?itemcode='"+ value2 +"'&RFID_id='" + finalValue + "'";
//        Log.d("OKHTTP", url);
        OkHttpClient client = new OkHttpClient();
//        Log.d("OKHTTP", "Client created");
        Request request = new Request.Builder()
                .url(url)
                .build();
//        Log.d("OKHTTP", "Request Build succesful.");
        try {
            Response response = client.newCall(request).execute();
//            Log.d("OKHTTP", "Got the response");
        } catch (IOException e) {
//            Log.d("OKHTTP", "Exception while doing request");
            e.printStackTrace();
        }
    }

    public void FillSpinner()
    {
        try{
            con = connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if(con == null){
                Log.e("SQL", "Connection Fail");
            }
            else{
                String query = "SELECT [ItemCode] FROM [dbo].[Item];";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                ArrayList<String> data = new ArrayList<String>();
                while(rs.next()){
                    String name = rs.getString("ItemCode");
                    data.add(name);
                }

                ArrayAdapter array = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data);
                spinner.setAdapter(array);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://"+ server +"/"+database+";user="+ user +";password="+ password+";";
            Log.e("SQL", connectionURL);
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error :",e.getMessage());
        }
        return connection;
    }

}
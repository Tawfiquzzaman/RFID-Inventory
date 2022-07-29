package com.example.storereceivetest.stockreceive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.storereceivetest.Connection.ConnectionClass;
import com.example.storereceivetest.MainActivity;
import com.example.storereceivetest.R;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class WithoutRFIDTag extends AppCompatActivity {

    private ImageButton backbutton;
    Connection con;

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

        output.setText(readFile());

        String sessionId = getIntent().getStringExtra("Itemcode");
        if(sessionId != null){
            itemcode.setText(sessionId);
        }


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), page1.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        qrscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), withoutrfidScanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Savebutton.setOnClickListener(new View.OnClickListener(){
            String value = output.getText().toString();
            String value1 = itemcode.getText().toString();

            @Override
            public void onClick(View v) {
                try{
                    con = connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
                    if(con == null){
                        Log.e("SQL", "Connection Fail");
                    }
                    else {
                        int DocKey;
                        int DtlKey;
                        int RegValue1;
                        String queryRegValue = "SELECT RegValue FROM Registry WHERE  (RegID = 32768)";
                        PreparedStatement stmtRegValue = con.prepareStatement(queryRegValue);
                        ResultSet rsRegValue = stmtRegValue.executeQuery();

                        String RegValue = null;
                        while (rsRegValue.next()) {
                            RegValue = rsRegValue.getString("RegValue");
                            Log.e("SQL", RegValue);
                        }

                        DocKey = Integer.parseInt(RegValue) + 1;
                        DtlKey = Integer.parseInt(RegValue) +2;
                        RegValue1 = DtlKey +1;
                        Log.e("SQL", DocKey +" "+ DtlKey+" "+RegValue1);

//                        String queryRegValue1 = "UPDATE Registry SET RegValue = "+ RegValue1 +"WHERE (RegID = 32768);";
//                        PreparedStatement stmtRegValue1 = con.prepareStatement(queryRegValue1);
//                        ResultSet rsRegValue1 = stmtRegValue1.executeQuery();

                        String queryitemcode = "SELECT itemcode from [dbo].[item] WHERE itemcode ='"+itemcode.getText().toString()+"';";
                        PreparedStatement stmtitemcode = con.prepareStatement(queryitemcode);
                        ResultSet rsitemcode = stmtitemcode.executeQuery();

                        String Itemcode = null;
                        while (rsitemcode.next()) {
                            Itemcode = rsitemcode.getString("itemcode");
                            Log.e("SQL", Itemcode);
                        }


                        String queryDocNoFormat = "SELECT NextNumber FROM [dbo].[DocNoFormat] WHERE DocType = 'SR';";
                        PreparedStatement stmtDocNoFormat = con.prepareStatement(queryDocNoFormat);
                        ResultSet rsDocNoFormat = stmtDocNoFormat.executeQuery();
                        String DocNoFormat = null;
                        String DocNo = null;
                        while (rsDocNoFormat.next()) {
                            DocNoFormat = rsDocNoFormat.getString("NextNumber");
                            Log.e("SQL", DocNoFormat);
                            DecimalFormat dfor = new DecimalFormat("000000");
                            dfor.toString().format(DocNoFormat);
                            Log.e("SQL", "SR-"+dfor.format(DocNoFormat));
                            DocNo = "SR-"+dfor.toString().format(DocNoFormat);

                        }



                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    private String readFile() {
        File fileEvents = new File(WithoutRFIDTag.this.getFilesDir()+"/SerialNo/SerialNo");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        return result;
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
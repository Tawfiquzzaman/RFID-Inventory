package com.example.storereceivetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storereceivetest.Connection.ConnectionClass;
import com.example.storereceivetest.stockreceive.WithoutRFIDTag;
import com.example.storereceivetest.stockreceive.page1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StockReceiveEditData extends AppCompatActivity {

    private EditText edittext;
    private Object value2;
    Spinner spinner;
    Connection con;
    private ImageButton backbutton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_stock_receive_edit_data);
        TextView text = findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        String value1 =extras.getString("Value1");
        text.setText(value1);

        backbutton = findViewById(R.id.backbutton);
        Button Savebutton = findViewById(R.id.Save);
        edittext= findViewById(R.id.editTextTextPersonName);
        String result =extras.getString("Result");
        TextView inputtext = findViewById(R.id.editTextTextPersonName);
        builder = new AlertDialog.Builder(this);
        inputtext.setText(result);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StockReceive.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Savebutton.setOnClickListener(new View.OnClickListener() {

            String db = "AED_aw";

            @Override
            public void onClick(View v) {
                try {
                    con = connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
                    if (con == null) {
                        Log.e("SQL", "Connection Fail");
                    } else {
                        String queryitemcode = "SELECT itemcode from [dbo].[item] WHERE itemcode ='" + edittext.getText().toString() + "';";
                        PreparedStatement stmtitemcode = con.prepareStatement(queryitemcode);
                        ResultSet rsitemcode = stmtitemcode.executeQuery();

                        String Itemcode = null;
                        while (rsitemcode.next()) {
                            Itemcode = rsitemcode.getString("itemcode");
                            Log.e("SQL", Itemcode);
                        }

                        if (Itemcode != null) {

                            int DocKey;
                            int DtlKey;
                            int RegValue1;


                            //Get Dockey & DtlKey
                            String queryRegValue = "SELECT RegValue FROM Registry WHERE  (RegID = 32768)";
                            PreparedStatement stmtRegValue = con.prepareStatement(queryRegValue);
                            ResultSet rsRegValue = stmtRegValue.executeQuery();

                            String RegValue = null;
                            while (rsRegValue.next()) {
                                RegValue = rsRegValue.getString("RegValue");
                                Log.e("SQL", RegValue);
                            }
                            DocKey = Integer.parseInt(RegValue) + 1;
                            DtlKey = Integer.parseInt(RegValue) + 2;
                            RegValue1 = Integer.parseInt(RegValue) + 3;

                            String queryRegValue1 = "UPDATE Registry SET RegValue = " + RegValue1 + "WHERE (RegID = 32768);";
                            PreparedStatement stmtRegValue1 = con.prepareStatement(queryRegValue1);
                            stmtRegValue1.executeUpdate();


                            //Get StockDTLKey
                            String querySELECT32772 = "SELECT RegValue FROM Registry WHERE  (RegID = 32772)";
                            PreparedStatement stmtSELECT32772 = con.prepareStatement(querySELECT32772);
                            ResultSet rsSELECT32772 = stmtSELECT32772.executeQuery();

                            String StockDTLKey = null;
                            while (rsSELECT32772.next()) {
                                StockDTLKey = rsSELECT32772.getString("RegValue");
                                Log.e("SQL", StockDTLKey);
                            }

                            int nextStockDTLKey;
                            nextStockDTLKey = Integer.parseInt(StockDTLKey) + 1;

                            String queryUpdate32772 = "UPDATE Registry SET RegValue = " + nextStockDTLKey + "WHERE (RegID = 32772);";
                            PreparedStatement stmtSUpdate32772 = con.prepareStatement(queryUpdate32772);
                            stmtSUpdate32772.executeUpdate();



                            //Get DocNo
                            String queryDocNoFormat = "SELECT NextNumber FROM [dbo].[DocNoFormat] WHERE DocType = 'SR';";
                            PreparedStatement stmtDocNoFormat = con.prepareStatement(queryDocNoFormat);
                            ResultSet rsDocNoFormat = stmtDocNoFormat.executeQuery();
                            String NextNumber = null;
                            String DocNo = null;
                            while (rsDocNoFormat.next()) {
                                NextNumber = rsDocNoFormat.getString("NextNumber");
                            }
                            int no = Integer.parseInt(NextNumber) + 1;

                            String queryUpdateDocNoFormat = "UPDATE DocNoFormat SET NextNumber = '" + no + "' WHERE DocType = 'SR';";
                            PreparedStatement stmtUpdateDocNoFormat = con.prepareStatement(queryUpdateDocNoFormat);
                            stmtUpdateDocNoFormat.executeUpdate();

                            //Count the DocNo digit
                            int count = 0;
                            int n = Integer.parseInt(NextNumber);
                            while (n != 0) {
                                n = n / 10;
                                ++count;
                            }
                            if (count == 1) {
                                DocNo = "SR-00000" + NextNumber;
                            } else if (count == 2) {
                                DocNo = "SR-0000" + NextNumber;
                            } else if (count == 3) {
                                DocNo = "SR-000" + NextNumber;
                            } else if (count == 4) {
                                DocNo = "SR-00" + NextNumber;
                            } else if (count == 5) {
                                DocNo = "SR-0" + NextNumber;
                            } else {
                                DocNo = "SR-" + NextNumber;
                            }

//                            //Select STOCK RECEIVE from Registry table
//                            String querySELECT77932 = "SELECT RegValue FROM Registry WHERE  (RegID = 77932)";
//                            PreparedStatement stmtSELECT77932 = con.prepareStatement(querySELECT77932);
//                            ResultSet rsSELECT77932 = stmtSELECT77932.executeQuery();
//                            String Descp = null;
//                            while (rsSELECT77932.next()) {
//                                Descp = rsSELECT77932.getString("RegValue");
//                                Log.e("SQL", Descp);
//                            }


                            //Select Item UOM
                            String querySELECTBaseUOM = "SELECT BaseUOM FROM Item WHERE ItemCode = '" + edittext.getText().toString() + "';";
                            PreparedStatement stmtSELECTBaseUOM = con.prepareStatement(querySELECTBaseUOM);
                            ResultSet rsSELECTBaseUOM = stmtSELECTBaseUOM.executeQuery();
                            String BaseUOM = null;
                            while (rsSELECTBaseUOM.next()) {
                                BaseUOM = rsSELECTBaseUOM.getString("BaseUOM");
                                Log.e("SQL", BaseUOM);
                            }


                            //Set Date format
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = df.format(Calendar.getInstance().getTime());


                            //Insert RCV
                            String queryInsertRCV = "INSERT INTO [" + db + "].[dbo].[RCV] ([DocKey],[DocNo],[DocDate],[Description],[Total],[Remark1],[PrintCount],[Cancelled],[LastModified],[LastModifiedUserID],[CreatedTimeStamp],[CreatedUserID],[LastUpdate],[CanSync])VALUES('" +
                                    DocKey + "', '" + DocNo + "', '" + date + "', 'RFID STOCK RECEIVE','0','"+ text.getText().toString() +"', '0', 'F', '" + date + "', 'ADMIN', '" + date + "', 'ADMIN', '0', 'T');";
                            PreparedStatement stmtInsertRCV = con.prepareStatement(queryInsertRCV);
                            stmtInsertRCV.executeUpdate();


                            //Insert RCVDTL
                            String queryInsertRCVDTL = "INSERT INTO [" + db + "].[dbo].[RCVDTL] ([DtlKey],[DocKey],[Seq],[ItemCode],[Location],[Qty],[UOM],[UnitCost],[SubTotal],[PrintOut])VALUES('" +
                                    DtlKey + "', '" + DocKey + "', '1', '" + Itemcode + "', 'HQ', '1', '" + BaseUOM + "', '0', '0', 'T')";
                            PreparedStatement stmtInsertRCVDTL = con.prepareStatement(queryInsertRCVDTL);
                            stmtInsertRCVDTL.executeUpdate();


                            //Insert StockDTL
                            String queryINSERTStockDTL = "INSERT INTO [" + db + "].[dbo].[StockDTL] ([StockDTLKey],[ItemCode],[UOM],[Location]," +
                                    "[DocDate],[Seq],[DocType],[DocKey],[DtlKey],[Qty],[Cost],[AdjustedCost],[TotalCost],[CostType]," +
                                    "[LastModified],[ReferTo],[InputCost])VALUES('" +
                                    StockDTLKey + "', '" + Itemcode + "', '" + BaseUOM + "', 'HQ', '" + date + "', '16', 'SR', '" +
                                    DocKey + "', '" + DtlKey + "', '1', '0', '0', '0', '4', '" + date + "', '0', '0')";
                            PreparedStatement stmtINSERTStockDTL = con.prepareStatement(queryINSERTStockDTL);
                            stmtINSERTStockDTL.executeUpdate();



                            //Update ItemBalQty
                            String querySELECTItemBalQty = "SELECT [BalQty] FROM [" + db + "].[dbo].[ItemBalQty] WHERE [ItemCode] = '"+ Itemcode +"'";
                            PreparedStatement stmtSELECTItemBalQty = con.prepareStatement(querySELECTItemBalQty);
                            ResultSet rsSELECTItemBalQty = stmtSELECTItemBalQty.executeQuery();
                            int ItemBalQty = 0;
                            while (rsSELECTItemBalQty.next()) {
                                ItemBalQty = rsSELECTItemBalQty.getInt("BalQty");
                            }
                            String queryUpdateItemBalQty = "UPDATE [dbo].[ItemBalQty] SET [BalQty] = '" + (ItemBalQty+1) + "' WHERE [ItemCode] = '"+ Itemcode +"';";
                            PreparedStatement stmtSUpdateItemBalQty = con.prepareStatement(queryUpdateItemBalQty);
                            stmtSUpdateItemBalQty.executeUpdate();



                            //Update ItemBatchBalQty
                            String querySELECTItemBatchBalQty = "SELECT [BalQty] FROM [" + db + "].[dbo].[ItemBatchBalQty] WHERE [ItemCode] = '"+ Itemcode +"'";
                            PreparedStatement stmtSELECTItemBatchBalQty= con.prepareStatement(querySELECTItemBatchBalQty);
                            ResultSet rsSELECTItemBatchBalQty = stmtSELECTItemBatchBalQty.executeQuery();
                            int ItemBatchBalQty = 0;
                            while (rsSELECTItemBatchBalQty.next()) {
                                ItemBatchBalQty = rsSELECTItemBatchBalQty.getInt("BalQty");
                            }
                            String queryUpdateItemBatchBalQty = "UPDATE [dbo].[ItemBatchBalQty] SET [BalQty] = '" + (ItemBatchBalQty+1) + "' WHERE [ItemCode] = '"+ Itemcode +"';";
                            PreparedStatement stmtSUpdateItemBatchBalQty = con.prepareStatement(queryUpdateItemBatchBalQty);
                            stmtSUpdateItemBatchBalQty.executeUpdate();
                            builder.setMessage("Stock Receive Done");
                            addListenerOnButton();

                        } else {
                            edittext.requestFocus();
                            edittext.setError("Invalid ItemCode");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



    }

    public void addListenerOnButton() {
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

        builder.setTitle("Stock Receive");
        builder.setMessage("Your data has been recorded");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(StockReceiveEditData.this, "Stock Receive Done", Toast.LENGTH_LONG).show();
                Intent i =new Intent(getApplicationContext(),StockReceive.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

//            case(R.id.Save):
//                addListenerOnButton();
//                break;

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

//    //OkHttp get Request
//    private void doGetRequest(String finalValue){
////        Log.d("OKHTTP","i ="+finalValue);
////        Log.d("OKHTTP", "Function called");
//        value2 = edittext.getText().toString();
////        Log.d("OKHTTP", "itemcode =" + value2);
//        String url = "http://192.168.0.167/iot_project/testsubmission.php?itemcode='"+ value2 +"'&RFID_id='" + finalValue + "'";
////        Log.d("OKHTTP", url);
//        OkHttpClient client = new OkHttpClient();
////        Log.d("OKHTTP", "Client created");
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
////        Log.d("OKHTTP", "Request Build succesful.");
//        try {
//            Response response = client.newCall(request).execute();
////            Log.d("OKHTTP", "Got the response");
//        } catch (IOException e) {
////            Log.d("OKHTTP", "Exception while doing request");
//            e.printStackTrace();
//        }
//    }


    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            connectionURL = "jdbc:jtds:sqlserver://192.168.0.167/AED_aw;instance=MSSQLSERVER;user=waiyuanaw";
            connectionURL = "jdbc:jtds:sqlserver://"+ server +"/"+database+";user="+ user +";password="+ password+";";
            Log.e("SQL", connectionURL);
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error :",e.getMessage());
        }
        return connection;
    }
}
package com.example.Util.Connection.Connection;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.StockInventory.ListItemCode.itemsetting;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    public static String ip = "192.168.0.88:1433"; //192.168.0.88\SQLEXPRESS01,1433
    public static String un = "sa";
    public static String pass ="rs6663";
    //public static String db = "AED_MOBILE";
    public static String db = "MES_Developer_EM30_2";
    //public static String integratedsystem = "Autocount2";
    public static String integratedsystem = "PrismaMES";

    @SuppressLint("NewApi")
    public static Connection connectionClass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //connectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database + ";user=" + user + ";password=" + password + ";";
            connectionURL = "jdbc:jtds:sqlserver://192.168.0.88:1433/MES_Developer_EM30_2";
            String username1 = "sa";
            String password1 = "rs6663";
            Log.e("SQL", connectionURL);
            //connection = DriverManager.getConnection(connectionURL);
            connection = DriverManager.getConnection(connectionURL,username1,password1);
            Log.d("Conn:", connectionURL.toString());
        } catch (Exception e) {
            Log.e("SQL Connection Error :", e.getMessage());
        }
        return connection;
    }
}

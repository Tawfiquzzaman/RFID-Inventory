package com.example.Util.Connection.InventoryHelper;

import android.os.Build;

import com.example.Util.Connection.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventLog {

    protected final static String UpdateEventLog = "INSERT INTO [dbo].[EventLog]([EventDateTime],[UserID],[ComputerName],[DocType],[DocKey],[EventType],[Description])"+
    "VALUES(?,'ADMIN',?,?,?,0,?)";



    public static void UpdateEventLog(int[] ArrOfInt, String[] ArrOfString, String Doctype) throws SQLException {
        Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
        PreparedStatement stmt = con.prepareStatement(UpdateEventLog);
        stmt.setString(1, InventoryHelper.getdateformat());
        stmt.setString(2, getDeviceName());

        if(Doctype.matches("SA1")||Doctype.matches("SA2")){
            stmt.setString(3, "SA");
        }else if(Doctype.matches("SR1")||Doctype.matches("SR2")){
            stmt.setString(3, "SR");
        }else if (Doctype.matches("SI")){
            stmt.setString(3, "SI");
        }

        stmt.setString(4, String.valueOf(ArrOfInt[0]));

        if(Doctype.matches("SA1")||Doctype.matches("SA2")){
            stmt.setString(5, "New Stock Adjustment: "+ ArrOfString[4] );
        }else if(Doctype.matches("SR1")||Doctype.matches("SR2")){
            stmt.setString(5, "New Stock Receive: "+ ArrOfString[4] );
        }else if (Doctype.matches("SI")){
            stmt.setString(5, "New Stock Issue: "+ ArrOfString[4] );
        }

        stmt.executeUpdate();
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }
}

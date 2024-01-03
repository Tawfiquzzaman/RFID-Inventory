package com.example.Util.Connection.InventoryHelper;

import com.example.Util.Connection.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class InventoryHelper extends InventorySQLcmd {

    public static int[] executeValue(Connection con, String query, String itemcode) throws SQLException {
        if (con == null || query == null || itemcode == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        int partId = 0;
        int uomId = 0;
        // Using try-with-resources to ensure ResultSet and PreparedStatement are closed after usage
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, itemcode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    partId = rs.getInt("PartId");
                    uomId = rs.getInt("UomId");
                }
            }
        } catch (SQLException e) {
            // Consider logging the exception or handling it as per your application's needs.
            throw e;
        }
        return new int[] { partId, uomId };
    }


    public static String[] executeitemcode(Connection con, String query, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, itemcode);
        ResultSet rs = stmt.executeQuery();
        String Itemcode = null;
        String BaseUOM = null;
        while (rs.next()) {
            //Itemcode = rs.getString("itemcode");
            //BaseUOM = rs.getString("BaseUOM");
            Itemcode = rs.getString("PartCode");
            BaseUOM = rs.getString("UOM");
        }
        String[] ArrayItemCode = {Itemcode, BaseUOM};
        rs.close();
        stmt.close();
        return ArrayItemCode;
    }

    public static void UpdateQuery(Connection con, String query, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, itemcode);
        stmt.executeUpdate();
    }

    public static void UpdateQuery(Connection con, String query) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.executeUpdate();
    }

    public static String executeQuery(Connection con, String query) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        String value = null;
        while (rs.next()) {
            value = rs.getString("RegValue");
        }
        return value;
    }

    public static String executeQuery(Connection con, String query, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, itemcode);
        ResultSet rs = stmt.executeQuery();
        String value = null;
        while (rs.next()) {
            //value = rs.getString("itemcode");
            value = rs.getString("PartCode");
        }
        return value;
    }

    public static void insertItemBalQtyQuery(Connection con, String query, String[] ArrOfString) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, ArrOfString[0]);
        stmt.setString(2, ArrOfString[1]);
        stmt.setString(3, "HQ");
        stmt.setString(4, "1");
        stmt.executeUpdate();
    }

    public static void insertUTDStockCost(Connection con, String query, String[] ArrOfString, int UTDStockCostKey) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, String.valueOf(UTDStockCostKey));
        stmt.setString(2, ArrOfString[0]);
        stmt.setString(3, "HQ");
        stmt.setString(4, "1");
        stmt.setString(5, ArrOfString[0]);
        stmt.executeUpdate();
    }

    public static String getdateformat() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }


    public static class DateUtil {
        private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        public static String getCurrentFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // or any other timezone if required
            return sdf.format(Calendar.getInstance().getTime());
        }
    }


    public static String ItemCodeValidation(String itemcode) {
        try {
            Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            PreparedStatement stmt = con.prepareStatement(getLoadItemCode());
            stmt.setString(1, itemcode);
            ResultSet rs = stmt.executeQuery();
            String Itemcode = null;
            String BaseUOM = null;
            while (rs.next()) {
                //Itemcode = rs.getString("itemcode");
                //BaseUOM = rs.getString("BaseUOM");
                Itemcode = rs.getString("PartCode");
                BaseUOM = rs.getString("UOM");
            }
            return Itemcode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

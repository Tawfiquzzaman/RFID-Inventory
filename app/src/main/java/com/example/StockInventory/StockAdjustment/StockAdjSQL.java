package com.example.StockInventory.StockAdjustment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.Util.Connection.InventoryHelper.InventoryHelper;

public class StockAdjSQL extends StockAdjSQLcmd{

    public static String executeLoadDocNo(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(LoadDocNo);
        ResultSet rs = stmt.executeQuery();
        String NextNumber = null;
        while (rs.next()) {
            NextNumber = rs.getString("NextNumber");
        }
        String DocNo = countDigit(NextNumber);
        return DocNo;
    }

    public static void executeInsertADJ(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertADJ);
        stmt.setInt(1, ArrOfInt[0]);
        stmt.setString(2, ArrOfString[4]);
        stmt.setString(3, InventoryHelper.getdateformat());
        stmt.setString(4, ArrOfString[0]);
        stmt.setString(5, InventoryHelper.getdateformat());
        stmt.setString(6, InventoryHelper.getdateformat());
        stmt.executeUpdate();
    }

    public static void executeInsertADJDTL(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertADJDTL);
        stmt.setInt(1, ArrOfInt[1]);
        stmt.setInt(2, ArrOfInt[0]);
        stmt.setString(3, ArrOfString[1]);
        stmt.setString(4, ArrOfString[2]);
        stmt.executeUpdate();
    }

    public static void executeInsertStockDTL(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertStockDTL);
        stmt.setString(1, ArrOfString[3]);
        stmt.setString(2, ArrOfString[1]);
        stmt.setString(3, ArrOfString[2]);
        stmt.setString(4, InventoryHelper.getdateformat());
        stmt.setInt(5, ArrOfInt[0]);
        stmt.setInt(6, ArrOfInt[1]);
        stmt.setString(7, InventoryHelper.getdateformat());
        stmt.executeUpdate();
    }

    public static String countDigit(String NextNumber) {
        int count = 0;
        String DocNo = null;
        int n = Integer.parseInt(NextNumber);
        while (n != 0) {
            n = n / 10;
            ++count;
        }
        if (count == 1) {
            DocNo = "ADJ-00000" + NextNumber;
            return DocNo;
        } else if (count == 2) {
            DocNo = "ADJ-0000" + NextNumber;
            return DocNo;
        } else if (count == 3) {
            DocNo = "ADJ-000" + NextNumber;
            return DocNo;
        } else if (count == 4) {
            DocNo = "ADJ-00" + NextNumber;
            return DocNo;
        } else if (count == 5) {
            DocNo = "ADJ-0" + NextNumber;
            return DocNo;
        } else {
            DocNo = "ADJ-" + NextNumber;
            return DocNo;
        }
    }
}

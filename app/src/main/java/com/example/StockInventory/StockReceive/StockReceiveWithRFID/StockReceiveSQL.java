package com.example.StockInventory.StockReceive.StockReceiveWithRFID;

import com.example.Util.Connection.InventoryHelper.InventoryHelper;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockReceiveSQL extends StockReceiveSQLcmd{

    @NotNull
    public static int executeLoadDocKey(Connection con) throws SQLException {
        String query = "SELECT TOP 1 DocKey FROM dbo.StockOutIn WHERE Type = 'SR' ORDER BY DocKey DESC";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("DocKey");
            } else {
                // Handle the case where the result set is empty.
                // You can return a default value or throw an exception depending on your requirements.
                throw new SQLException("No records found for type 'SR'.");
            }
        }
    }


    @NotNull
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
    @NotNull
    public static String executeLoadDocNoPrismaMES(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(LoadDocNoPrismaMES);
        ResultSet rs = stmt.executeQuery();
        String NextNumber = null;
        while (rs.next()) {
            NextNumber = rs.getString("Number");
        }
        String DocNo = countDigit(NextNumber);
        return DocNo;
    }

    public static void executeInsertRCV(@NotNull Connection con, @NotNull String[] ArrOfString, @NotNull int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertRCV);
        stmt.setInt(1, ArrOfInt[0]);
        stmt.setString(2, ArrOfString[4]);
        stmt.setString(3, InventoryHelper.getdateformat());
        stmt.setString(4, ArrOfString[0]);
        stmt.setString(5, InventoryHelper.getdateformat());
        stmt.setString(6, InventoryHelper.getdateformat());
        stmt.executeUpdate();
    }
    public static void executeInsertRCVPrismaMES(@NotNull Connection con, @NotNull String[] ArrOfString, @NotNull int[] ArrOfInt) throws SQLException {
        // Use try-with-resources to ensure that the PreparedStatement is closed properly
        try (PreparedStatement stmt = con.prepareStatement(InsertRCVPrismaMES)) {

            // Constants make the code more readable
            final int DOC_NO_INDEX = 3;

            stmt.setString(1, ArrOfString[DOC_NO_INDEX]);
            stmt.setString(2, InventoryHelper.DateUtil.getCurrentFormattedDate()); // Assuming this method gets the current formatted date

            stmt.executeUpdate();
        }
        // No need to catch SQLException here since the method declares it throws SQLException.
        // The caller should handle it.
    }

    public static void executeInsertRCVDTL(@NotNull Connection con, @NotNull String[] ArrOfString, @NotNull int[] ArrOfInt) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(InsertRCVDTL);
        stmt.setInt(1, ArrOfInt[1]);
        stmt.setInt(2, ArrOfInt[0]);
        stmt.setString(3, ArrOfString[1]);
        stmt.setString(4, ArrOfString[2]);
        stmt.executeUpdate();
    }

    //INSERT INTO [dbo].[StockOutInDTL] ([PartId],[Quantity],[UOMId],[Date],[Type],[DocKey])VALUES(?,'1',?,?,'SR',?)";

    public static void executeInsertRCVDTLPrismaMES(Connection con, String[] dataStrings, int[] dataInts) throws SQLException {
        if (con == null || dataStrings == null || dataInts == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        String insertQuery = InsertRCVDTLPrismaMES; // Replace with your actual query
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setInt(1, dataInts[0]); // Assuming PartId is at index 1
            stmt.setInt(2, dataInts[1]); // Assuming UOMId is at index 2
            stmt.setString(3, InventoryHelper.DateUtil.getCurrentFormattedDate()); // Date
            stmt.setInt(4, dataInts[2]); // Assuming DocKey is at index 3
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Consider using a logging library to log this exception.
            throw e;
        }
    }


    public static void executeInsertStockDTL(@NotNull Connection con, @NotNull String[] ArrOfString, @NotNull int[] ArrOfInt) throws SQLException {

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

    @NotNull
    public static String countDigit(String NextNumber) {
        int count = 0;
        String DocNo = null;
        int n = Integer.parseInt(NextNumber);
        while (n != 0) {
            n = n / 10;
            ++count;
        }
        if (count == 1) {
            DocNo = "SR-00000" + NextNumber;
            return DocNo;
        } else if (count == 2) {
            DocNo = "SR-0000" + NextNumber;
            return DocNo;
        } else if (count == 3) {
            DocNo = "SR-000" + NextNumber;
            return DocNo;
        } else if (count == 4) {
            DocNo = "SR-00" + NextNumber;
            return DocNo;
        } else if (count == 5) {
            DocNo = "SR-0" + NextNumber;
            return DocNo;
        } else {
            DocNo = "SR-" + NextNumber;
            return DocNo;
        }
    }
}

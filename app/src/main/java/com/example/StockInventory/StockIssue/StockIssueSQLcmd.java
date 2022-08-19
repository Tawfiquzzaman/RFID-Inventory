package com.example.StockInventory.StockIssue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StockIssueSQLcmd {

    protected final static String LoadItemCode = "SELECT itemcode, BaseUOM from [dbo].[item] WHERE itemcode = ?;";
    protected final static String LoadRegValue = "SELECT RegValue FROM Registry WHERE  (RegID = 32768);";
    protected final static String UpdateRegValue = "UPDATE Registry SET [RegValue] = [RegValue]+3 WHERE (RegID = 32768);";
    protected final static String LoadStockDTLKey = "SELECT RegValue FROM Registry WHERE  (RegID = 32772)";
    protected final static String UpdateStockDTLKey = "UPDATE Registry SET [RegValue] = [RegValue]+1 WHERE (RegID = 32772);";
    protected final static String Update128 = "UPDATE [dbo].[Registry] SET [RegValue] = [RegValue]+1 WHERE RegID = 128;";
    protected final static String LoadDocNo = "SELECT NextNumber FROM [dbo].[DocNoFormat] WHERE DocType = 'SI';";
    protected final static String UpdateDocNo = "UPDATE DocNoFormat SET [NextNumber] = [NextNumber]+1 WHERE DocType = 'SI';";
    protected final static String InsertISS = "INSERT INTO [dbo].[ISS] ([DocKey],[DocNo],[DocDate],[Description],[Total],[Remark1],[PrintCount],[Cancelled]," +
            "[LastModified],[LastModifiedUserID],[CreatedTimeStamp],[CreatedUserID],[LastUpdate],[CanSync],[ReallocatePurchaseByProject])VALUES(" +
            "?,?,?,'RFID STOCK ISSUE','0',?,'0','F',?,'ADMIN',?,'ADMIN','0','T','F');";

    protected final static String InsertISSDTL = "INSERT INTO [dbo].[ISSDTL]([DtlKey],[DocKey],[Seq],[ItemCode],[Location],[Qty],[UOM]\n" +
            ",[UnitCost],[SubTotal],[PrintOut])VALUES(?,?,'16',?,'HQ','1',?,'0','0','T');";

    protected final static String InsertStockDTL = "INSERT INTO [dbo].[StockDTL] ([StockDTLKey],[ItemCode],[UOM],[Location],[DocDate],[Seq],[DocType]," +
            "[DocKey],[DtlKey],[Qty],[Cost],[AdjustedCost],[TotalCost],[CostType],[LastModified],[ReferTo],[InputCost])VALUES(" +
            "? , ? , ?, 'HQ',? , '16', 'SI',? , ? , '1', '0', '0', '0', '1', ?, '0', '0');";

    protected final static String UpdateItemBalQty = "UPDATE [dbo].[ItemBalQty] SET [BalQty] = [BalQty]-1 WHERE [ItemCode] = ?;";
    protected final static String UpdateItemBatchBalQty = "UPDATE [dbo].[ItemBatchBalQty] SET [BalQty] = [BalQty]-1 WHERE [ItemCode] = ? ;";
    protected final static String UpdateItemUOM = "UPDATE [dbo].[ItemUOM] SET [BalQty] = [BalQty]-1 WHERE ItemCode = ? ";
    protected final static String UpdateUTDStockCost = "UPDATE [dbo].[UTDStockCost] SET [UTDQty] = [UTDQty]-1 WHERE [ItemCode] = ?;";

    public static String[] getLoadItemCode(Connection con, String itemcode) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(LoadItemCode);
        stmt.setString(1, itemcode);
        ResultSet rs = stmt.executeQuery();
        String Itemcode = null;
        String BaseUOM = null;
        while (rs.next()) {
            Itemcode = rs.getString("itemcode");
            BaseUOM = rs.getString("BaseUOM");
        }
        String[] ArrayItemCode = {Itemcode, BaseUOM};
        return ArrayItemCode;
    }

    public static String getLoadRegValue(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(LoadRegValue);
        ResultSet rs = stmt.executeQuery();
        String RegValue = null;
        while (rs.next()) {
            RegValue = rs.getString("RegValue");
        }
        return RegValue;
    }

    public static void getUpdateRegValue(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(UpdateRegValue);
        stmt.executeUpdate();

    }

    public static String getLoadStockDTLKey(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(LoadStockDTLKey);
        ResultSet rs = stmt.executeQuery();
        String StockDTLKey = null;
        while (rs.next()) {
            StockDTLKey = rs.getString("RegValue");
        }
        return StockDTLKey;
    }

    public static void getUpdateStockDTLKey(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(UpdateStockDTLKey);
        stmt.executeUpdate();
    }

    public static String getLoadDocNo(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(LoadDocNo);
        ResultSet rs = stmt.executeQuery();
        String NextNumber = null;
        while (rs.next()) {
            NextNumber = rs.getString("NextNumber");
        }
        String DocNo = countDigit(NextNumber);
        return DocNo;
    }

    public static void getUpdate128(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(Update128);
        stmt.executeUpdate();

    }

    public static void getUpdateDocNo(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UpdateDocNo);
        stmt.executeUpdate();
    }

    public static void getInsertISS(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertISS);
        stmt.setInt(1, ArrOfInt[0]);
        stmt.setString(2, ArrOfString[4]);
        stmt.setString(3, getdateformat());
        stmt.setString(4, ArrOfString[0]);
        stmt.setString(5, getdateformat());
        stmt.setString(6, getdateformat());
        stmt.executeUpdate();
    }

    public static void getInsertISSDTL(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertISSDTL);
        stmt.setInt(1, ArrOfInt[1]);
        stmt.setInt(2, ArrOfInt[0]);
        stmt.setString(3, ArrOfString[1]);
        stmt.setString(4, ArrOfString[2]);
        stmt.executeUpdate();
    }

    public static void getInsertStockDTL(Connection con, String[] ArrOfString, int[] ArrOfInt) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(InsertStockDTL);
        stmt.setString(1, ArrOfString[3]);
        stmt.setString(2, ArrOfString[1]);
        stmt.setString(3, ArrOfString[2]);
        stmt.setString(4, getdateformat());
        stmt.setInt(5, ArrOfInt[0]);
        stmt.setInt(6, ArrOfInt[1]);
        stmt.setString(7, getdateformat());
        stmt.executeUpdate();
    }

    public static void getUpdateItemBalQty(Connection con, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UpdateItemBalQty);
        stmt.setString(1, itemcode);
        stmt.executeUpdate();

    }

    public static void getUpdateItemBatchBalQty(Connection con, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UpdateItemBatchBalQty);
        stmt.setString(1, itemcode);
        stmt.executeUpdate();
    }

    public static void getUpdateItemUOM(Connection con, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UpdateItemUOM);
        stmt.setString(1, itemcode);
        stmt.executeUpdate();
    }

    public static void getUpdateUTDStockCost(Connection con, String itemcode) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UpdateUTDStockCost);
        stmt.setString(1, itemcode);
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
            DocNo = "SI-00000" + NextNumber;
            return DocNo;
        } else if (count == 2) {
            DocNo = "SI-0000" + NextNumber;
            return DocNo;
        } else if (count == 3) {
            DocNo = "SI-000" + NextNumber;
            return DocNo;
        } else if (count == 4) {
            DocNo = "SI-00" + NextNumber;
            return DocNo;
        } else if (count == 5) {
            DocNo = "SI-0" + NextNumber;
            return DocNo;
        } else {
            DocNo = "SI-" + NextNumber;
            return DocNo;
        }
    }

    public static String getdateformat() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}

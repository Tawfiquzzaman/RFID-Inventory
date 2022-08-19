package com.example.StockInventory.StockAdjustment;

public class StockAdjSQLcmd {

    protected final static String LoadDocNo = "SELECT NextNumber FROM [dbo].[DocNoFormat] WHERE DocType = 'SA';";
    protected final static String UpdateDocNo = "UPDATE DocNoFormat SET [NextNumber] = [NextNumber]+1 WHERE DocType = 'SA';";

    protected final static String InsertADJ = "INSERT INTO [dbo].[ADJ] ([DocKey], [DocNo], [DocDate], [Description], [Total], [Remark1], [PrintCount]" +
            ", [Cancelled], [LastModified], [LastModifiedUserID],[CreatedTimeStamp], [CreatedUserID], [LastUpdate], [CanSync])" +
            "VALUES(?,?, ?, 'RFID STOCK ADJUSTMENT','0', ? , '0', 'F', ?, 'ADMIN', ?, 'ADMIN', '0', 'T')";
    protected final static String InsertADJDTL = "INSERT INTO [dbo].[ADJDTL]([DtlKey],[DocKey],[Seq],[ItemCode],[Location],[Qty],[UOM]" +
            ",[UnitCost],[SubTotal],[PrintOut])VALUES(?,?,'16',?,'HQ','1',?,'0','0','T');";
    protected final static String InsertStockDTL = "INSERT INTO [dbo].[StockDTL] ([StockDTLKey],[ItemCode],[UOM],[Location],[DocDate],[Seq],[DocType]," +
            "[DocKey],[DtlKey],[Qty],[Cost],[AdjustedCost],[TotalCost],[CostType],[LastModified],[ReferTo],[InputCost])VALUES(" +
            "? , ? , ?, 'HQ',? , '16', 'SA',? , ? , '1', '0', '0', '0', '1', ?, '0', '0');";

    public String getLoadDocNo(){
        return LoadDocNo;
    }
    public static String getUpdateDocNo(){
        return UpdateDocNo;
    }
    public String getInsertADJ(){
        return InsertADJ;
    }
    public String getInsertADJDTL(){
        return InsertADJDTL;
    }
    public String getInsertStockDTL(){
        return InsertStockDTL;
    }

}

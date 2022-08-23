package com.example.StockInventory.StockReceive.StockReceiveWithRFID;

public class StockReceiveSQLcmd {

    protected final static String LoadDocNo = "SELECT NextNumber FROM [dbo].[DocNoFormat] WHERE DocType = 'SR'";
    protected final static String UpdateDocNo ="UPDATE DocNoFormat SET [NextNumber] = [NextNumber]+1 WHERE DocType = 'SR';";
    protected final static String InsertRCV = "INSERT INTO [dbo].[RCV] ([DocKey],[DocNo],[DocDate],[Description],[Total],[Remark1],[PrintCount]," +
            "[Cancelled],[LastModified],[LastModifiedUserID],[CreatedTimeStamp],[CreatedUserID],[LastUpdate],[CanSync])" +
            "VALUES( ?, ?, ?, 'RFID STOCK RECEIVE', '0', ?, '0', 'F', ?, 'ADMIN', ?, 'ADMIN', '0', 'T');";
    protected final static String InsertRCVDTL = "INSERT INTO [dbo].[RCVDTL] ([DtlKey],[DocKey],[Seq],[ItemCode],[Location],[Qty],[UOM],[UnitCost]," +
            "[SubTotal],[PrintOut])VALUES(? , ?, '1', ?, 'HQ', '1', ?, '0', '0', 'T')";
    protected final static String InsertStockDTL = "INSERT INTO [dbo].[StockDTL] ([StockDTLKey],[ItemCode],[UOM],[Location],[DocDate],[Seq],[DocType]," +
            "[DocKey],[DtlKey],[Qty],[Cost],[AdjustedCost],[TotalCost],[CostType],[LastModified],[ReferTo],[InputCost])VALUES(" +
            "? , ?, ?, 'HQ', ?, '16', 'SR', ?, ?, '1', '0', '0', '0', '4', ?, '0', '0')";

    public String getLoadDocNo(){return LoadDocNo;}
    public static String getUpdateDocNo(){return UpdateDocNo;}
    public String getInsertRCV(){return InsertRCV;}
    public String getInsertRCVDTL(){return InsertRCVDTL;}
    public String geInsertStockDTL(){return InsertStockDTL;}

}

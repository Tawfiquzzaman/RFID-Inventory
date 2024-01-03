package com.example.Util.Connection.InventoryHelper;

public class InventorySQLcmd {

    //protected final static String LoadItemCode = "SELECT itemcode, BaseUOM from [dbo].[item] WHERE itemcode = ?;";
    //protected final static String LoadItemCode = "SELECT PartCode, UOM from [dbo].[PartDTL] WHERE itemcode = ?;";
    protected final static String LoadItemCode = "SELECT PartCode, UOM.UOM from [dbo].[PartDTL] left join PartUOM on PartUOM.PartId = PartDTL.Id left join UOM on UOM.Id = PartUOM.UOMId WHERE PartCode = ?;";
    protected final static String LoadPartIdPrismaMES = "SELECT PartDTL.Id As PartId, UOM.Id As UomId from [dbo].[PartDTL] left join PartUOM on PartUOM.PartId = PartDTL.Id left join UOM on UOM.Id = PartUOM.UOMId WHERE PartCode = ?;";



    protected final static String LoadRegValue = "SELECT RegValue FROM Registry WHERE  (RegID = 32768);";
    protected final static String UpdateRegValue = "UPDATE Registry SET [RegValue] = [RegValue]+3 WHERE (RegID = 32768);";
    protected final static String LoadStockDTLKey = "SELECT RegValue FROM Registry WHERE  (RegID = 32772)";
    protected final static String UpdateStockDTLKey = "UPDATE Registry SET [RegValue] = [RegValue]+1 WHERE (RegID = 32772);";
    protected final static String Update128 = "UPDATE [dbo].[Registry] SET [RegValue] = [RegValue]+1 WHERE RegID = 128;";

    protected final static String UpdateItemUOM = "UPDATE [dbo].[ItemUOM] SET [BalQty] = [BalQty]+1 WHERE ItemCode = ? ";

    protected final static String SelectItemBalQty = "SELECT [ItemCode] FROM [dbo].[ItemBalQty] WHERE [ItemCode] = ?";
    protected final static String UpdateItemBalQty = "UPDATE [dbo].[ItemBalQty] SET [BalQty] = [BalQty]+1 WHERE [ItemCode] = ?;";
    protected final static String InsertItemBalQty = "INSERT INTO [dbo].[ItemBalQty] ([ItemCode], [UOM], [Location], [BalQty])VALUES(?, ?, ?, ?)";

    protected final static String SelectItemBatchBalQty = "SELECT [ItemCode] FROM [dbo].[ItemBatchBalQty] WHERE [ItemCode] = ?";
    protected final static String UpdateItemBatchBalQty = "UPDATE [dbo].[ItemBatchBalQty] SET [BalQty] = [BalQty]+1 WHERE [ItemCode] = ? ;";
    protected final static String InsertItemBatchBalQty = "INSERT INTO ItemBatchBalQty (ItemCode, UOM, Location, BalQty) VALUES (?, ?, ?, ?)";

    protected final static String SelectRegID32771 = "SELECT [RegValue] FROM [dbo].[Registry] WHERE RegID = 32771;";
    protected final static String UpdateRegID32771 = "UPDATE [dbo].[Registry] SET [RegValue] = [RegValue]+1 WHERE RegID = 32771;";

    protected final static String SelectUTDStockCost = "SELECT [ItemCode] FROM [dbo].[UTDStockCost] WHERE [ItemCode] = ?;";
    protected final static String UpdateUTDStockCost = "UPDATE [dbo].[UTDStockCost] SET [UTDQty] = [UTDQty]+1 WHERE [ItemCode] = ?;";
    protected final static String InsertUTDStockCost = "INSERT INTO [dbo].[UTDStockCost]([UTDStockCostKey],[ItemCode],[UOM],[Location],[BatchNo],[UTDQty][UTDCost],[AdjustedCost])VALUES(?,?,?,?,?,1,0,0);";

    public static String getLoadItemCode(){
        return LoadItemCode;
    }
    public static String getLoadPartIdPrismaMES(){
        return LoadPartIdPrismaMES;
    }
    //public static String getLoadItemCodePrismaMES(){
    //    return LoadItemCodePrismaMES;
    //}
    public static String getLoadRegValue(){
        return LoadRegValue;
    }
    public static String getUpdateRegValue(){
        return UpdateRegValue;
    }
    public static String getLoadStockDTLKey(){
        return LoadStockDTLKey;
    }
    public static String getUpdateStockDTLKey(){
        return UpdateStockDTLKey;
    }
    public static String getUpdate128(){
        return Update128;
    }

    public static String getUpdateItemUOM(){
        return UpdateItemUOM;
    }

    public static String getSelectItemBalQty(){return SelectItemBalQty;}
    public static String getUpdateItemBalQty(){
        return UpdateItemBalQty;
    }
    public static String getInsertItemBalQty(){return InsertItemBalQty;}

    public static String getSelectItemBatchBalQty(){return SelectItemBatchBalQty;}
    public static String getUpdateItemBatchBalQty(){
        return UpdateItemBatchBalQty;
    }
    public static String getInsertItemBatchBalQty(){return InsertItemBatchBalQty;}

    public static String getSelectRegID32771(){return SelectRegID32771;}
    public static String getUpdateRegID32771(){return UpdateRegID32771;}

    public static String getSelectUTDStockCost(){return SelectUTDStockCost;}
    public static String getUpdateUTDStockCost(){
        return UpdateUTDStockCost;
    }
    public static String getInsertUTDStockCost(){return InsertUTDStockCost;}

}

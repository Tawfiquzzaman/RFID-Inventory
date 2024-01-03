package com.example.StockInventory.StockReceive.StockReceiveWithRFID;

import com.example.StockInventory.StockAdjustment.StockAdjSQL;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySQLcmd;

import java.sql.Connection;
import java.sql.SQLException;

public class StockReceiveSaveData extends InventoryHelper {

    public static void RCVSave(Connection con, String[] ArrOfString, int[] ArrOfInt) {
        try {
            StockReceiveSQL.executeInsertRCV(con, ArrOfString, ArrOfInt);
            StockReceiveSQL.executeInsertRCVDTL(con, ArrOfString, ArrOfInt);
            StockReceiveSQL.executeInsertStockDTL(con, ArrOfString, ArrOfInt);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void RCVSavePrismaMES(Connection con, String[] dataStrings, int[] dataInts) {
        if (con == null || dataStrings == null || dataInts == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        try {
            StockReceiveSQL.executeInsertRCVPrismaMES(con, dataStrings, dataInts);

            int docKey = StockReceiveSQL.executeLoadDocKey(con);
            int[] newArrOfInt = {dataInts[0], dataInts[1], docKey}; //PartId, UOMId, DocKey

            StockReceiveSQL.executeInsertRCVDTLPrismaMES(con, dataStrings, newArrOfInt);
        } catch (SQLException e) {
            // Consider using a logging library to log this exception.
            e.printStackTrace();
        }
    }


    public static void RCVSaveItemQty(Connection con , String[] ArrayItemCode) throws SQLException {

        //UpdateItemUOM
        UpdateQuery(con, InventorySQLcmd.getUpdateItemUOM(), ArrayItemCode[0]);

        //Update ItemBalQty
        if (executeQuery(con, InventorySQLcmd.getSelectItemBalQty(), ArrayItemCode[0]) == null) {
            insertItemBalQtyQuery(con, InventorySQLcmd.getInsertItemBalQty(), ArrayItemCode);
        } else {
            UpdateQuery(con, InventorySQLcmd.getUpdateItemBalQty(), ArrayItemCode[0]);
        }

        //Update ItemBatchBalQty
        if (executeQuery(con, InventorySQLcmd.getSelectItemBatchBalQty(),  ArrayItemCode[0]) == null) {
            insertItemBalQtyQuery(con, InventorySQLcmd.getInsertItemBatchBalQty(), ArrayItemCode);
        } else {
            UpdateQuery(con, InventorySQLcmd.getUpdateItemBatchBalQty(),  ArrayItemCode[0]);
        }

        //Update UTDStockCost
        if (executeQuery(con, InventorySQLcmd.getSelectUTDStockCost(),  ArrayItemCode[0]) == null) {
            String UTDStockCostKey = executeQuery(con, InventorySQLcmd.getSelectRegID32771());
            UpdateQuery(con, InventorySQLcmd.getSelectRegID32771());
            insertUTDStockCost(con, InventorySQLcmd.getInsertUTDStockCost(), ArrayItemCode, Integer.parseInt(UTDStockCostKey));
        } else {
            UpdateQuery(con, InventorySQLcmd.getUpdateUTDStockCost(),  ArrayItemCode[0]);
        }
    }
}

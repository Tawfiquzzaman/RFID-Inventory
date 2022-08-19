package com.example.StockInventory.StockAdjustment;

import android.view.View;

import com.example.Util.Connection.ConnectionClass;
import com.example.Util.Connection.InventoryHelper.InventoryHelper;
import com.example.Util.Connection.InventoryHelper.InventorySQLcmd;

import java.sql.Connection;
import java.sql.SQLException;

public class StockAdjSaveData extends InventoryHelper {

    public static void ADJSave(Connection con, String[] ArrOfString, int[] ArrOfInt) {

        try {

            StockAdjSQL.executeInsertADJ(con, ArrOfString, ArrOfInt);
            StockAdjSQL.executeInsertADJDTL(con, ArrOfString, ArrOfInt);
            StockAdjSQL.executeInsertStockDTL(con, ArrOfString, ArrOfInt);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void ADJSaveItemQty(Connection con , String[] ArrayItemCode) throws SQLException {

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

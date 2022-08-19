package com.example.Util.Connection.InventoryHelper;

import android.view.View;

import com.example.StockInventory.StockAdjustment.StockAdjSQL;
import com.example.StockInventory.StockAdjustment.StockAdjSQLcmd;
import com.example.StockInventory.StockAdjustment.StockAdjSaveData;
import com.example.Util.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.SQLException;

public class InventorySaveData extends InventoryHelper{

    public static void savedata(String id, String itemcode, View v, String Doctype) {
        try {
            Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if (con == null) {

            } else {

                String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCode(), itemcode);

                String RegValue = executeQuery(con, InventorySQLcmd.getLoadRegValue());
                UpdateQuery(con, InventorySQLcmd.getUpdateRegValue());
                int DocKey = Integer.parseInt(RegValue) + 1;
                int DtlKey = Integer.parseInt(RegValue) + 2;

                UpdateQuery(con, InventorySQLcmd.getUpdate128());

                String StockDTLKey = executeQuery(con, InventorySQLcmd.getLoadStockDTLKey());
                UpdateQuery(con, InventorySQLcmd.getUpdateStockDTLKey());

                String DocNo = CheckDocType(con, Doctype);

                int[] ArrOfInt = {DocKey, DtlKey};
                String[] ArrOfString = {id, itemcode, ArrayItemCode[1], StockDTLKey, DocNo};

                if(Doctype.matches("SA")){

                    StockAdjSaveData.ADJSave(con, ArrOfString, ArrOfInt);
                    StockAdjSaveData.ADJSaveItemQty(con, ArrayItemCode);

                }else if(Doctype.matches("SR")){


                }else if(Doctype.matches("SI")){


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String CheckDocType(Connection con, String DocType) throws SQLException {
        if(DocType.matches("SA")){
            String DocNo = StockAdjSQL.executeLoadDocNo(con);
            UpdateQuery(con, StockAdjSQLcmd.getUpdateDocNo());
            return DocNo;
        }else if(DocType.matches("SR")){

        }else if (DocType.matches("SI")){

        }else{

        }
        return null;
    }

}

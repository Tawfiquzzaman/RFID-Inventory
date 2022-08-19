package com.example.StockInventory.StockIssue;

import android.view.View;

import com.example.Util.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.SQLException;

public class SIsavedata {
//
    public static boolean savedata(String id, String itemcode, View v) {
        try {
            Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if (con == null) {
                return false;
            } else {

                String[] ArrayItemCode = StockIssueSQLcmd.getLoadItemCode(con, itemcode);              //Get ItemCode and Base UOM

                if (ArrayItemCode[0].matches(itemcode)) {

                    String RegValue = StockIssueSQLcmd.getLoadRegValue(con);                            //Get Dockey & DtlKey
                    StockIssueSQLcmd.getUpdateRegValue(con);                                            //Update Dockey & DtlKey
                    int DocKey = Integer.parseInt(RegValue) + 1;
                    int DtlKey = Integer.parseInt(RegValue) + 2;

                    StockIssueSQLcmd.getUpdate128(con);                                                 //Update 128

                    String StockDTLKey = StockIssueSQLcmd.getLoadStockDTLKey(con);                      //Get StockDTLKey
                    StockIssueSQLcmd.getUpdateStockDTLKey(con);                                         //Update StockDTLKey

                    String DocNo = StockIssueSQLcmd.getLoadDocNo(con);                                  //Get DocNo
                    StockIssueSQLcmd.getUpdateDocNo(con);                                               //Update Docno

                    int[] ArrOfInt = {DocKey, DtlKey};
                    String[] ArrOfString = {id, itemcode, ArrayItemCode[1], StockDTLKey, DocNo};

                    try {
                        StockIssueSQLcmd.getInsertISS(con, ArrOfString, ArrOfInt);                      //Insert ISS
                        StockIssueSQLcmd.getInsertISSDTL(con, ArrOfString, ArrOfInt);                   //Insert ISSDTL
                        StockIssueSQLcmd.getInsertStockDTL(con, ArrOfString, ArrOfInt);                 //Insert StockDTL
                        StockIssueSQLcmd.getUpdateItemBalQty(con, itemcode);                            //Update ItemBalQty
                        StockIssueSQLcmd.getUpdateItemBatchBalQty(con, itemcode);                       //Update ItemBatchBalQty
                        StockIssueSQLcmd.getUpdateItemUOM(con, itemcode);                               //Update ItemUOM
                        StockIssueSQLcmd.getUpdateUTDStockCost(con, itemcode);                          //Update UTDStockCost

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

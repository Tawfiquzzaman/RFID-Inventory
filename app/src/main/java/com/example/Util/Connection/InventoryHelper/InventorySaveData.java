package com.example.Util.Connection.InventoryHelper;

import android.view.View;
import android.widget.Toast;

import com.example.StockInventory.StockAdjustment.StockAdjSQL;
import com.example.StockInventory.StockAdjustment.StockAdjSQLcmd;
import com.example.StockInventory.StockAdjustment.StockAdjSaveData;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.StockReceiveSQL;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.StockReceiveSQLcmd;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.StockReceiveSaveData;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.Util.Connection.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.SQLException;

public class InventorySaveData extends InventoryHelper{

    public static void savedata(String id, String itemcode, View v, String Doctype) {
        try
        {
            Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if (con == null) {

            }
            else if (ConnectionClass.integratedsystem.toString() == "PrismaMES")
            {
                //String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCodePrismaMES(), itemcode);
                String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCode(), itemcode); //itemcode,BaseUOM (PartCode,UOM)
                int[] ArrayPartIdUomId = InventoryHelper.executeValue(con, InventorySQLcmd.getLoadPartIdPrismaMES(), itemcode);
                String DocNo = CheckDocType(con, Doctype);
                String[] ArrOfStringPrismaMES = {id, itemcode, ArrayItemCode[1], DocNo};
                int[] ArrOfIntPrismaMES = {ArrayPartIdUomId[0],ArrayPartIdUomId[1]}; //to bring into loop RCVSave, get DocKey, update DTL
                if(Doctype.matches("SR1") || Doctype.matches("SR2")){
                    StockReceiveSaveData.RCVSavePrismaMES(con, ArrOfStringPrismaMES, ArrOfIntPrismaMES); //id, itemcode, ArrayItemCode[1], stockDTLKey, DocNo;;DocKey,DtlKey

                     //PartId, UOMId, DocKey
                    //later update -> StockReceiveSaveData.RCVSaveItemQty(con, ArrayItemCode);
                }

            }
            else if (ConnectionClass.integratedsystem.toString() == "Autocount2")
            {
                String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCode(), itemcode);

                String RegValue = executeQuery(con, InventorySQLcmd.getLoadRegValue());
                UpdateQuery(con, InventorySQLcmd.getUpdateRegValue());
                int DocKey = Integer.parseInt(RegValue) + 1;
                int DtlKey = Integer.parseInt(RegValue) + 2;

                UpdateQuery(con, InventorySQLcmd.getUpdate128());

                String StockDTLKey = executeQuery(con, InventorySQLcmd.getLoadStockDTLKey());
                UpdateQuery(con, InventorySQLcmd.getUpdateStockDTLKey()); //update registry 32772

                String DocNo = CheckDocType(con, Doctype);

                int[] ArrOfInt = {DocKey, DtlKey};
                String[] ArrOfString = {id, itemcode, ArrayItemCode[1], StockDTLKey, DocNo};

                if(Doctype.matches("SA1") || Doctype.matches("SA2")){
                    StockAdjSaveData.ADJSave(con, ArrOfString, ArrOfInt);
                    StockAdjSaveData.ADJSaveItemQty(con, ArrayItemCode);
                }
                else if(Doctype.matches("SR1") || Doctype.matches("SR2")){
                    StockReceiveSaveData.RCVSave(con, ArrOfString, ArrOfInt);
                    StockReceiveSaveData.RCVSaveItemQty(con, ArrayItemCode);
                }
                else if(Doctype.matches("SI")){


                }
                EventLog.UpdateEventLog(ArrOfInt, ArrOfString,Doctype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savedata(String id, String itemcode, String Doctype) {
        try
        {
            Connection con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if (con == null) {

            }
            else if (ConnectionClass.integratedsystem.toString() == "PrismaMES")
            {
                //String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCodePrismaMES(), itemcode);
                String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCode(), itemcode); //itemcode,BaseUOM (PartCode,UOM)
                int[] ArrayPartIdUomId = InventoryHelper.executeValue(con, InventorySQLcmd.getLoadPartIdPrismaMES(), itemcode);
                String DocNo = CheckDocType(con, Doctype);
                String[] ArrOfStringPrismaMES = {id, itemcode, ArrayItemCode[1], DocNo};
                int[] ArrOfIntPrismaMES = {ArrayPartIdUomId[0],ArrayPartIdUomId[1]}; //to bring into loop RCVSave, get DocKey, update DTL
                if(Doctype.matches("SR1") || Doctype.matches("SR2")){
                    StockReceiveSaveData.RCVSavePrismaMES(con, ArrOfStringPrismaMES, ArrOfIntPrismaMES); //id, itemcode, ArrayItemCode[1], stockDTLKey, DocNo;;DocKey,DtlKey

                    //PartId, UOMId, DocKey
                    //later update -> StockReceiveSaveData.RCVSaveItemQty(con, ArrayItemCode);
                }

            }
            else if (ConnectionClass.integratedsystem.toString() == "Autocount2")
            {
                String[] ArrayItemCode = InventoryHelper.executeitemcode(con, InventorySQLcmd.getLoadItemCode(), itemcode);

                String RegValue = executeQuery(con, InventorySQLcmd.getLoadRegValue());
                UpdateQuery(con, InventorySQLcmd.getUpdateRegValue());
                int DocKey = Integer.parseInt(RegValue) + 1;
                int DtlKey = Integer.parseInt(RegValue) + 2;

                UpdateQuery(con, InventorySQLcmd.getUpdate128());

                String StockDTLKey = executeQuery(con, InventorySQLcmd.getLoadStockDTLKey());
                UpdateQuery(con, InventorySQLcmd.getUpdateStockDTLKey()); //update registry 32772

                String DocNo = CheckDocType(con, Doctype);

                int[] ArrOfInt = {DocKey, DtlKey};
                String[] ArrOfString = {id, itemcode, ArrayItemCode[1], StockDTLKey, DocNo};

                if(Doctype.matches("SA1") || Doctype.matches("SA2")){
                    StockAdjSaveData.ADJSave(con, ArrOfString, ArrOfInt);
                    StockAdjSaveData.ADJSaveItemQty(con, ArrayItemCode);
                }
                else if(Doctype.matches("SR1") || Doctype.matches("SR2")){
                    StockReceiveSaveData.RCVSave(con, ArrOfString, ArrOfInt);
                    StockReceiveSaveData.RCVSaveItemQty(con, ArrayItemCode);
                }
                else if(Doctype.matches("SI")){


                }
                EventLog.UpdateEventLog(ArrOfInt, ArrOfString,Doctype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String CheckDocType(Connection con, String DocType) throws SQLException
    {
        if (ConnectionClass.integratedsystem.toString() == "PrismaMES")
        {
            if(DocType.matches("SA1") || DocType.matches("SA2")){
                String DocNo = StockAdjSQL.executeLoadDocNo(con);
                UpdateQuery(con, StockAdjSQLcmd.getUpdateDocNo());
                return DocNo;
            }else if(DocType.matches("SR1")||DocType.matches("SR2")){
                String DocNo = StockReceiveSQL.executeLoadDocNoPrismaMES(con); //find top number and add 1
                UpdateQuery(con, StockReceiveSQLcmd.getUpdateDocNoPrismaMES());//update table running number
                return DocNo;
            }else if (DocType.matches("SI")){

            }
            return null;
        }
        else if (ConnectionClass.integratedsystem.toString() == "Autocount2")
        {
            if(DocType.matches("SA1") || DocType.matches("SA2")){
                String DocNo = StockAdjSQL.executeLoadDocNo(con);
                UpdateQuery(con, StockAdjSQLcmd.getUpdateDocNo());
                return DocNo;
            }else if(DocType.matches("SR1")||DocType.matches("SR2")){
                String DocNo = StockReceiveSQL.executeLoadDocNo(con);
                UpdateQuery(con, StockReceiveSQLcmd.getUpdateDocNo());
                return DocNo;
            }else if (DocType.matches("SI")){

            }
            return null;
        }
        return null;
    }
}

package com.example.Util.Connection.Scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.StockInventory.CodeGenerator.generatecode;
import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;
import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.StockInventory.StockAdjustment.withrfid.editAdj;
import com.example.StockInventory.StockIssue.WithoutRFID.stockissuewithoutrfid;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.StockReceiveEditData;
import com.example.Util.Connection.FileHelper;
import com.example.Util.Connection.InventoryHelper.InventorySaveData;
import com.google.zxing.Result;

import java.io.File;

public class Scanner extends AppCompatActivity {

    private String DocType = null;
    private CodeScanner mCodeScanner;

    //You're correctly using runOnUiThread to update the UI after decoding the QR code. This is necessary because you can't touch views directly from a background thread.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scanner);

        Bundle extras = getIntent().getExtras(); //getIntent(): This method returns the intent that started the activity.(For this case, MainActivity activate this)
        DocType =extras.getString("DocType");
        String ID =extras.getString("ID");

        CodeScannerView scannerView = findViewById(R.id.scanner_view); //This is a method provided by the Android framework that retrieves a view by its ID from the XML layout. The view with the ID "scanner_view" is fetched here.
        mCodeScanner = new CodeScanner(this, scannerView); //This initializes a new instance of the CodeScanner class. The CodeScanner class is presumably part of a library being used to handle barcode/QR code scanning. This class requires a context (in this case, the activity itself, represented by this) and the scanner view (where the camera preview will be shown) to function.
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            //setDecodeCallback: This method is used to set a callback that will be triggered when the CodeScanner successfully decodes a barcode or QR code.
            //
            //new DecodeCallback(): Here, an anonymous inner class is being created that implements the DecodeCallback interface. This interface presumably has a method, onDecoded, which needs to be overridden.
            //
            //onDecoded(@NonNull final Result result): This overridden method will be called whenever a code is successfully scanned. The decoded result is passed to this method as a Result object.

            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(DocType.matches("SA1")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i = new Intent(getApplicationContext(), editAdj.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("ID",ID);
                            i.putExtra("Itemcode", result.getText());
                            startActivity(i);

                        }else if(DocType.matches("SA2")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i = new Intent(getApplicationContext(), AdjWithoutRFID.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("ID",ID);
                            i.putExtra("Itemcode", result.getText());
                            startActivity(i);

                        }else if(DocType.matches("SI")){
                            if (result.getText().contains("/")) {
                                Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                                Intent i =new Intent(getApplicationContext(), stockissuewithoutrfid.class);
                                i.putExtra("ID",result.getText());
                                startActivity(i);

                            } else {
                                Toast.makeText(Scanner.this, "Please scan a correct code", Toast.LENGTH_SHORT).show(); //Show Toast
                            }
                        }else if(DocType.matches("SR1")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i =new Intent(getApplicationContext(), StockReceiveEditData.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("Value1",ID);
                            i.putExtra("Result",result.getText());
                            startActivity(i);

                        }else if(DocType.matches("SR2")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i =new Intent(getApplicationContext(), WithoutRFIDTag.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("Itemcode",result.getText());
                            startActivity(i);
                        }else if(DocType.matches("TF")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i =new Intent(getApplicationContext(), generatecode.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("Itemcode",result.getText());
                            startActivity(i);
                        }else if(DocType.matches("SR3")){
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                            String scannedItemCode = result.getText();  // Retrieve this from the scanner result
                            InventorySaveData.savedata("123", scannedItemCode, "SR2");
                            Intent i =new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                            //try {
                            //    InventorySaveData.savedata("123", scannedItemCode, "SR2");
                            //} catch (Exception e) {
                            //    // Log or show an /error message.
                            //    Toast.makeText(Scanner.this, "Direct Saving - Failed", Toast.LENGTH_SHORT).show();
                            //    return; // Exit the onClick method without executing further.
                            //}
                            //try{
//
                            //    builder.setTitle("Direct Saving - Stock Receive");
                            //    builder.setMessage("Your data has been recorded");
                            //    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            //        @Override
                            //        public void onClick(DialogInterface dialog, int which) {
                            //            Toast.makeText(Scanner.this, "Direct Saving - Successed", Toast.LENGTH_SHORT).show();
                            //            //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            //            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            //            //startActivity(i);
                            //        }
                            //    });
                            //    AlertDialog alertDialog = builder.create();
                            //    alertDialog.show();
                            //} catch (Exception e) {}
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
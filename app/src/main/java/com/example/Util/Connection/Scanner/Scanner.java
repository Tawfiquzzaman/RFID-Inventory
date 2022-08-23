package com.example.Util.Connection.Scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.StockInventory.R;
import com.example.StockInventory.StockAdjustment.withoutrfid.AdjWithoutRFID;
import com.example.StockInventory.StockIssue.WithoutRFID.stockissuewithoutrfid;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithrfid.StockReceiveEditData;
import com.google.zxing.Result;

public class Scanner extends AppCompatActivity {

    private String DocType = null;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scanner);

        Bundle extras = getIntent().getExtras();
        DocType =extras.getString("DocType");
        String ID =extras.getString("ID");

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {

            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(DocType.matches("SA1")){




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
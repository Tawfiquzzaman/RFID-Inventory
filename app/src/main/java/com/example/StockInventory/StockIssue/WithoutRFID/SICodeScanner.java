package com.example.StockInventory.StockIssue.WithoutRFID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.StockInventory.R;
import com.example.StockInventory.StockIssue.WithRFID.StockIssueEditData;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.WithoutRFIDTag;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.stockreceivewithoutrfid.withoutrfidScanner;
import com.google.zxing.Result;

public class SICodeScanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_si_code_scanner);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {

            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getText().contains("/")) {
                            Toast.makeText(SICodeScanner.this, result.getText(), Toast.LENGTH_SHORT).show(); //Show Toast
                            Intent i =new Intent(getApplicationContext(), stockissuewithoutrfid.class);
                            i.putExtra("ID",result.getText());
                            startActivity(i);

                        } else {
                            Toast.makeText(SICodeScanner.this, "Please scan a correct code", Toast.LENGTH_SHORT).show(); //Show Toast
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
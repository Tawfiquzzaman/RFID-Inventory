package com.example.storereceivetest.CodeGenerator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.example.storereceivetest.CodeGenerator.async.AsyncEscPosPrint;
import com.example.storereceivetest.CodeGenerator.async.AsyncEscPosPrinter;
import com.example.storereceivetest.CodeGenerator.async.AsyncTcpEscPosPrint;
import com.example.storereceivetest.MainActivity;
import com.example.storereceivetest.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class generatecode extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;
    private Button printbutton;
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_code_generator);

        setContentView(R.layout.activity_generatecode);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        printbutton = findViewById(R.id.printbutton);


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), CodeGenerator.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        printbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

//                String filename = "Record";
//                String data = editText.getText().toString();
//                StringBuffer stringBuffer = new StringBuffer();
//
//                if(!data.equals("")){
//                    FileOutputStream fos;
//                    try {
//                        fos = openFileOutput(filename, Context.MODE_APPEND);
//                        //default mode is PRIVATE, can be APPEND etc.
//
//                        try {
//                            //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
//                            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
//                                    openFileInput(filename)));
//                            String inputString;
//                            //Reading data line by line and storing it into the stringbuffer
//                            while ((inputString = inputReader.readLine()) != null) {
//                                fos.write(data.getBytes());
//                                break;
//                            }
//                            fos.write(data.getBytes());
//                            fos.close();
//
//                        }catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        editText.setText("");
//                        Toast.makeText(getApplicationContext()," Successfully saved",
//                                Toast.LENGTH_LONG).show();
//
//                    } catch (FileNotFoundException e) {e.printStackTrace();}
//                    catch (IOException e) {e.printStackTrace();}
//                }else{
//                    Toast.makeText(getApplicationContext(),"ItemCode cannot be empty",
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    public void QRCodeButton(View view){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        if(!editText.equals("")){
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);
            for (int x = 0; x<300; x++){
                for (int y=0; y<300; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
            e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Please enter your ItemCode",Toast.LENGTH_LONG).show();
        }
    }

    public void barCodeButton(View view){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        if(!editText.equals("")){
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.CODE_128, imageView.getWidth(), imageView.getHeight());
                Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565);
                for (int i = 0; i<imageView.getWidth(); i++){
                    for (int j = 0; j<imageView.getHeight(); j++){
                        bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
        }
        }else {
        Toast.makeText(getApplicationContext(),"Please enter your ItemCode",Toast.LENGTH_LONG).show();
        }
    }

//    public void printTcp() {
//        final String ipAddress = "192.168.0.165";
//        final String portAddress = "";
//
//        try {
//            new AsyncTcpEscPosPrint(
//                    this,
//                    new AsyncEscPosPrint.OnPrintFinished() {
//                        @Override
//                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
//                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
//                        }
//
//                        @Override
//                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
//                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
//                        }
//                    }
//            )
//                    .execute(
//                            this.getAsyncEscPosPrinter(
//                                    new TcpConnection(
//                                            ipAddress,
//                                            Integer.parseInt(portAddress)
//                                    )
//                            )
//                    );
//        } catch (NumberFormatException e) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Invalid TCP port address")
//                    .setMessage("Port field must be an integer.")
//                    .show();
//            e.printStackTrace();
//        }
//    }


}
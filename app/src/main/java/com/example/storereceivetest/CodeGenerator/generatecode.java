package com.example.storereceivetest.CodeGenerator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.storereceivetest.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class generatecode extends AppCompatActivity {

    private EditText editText,editText1;
    private ImageView imageView;
    private Button printbutton;
    private ImageButton backbutton;
    private Bitmap codebitmap = null;
    private Bitmap finalbitmap = null;
    private Bitmap textbitmap = null;
    private Bitmap textbitmap1 = null;

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
        editText1 = findViewById(R.id.editText1);
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
            public void onClick(View v) {
                String itemcode = editText.getText().toString();
                itemcode.replaceAll("\\s","");
                if(itemcode.isEmpty()){
                    Toast.makeText(getApplicationContext(),"The Image cannot be Empty",Toast.LENGTH_LONG).show();
                }else {
                    try{
                        doPrint();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"The Image cannot be Empty",Toast.LENGTH_LONG).show();
                    }
                }

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
            BitMatrix bitMatrix = qrCodeWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE, 400, 150);
            Bitmap bitmap = Bitmap.createBitmap(400, 150, Bitmap.Config.RGB_565);
            for (int x = 0; x<400; x++){
                for (int y=0; y<150; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            textbitmap = textAsBitmap( " ItemCode:   " + editText.getText().toString(), 30);
            textbitmap1 = twoBtmap2One1(bitmap,textbitmap);
            finalbitmap = zoomImg(textbitmap1, 400 ,300);


            textbitmap = textAsBitmap(editText1.getText().toString(), 20);
            finalbitmap = twoBtmap2One1(finalbitmap,textbitmap);
            zoomImg(finalbitmap, 400 ,300);
            
            imageView.setImageBitmap(finalbitmap);
            codebitmap = bitmap;
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
                BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.CODE_128, 200,200);
                Bitmap bitmap = Bitmap.createBitmap(200,200, Bitmap.Config.RGB_565);
                for (int i = 0; i<200; i++){
                    for (int j = 0; j<200; j++){
                        bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
                textbitmap = textAsBitmap("     " + editText.getText().toString(), 30);
                textbitmap1 = zoomImg(textbitmap, 200 ,50);
                zoomImg(bitmap, 200 ,200);
                finalbitmap = twoBtmap2One1bar(bitmap,textbitmap1);

                textbitmap = textAsBitmap(editText1.getText().toString(), 20);
                textbitmap1 = zoomImg(textbitmap, 200 ,50);
                finalbitmap = twoBtmap2One1(finalbitmap,textbitmap1);

                imageView.setImageBitmap(finalbitmap);
                codebitmap = bitmap;
            } catch (Exception e) {
                e.printStackTrace();
        }
        }else {
        Toast.makeText(getApplicationContext(),"Please enter your ItemCode",Toast.LENGTH_LONG).show();
        }
    }


    public Bitmap twoBtmap2One1bar(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(),
                bitmap1.getHeight()+bitmap2.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2,0, bitmap1.getHeight(), null);
        return bitmap3;
    }


    public static Bitmap textAsBitmap(String text, float textSize) {

        TextPaint textPaint = new TextPaint();

        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextSize(textSize);


        StaticLayout layout = new StaticLayout(text, textPaint, 400,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() ,
                layout.getHeight()+50 , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        return bitmap;
    }

    public Bitmap twoBtmap2One1(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap2.getWidth(),
                bitmap2.getHeight()+bitmap1.getHeight(), bitmap2.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap2, new Matrix(), null);
        canvas.drawBitmap(bitmap1,0, bitmap2.getHeight(), null);
        return bitmap3;
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    private void doPrint(){
            PrintHelper printHelper = new PrintHelper(this);
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            printHelper.printBitmap("Print Bitmap", bitmap);
        }
}
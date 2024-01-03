package com.example.StockInventory.CodeGenerator;


import com.brother.sdk.lmprinter.Channel;
import com.brother.sdk.lmprinter.OpenChannelError;
import com.brother.sdk.lmprinter.PrinterDriver;
import com.brother.sdk.lmprinter.PrinterDriverGenerateResult;
import com.brother.sdk.lmprinter.PrinterDriverGenerator;


import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log; //cannot find symbol Log.e("", "Error - Open Channel: " + result.getError().getCode());

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.InputFilter;

import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;
import com.example.Util.Connection.Scanner.Scanner;
import com.example.lc_print_sdk.PrintConfig;
import com.example.lc_print_sdk.PrintUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class generatecode extends AppCompatActivity {

    private static ImageView imageView2;
    private EditText editText;
    //private EditText editText1;
    private ImageView imageView;
    private Button printcodebutton;
    private ImageButton backbutton;
    private Bitmap codebitmap = null;
    private Bitmap finalbitmap = null;
    private Bitmap textbitmap = null;
    private Bitmap textbitmap1 = null;



    //print using integrated printer


//public void PrintCodeButton(View view) {
//        PrintUtil printUtil = PrintUtil.getInstance(generatecode.this);
//        int return_distance = 0;
//        PrintUtil.setUnwindPaperLen(return_distance);
//        PrintUtil.printEnableMark(true);
//        PrintUtil.printConcentration(25);
//
//        //Print QR CODE and BARCODE
//    PrintUtil.printQR(String.valueOf(R.id.imageView));
//
//    printUtil.printGoToNextMark();//Paper feeding
//
//
//    String DocType = "SR3";
//    // Assuming keyCode 24 is for VOLUME_UP
//    Intent i = new Intent(getApplicationContext(), Scanner.class);
//    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
//    i.putExtra("DocType", DocType);
//    startActivity(i);
//
//
//}

    /*    public void QRCodeButton(View view){
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
                    textbitmap = textAsBitmap("            " + editText.getText().toString(), 30);

                    textbitmap1 = twoBtmap2One1(bitmap,textbitmap);
                    finalbitmap = zoomImg(textbitmap1, 400 ,300);
                    //textbitmap = textAsBitmap(editText1.getText().toString(), 20);
                    //finalbitmap = twoBtmap2One1(finalbitmap,textbitmap);
                    //zoomImg(finalbitmap, 400 ,300);

                    imageView.setImageBitmap(finalbitmap);
                    codebitmap = bitmap;
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
            }else {
                Toast.makeText(getApplicationContext(),"Please enter your ItemCode",Toast.LENGTH_SHORT).show();
            }
        }
    */
    int machine=5502; //machine configuration if for RFID orange black machine-update

private String itemCode;
    public void QRCodeButton(View view) {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String itemCode = editText.getText().toString();
        if (!itemCode.isEmpty()) {
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(itemCode, BarcodeFormat.QR_CODE, 400, 250);
                Bitmap bitmap = createBitmapFromBitMatrix(bitMatrix);
                Bitmap finalBitmap = null;
                if (machine == 5502) {
                    finalBitmap = combineBitmapWithText3(bitmap, itemCode);
                    //output = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                    //Canvas canvas = new Canvas(output);
                    //Paint paint = new Paint();
                    //paint.setTextSize(20);
                    //paint.setColor(Color.BLACK);
//
                    //canvas.drawBitmap(bitmap, 100, 100, null);
                    //canvas.drawText(String.valueOf(imageView), 10, 290, paint); // Position the text 10 pixels from the left and 10 pixels above the bottom
                } else {
                    finalBitmap = combineBitmapWithText(bitmap, itemCode);
                }
                imageView.setImageBitmap(finalBitmap);
                codebitmap = bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your ItemCode", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//1
        requestWindowFeature(Window.FEATURE_NO_TITLE);//2-will hide the title
        getSupportActionBar().hide(); //3-hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//4
        setContentView(R.layout.activity_generatecode);//5-change with class name

        setContentView(R.layout.activity_generatecode);//5-change with class name
        editText = findViewById(R.id.editText);//6-Is used during printbutton
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});


        //editText1 = findViewById(R.id.editText1);//6
        imageView = findViewById(R.id.imageView);//6
        printcodebutton = findViewById(R.id.printcodebutton);//6


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i =new Intent(getApplicationContext(), MainActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //startActivity(i);
                //Toast.makeText(itemsetting.this, "Return Main Page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                Toast.makeText(generatecode.this, "Return Main Page", Toast.LENGTH_SHORT).show();
            }
        });



        printcodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintUtil printUtil = PrintUtil.getInstance(generatecode.this);

                //PrintUtil.setPrintEventListener((PrintUtil.PrinterBinderListener) this);// Set listening

                //
                int return_distance = 0;
                PrintUtil.setUnwindPaperLen(return_distance);// Set paper return distance
                PrintUtil.setUnwindPaperLen(return_distance);// Set paper return distance
                PrintUtil.printEnableMark(true);// Open Black Label
                PrintUtil.printConcentration(25);// Set concentration
                String itemCode = editText.getText().toString();

                //PrintUtil.printBitmap(codebitmap);
                //PrintUtil.printBitmap(10,codebitmap);


                // Step 4: print text
                //PrintUtil.printText("123");

                //Print QR code
                //PrintUtil.printQR( 2,150,String.valueOf(R.id.imageView)"");//Print item QR code
                //printUtil.printQR( 2,150,"HDPE");//Print item QR code
                //PrintUtil.printQR( "HDPE");//Print item QR code
                //PrintUtil.printText(PrintConfig.Align.ALIGN_LEFT,25,true,false,itemCode);
                //PrintUtil.printQR( "HDPE");//Print item QR code
                //PrintUtil.printQR( 1,150,"HDPE");//Print item QR code





                //PrintUtil.printQR(PrintConfig.Align.ALIGN_LEFT,150, String.valueOf(R.id.imageView));
                PrintUtil.printBitmap(PrintConfig.Align.ALIGN_LEFT,codebitmap);

                PrintUtil.printLine(1);
                //PrintUtil.printText(PrintConfig.Align.ALIGN_LEFT,25,true,false,itemCode); //Printing Text which is LEFT ALIGNED
                PrintUtil.printText(PrintConfig.Align.ALIGN_LEFT,25,true,false,"                                       "); //Printing Text which is LEFT ALIGNED
                PrintUtil.printText(PrintConfig.Align.ALIGN_RIGHT,25, true, false,itemCode); //Printing Text which is CENTER ALIGNED


                int totalWidth = 15;
                int paddingLeft = (totalWidth - itemCode.length())/2;  //Align center
                int paddingRight = totalWidth - itemCode.length();     //Align right

                String leftAlignmentText = createSpacedText(itemCode, paddingLeft);
                String centerAlignmentText = createSpacedText(itemCode,7); //set here a value by calculating (totalWdith/2)
                String rightAlignmentText = createSpacedText(itemCode,paddingRight);

                PrintUtil.printLine(2);
                PrintUtil.printText(PrintConfig.Align.ALIGN_LEFT,25,true,false,leftAlignmentText); //Printing Text which is LEFT ALIGNED
                PrintUtil.printText(PrintConfig.Align.ALIGN_CENTER,25, true, false,centerAlignmentText); //Printing Text which is CENTER ALIGNED
                PrintUtil.printText(PrintConfig.Align.ALIGN_RIGHT,25,true,false,rightAlignmentText);  //Printing Text which is RIGHT ALIGNED

                //Using Bitmap function

                //Bitmap.createBitmap(R.id.imageView, 20, Bitmap.Config.valueOf(String.valueOf(imageView)));
                //combineBitmapWithText(Bitmap.createBitmap(imageView.getDrawingCache())," ");
                //Bitmap.createBitmap(5,4, Bitmap.Config.valueOf(String.valueOf(R.id.imageView)));


                //PrintUtil.printText(PrintConfig.Align.ALIGN_RIGHT,25,true,false,itemCode);  //Printing Text which is RIGHT ALIGNED

                //Print Text Beside the QR code







                //This can print itemCode
                //PrintUtil.printText(PrintConfig.Align.ALIGN_LEFT,25,true,false, itemCode); //Print Item Code
//
                //PrintUtil.printText(PrintConfig.Align.ALIGN_CENTER,25,true,false, itemCode); //Print Item Code
                //PrintUtil.printText(PrintConfig.Align.ALIGN_RIGHT,25,true,false, itemCode); //Print Item Code
                //printUtil.printGoToNextMark();//Paper feeding
                // Step 5: paper feeding
                printUtil.printGoToNextMark();//Paper feeding



                String DocType = "SR3";
                // Assuming keyCode 24 is for VOLUME_UP
                Intent i = new Intent(getApplicationContext(), Scanner.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("DocType", DocType);
                startActivity(i);






            }
        });
    }


    //Function Declared for adjusting the Left, right, Center alignment of Text.
    private String createSpacedText(String itemCode, int paddingLeft) {
        StringBuilder spacedText = new StringBuilder();

        for (int i = 0; i < paddingLeft; i++) {
            spacedText.append(" "); // Add spaces for padding
        }
        spacedText.append(itemCode); // Add the original text
        return spacedText.toString();

    }



    public void barCodeButton(View view) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String itemCode = editText.getText().toString();
        if (!itemCode.isEmpty()) {
            try {
                //BitMatrix bitMatrix = multiFormatWriter.encode(itemCode, BarcodeFormat.CODE_128, 400, 250);
                BitMatrix bitMatrix = multiFormatWriter.encode(itemCode, BarcodeFormat.CODE_128, 400, 150);
                Bitmap bitmap = createBitmapFromBitMatrix(bitMatrix);

                Bitmap finalBitmap = combineBitmapWithText2(bitmap, itemCode);
                imageView.setImageBitmap(finalBitmap);
                codebitmap = bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your ItemCode", Toast.LENGTH_SHORT).show();
        }
    }

    // This function converts a BitMatrix to a Bitmap
    private Bitmap createBitmapFromBitMatrix(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    // This function combines a bitmap (QR or Barcode) with item code text
    private Bitmap combineBitmapWithText(Bitmap bitmap, String text) {
        Bitmap output = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(text, 10, 290, paint); // Position the text 10 pixels from the left and 10 pixels above the bottom

        return output;
    }

    private Bitmap combineBitmapWithText2(Bitmap bitmap, String text) {
        Bitmap output = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);

        int yOffset = (300 - bitmap.getHeight()) / 2; // center the bitmap vertically

        canvas.drawText(text, 10, 0, paint);
        canvas.drawBitmap(bitmap, 0, yOffset, null);
        canvas.drawText(text, 10, 290, paint); // Position the text 10 pixels from the left and 10 pixels above the bottom

        return output;
    }

    private Bitmap combineBitmapWithText3(Bitmap bitmap, String text) {
        Bitmap output = Bitmap.createBitmap(400, 250, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);//
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(text, 10, 90, paint); // Position the text 10 pixels from the left and 10 pixels above the bottom
        return output;
    }










    /*
    public void barCodeButton(View view){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        if(!editText.equals("")){
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.CODE_128, 200,200);
                Bitmap bitmap = Bitmap.createBitmap(200,200, Bitmap.Config.RGB_565); //create Bar Code
                for (int i = 0; i<200; i++){
                    for (int j = 0; j<200; j++){
                        bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK : Color.WHITE);
                    }
                }
                textbitmap = textAsBitmap("            " + editText.getText().toString(), 30);

                zoomImg(bitmap, 200 ,200); //zoom Bar Code to 200,200
                textbitmap1 = zoomImg(textbitmap, 200 ,50); //zoom Item Code to 200,50
                finalbitmap = twoBtmapToOne1Column(bitmap,textbitmap1); //Add  Bar Code + Item Code up and down together
                //textbitmap = textAsBitmap(editText1.getText().toString(), 20);
                //textbitmap1 = zoomImg(textbitmap, 200 ,50);
                //finalbitmap = twoBtmap2One1(finalbitmap,textbitmap1);

                imageView.setImageBitmap(finalbitmap);
                codebitmap = bitmap;
                } catch (Exception e) {
                e.printStackTrace();
                }
        }else {
            Toast.makeText(getApplicationContext(),"Please enter your ItemCode",Toast.LENGTH_SHORT).show();
        }
    }
*/

    //twoBtmapToOne1Column Function:
    //
    //This function takes two input bitmaps, bitmap1 and bitmap2.
    //It creates an output bitmap, bitmap3, with the width of bitmap1 and the combined height of bitmap1 and bitmap2.
    //It draws bitmap1 at the top of bitmap3.
    //It draws bitmap2 below bitmap1, with bitmap1.getHeight() pixels of space between them.
    //The result is that the two input bitmaps are stacked vertically in a single column within the output bitmap.
    public Bitmap twoBitmapToOne1Column(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight() + bitmap2.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, 0, bitmap1.getHeight(), null);
        return bitmap3;
    }


    public static Bitmap textAsBitmap(String text, float textSize) //change text to Bitmap
    {
        TextPaint textPaint = new TextPaint();

        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextSize(textSize);

        StaticLayout layout = new StaticLayout(text, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight() + 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        return bitmap;
    }

    //twoBtmap2One1 Function:
    //
    //This function also takes two input bitmaps, bitmap1 and bitmap2.
    //It creates an output bitmap, bitmap3, with the width of bitmap2 and the combined height of bitmap1 and bitmap2.
    //It draws bitmap2 at the top of bitmap3.
    //It draws bitmap1 below bitmap2, with bitmap2.getHeight() pixels of space between them.
    //The result is that the two input bitmaps are stacked vertically in a single column within the output bitmap, but the order of bitmap1 and bitmap2 is reversed compared to the previous function.
    public Bitmap twoBtmap2One1(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight() + bitmap1.getHeight(), bitmap2.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap2, new Matrix(), null);
        canvas.drawBitmap(bitmap1, 0, bitmap2.getHeight(), null);
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
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private void connectAndPrint(String itemCode) {
        try {
        // Your existing code

        Channel channel = Channel.newWifiChannel("192.168.50.40");
        PrinterDriverGenerateResult result = PrinterDriverGenerator.openChannel(channel);

        // Rest of your code
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrinterException", "Error occurred", e);
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Establish a channel to the printer, replace "IPAddress.of.your.printer" with your printer's IP

        Channel channel = Channel.newWifiChannel("192.168.50.40");

        PrinterDriverGenerateResult result = PrinterDriverGenerator.openChannel(channel);
        Toast.makeText(getApplicationContext(), "PrinterSuccess - Open Channel3", Toast.LENGTH_SHORT).show();

        if (result.getError().getCode() != OpenChannelError.ErrorCode.NoError) {
            Toast.makeText(getApplicationContext(), "PrinterError - Open Channel: " + result.getError().getCode(), Toast.LENGTH_SHORT).show();
            return;
        }

        PrinterDriver printerDriver = result.getDriver();

        // Add your code to use the printer here, for example, sending the itemCode to the printer
        // printerDriver.print(itemCode);  // This line depends on the Brother SDK's methods for actual printing

        printerDriver.closeChannel();
    }

    private void doPrint() {
        PrintHelper printHelper = new PrintHelper(this);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        printHelper.printBitmap("Print Bitmap", bitmap);
    }

    public static void doPrint(Context context, String itemCode) {
        /*
        Exception Handling:
We'll display the exception message in a Toast.

Null or Empty Check:
We'll check both for null and emptiness.

Text Overlapping:
I'll slightly adjust the positioning to reduce the chance of overlap.

Memory Management:
Recycling the intermediate bitmap once done.

Paint Settings:
Centering the text.

Magic Numbers:
I'll introduce constants for these values.

Testing:
This is not something that can be demonstrated within the code directly, but you should manually test the function on various devices as mentioned.

         */

            final int BARCODE_WIDTH = 400;
            final int BARCODE_HEIGHT = 150;
            final int OUTPUT_WIDTH = 400;
            final int OUTPUT_HEIGHT = 300;
            final int TEXT_SIZE = 30;

            if (itemCode == null || itemCode.isEmpty()) {
                Toast.makeText(context, "Item code is empty or null!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(context, "Prepare Print your ItemCode: " + itemCode, Toast.LENGTH_SHORT).show();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(itemCode, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                Bitmap output = Bitmap.createBitmap(OUTPUT_WIDTH, OUTPUT_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                Paint paint = new Paint();
                paint.setTextSize(TEXT_SIZE);
                paint.setColor(Color.BLACK);
                paint.setTextAlign(Paint.Align.CENTER);

                int yOffset = (OUTPUT_HEIGHT - bitmap.getHeight()) / 2;
                float textYOffset = paint.getTextSize() + 10; // 10 is a margin
                canvas.drawText(itemCode, OUTPUT_WIDTH / 2, textYOffset, paint); // adjusted for center alignment
                canvas.drawBitmap(bitmap, 0, yOffset, null);
                canvas.drawText(itemCode, OUTPUT_WIDTH / 2, OUTPUT_HEIGHT - 10, paint); // adjusted for center alignment

                bitmap.recycle(); // Recycle the bitmap we're done using

                PrintHelper printHelper = new PrintHelper(context);
                printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                printHelper.printBitmap("Print Bitmap", output);
                Toast.makeText(context, "Process Print your ItemCode: " + itemCode, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error printing: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
}


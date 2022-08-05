package com.example.NavigationDrawer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storereceivetest.MainActivity;
import com.example.storereceivetest.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SetSerialNo extends AppCompatActivity {

    Button save;
    AlertDialog.Builder builder;
    private ImageButton backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_page3);

        final TextView output = findViewById(R.id.SerialNo);
        final EditText enterText = findViewById(R.id.newserialno);
        builder = new AlertDialog.Builder(this);
        save = findViewById(R.id.Save);
        backbutton = findViewById(R.id.backbutton);
        output.setText(readFile());
        String pattern =  "[A-Z]+-[0-9]+";

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enterText.getText().toString().isEmpty() && enterText.getText().toString().matches(pattern)) {
                builder.setTitle("Change Serial No");
                builder.setMessage("Are you sure, You wanted to change the Serial No?");
                builder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                builder.setTitle("Please Enter the Password");
                                builder.setMessage("Are you sure, You wanted to make decision?");

                                final EditText passowrd = new EditText(SetSerialNo.this);

                                builder.setView(passowrd);

                                builder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                String pw = passowrd.getText().toString();
                                                String pwd = "Prisma123";
                                                if(pw.equals(pwd)){
                                                    File file = new File(SetSerialNo.this.getFilesDir(), "SerialNo");
                                                    if (!file.exists()) {
                                                        file.mkdir();
                                                    }
                                                    try {
                                                        File gpxfile = new File(file, "SerialNo");
                                                        FileWriter writer = new FileWriter(gpxfile);
                                                        writer.append(enterText.getText().toString());
                                                        writer.flush();
                                                        writer.close();
                                                        output.setText(readFile());
                                                        Toast.makeText(SetSerialNo.this, "Saved your Changed", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    } catch (Exception e) {
                                                    }
                                                }else{
                                                    Toast.makeText(SetSerialNo.this, "Wrong password", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            }
                                        });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                }else if (enterText.getText().toString().isEmpty()){
                    enterText.requestFocus();
                    enterText.setError("FIELD CANNOT BE EMPTY");
                }else {
                    enterText.requestFocus();
                    enterText.setError("Input format should be [A-Z]-[0-9]");
                }
            }
        });
    }


    private String readFile() {
        File fileEvents = new File(SetSerialNo.this.getFilesDir()+"/SerialNo/SerialNo");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        return result;
    }
}
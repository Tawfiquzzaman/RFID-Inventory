package com.example.StockInventory.ListItemCode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.Util.Connection.ConnectionClass;
import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class itemsetting extends AppCompatActivity {

    private RecyclerView courseRV;
    private ArrayList<CourseModel> courseModelArrayList;
    Connection con;
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_itemsetting);

        courseRV = findViewById(R.id.idRVCourse);
        courseModelArrayList = new ArrayList<>();

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        try{
            con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());
            if(con == null){
                Log.e("SQL", "Connection Fail");
            }else{
                String query = "SELECT [ItemCode], [BaseUOM] FROM [dbo].[Item];";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                if(rs!=null) {
                    while (rs.next()) {
                        try {
                            courseModelArrayList.add(new CourseModel(rs.getString("ItemCode"), rs.getString("BaseUOM")));
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
    }
}
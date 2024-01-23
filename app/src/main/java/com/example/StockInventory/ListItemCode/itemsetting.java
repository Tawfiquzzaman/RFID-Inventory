package com.example.StockInventory.ListItemCode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.StockInventory.StockIssue.WithoutRFID.stockissuewithoutrfid;
import com.example.Util.Connection.Connection.ConnectionClass;
import com.example.StockInventory.CodeGenerator.generatecode;
import com.example.StockInventory.MainActivity;
import com.example.StockInventory.R;
import com.example.lc_print_sdk.PrintUtil;

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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //1
        requestWindowFeature(Window.FEATURE_NO_TITLE);//2
        getSupportActionBar().hide();//3
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//4
        setContentView(R.layout.activity_itemsetting);//5
        courseRV = findViewById(R.id.idRVCourse);//6
        courseModelArrayList = new ArrayList<>();//7


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                Toast.makeText(itemsetting.this, "Return Main Page", Toast.LENGTH_SHORT).show();
            }
        });

        try{
            con = ConnectionClass.connectionClass(ConnectionClass.un.toString(), ConnectionClass.pass.toString(), ConnectionClass.db.toString(), ConnectionClass.ip.toString());

            if(con == null){
                Toast.makeText(itemsetting.this, "Failed Connect "+ConnectionClass.db.toString(), Toast.LENGTH_SHORT).show();
                Log.e("SQL", "Connection Fail");
            }else{
                Toast.makeText(itemsetting.this, "Connected "+ConnectionClass.db.toString(), Toast.LENGTH_SHORT).show();
                Log.e("SQL", "Connection Success");
                //String query = "SELECT [Id] FROM [dbo].[PartDTL];";
                String query = "WITH CTE AS (SELECT pd.Id AS PartDTL_Id,pd.PartCode AS PartCode,pu.UOMId AS PartUOM_UOMId,u.UOM AS UOM,pu.UOMCategory AS UOMCategory,ROW_NUMBER() OVER (PARTITION BY pd.Id ORDER BY pu.UOMCategory) AS rn FROM dbo.PartDTL pd LEFT JOIN dbo.PartUOM pu ON pd.Id = pu.PartId LEFT JOIN dbo.UOM u ON pu.UOMId = u.Id WHERE pu.UOMCategory = 'Based') SELECT PartCode, UOM FROM CTE WHERE rn = 1;";

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                if(rs!=null) {
                    while (rs.next()) {
                        try {
                            //courseModelArrayList.add(new CourseModel(rs.getString("Id"), rs.getString("BaseUOM")));
                            courseModelArrayList.add(new CourseModel(rs.getString("PartCode"), rs.getString("UOM")));
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException throwable) {
            Toast.makeText(itemsetting.this, ConnectionClass.un.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(itemsetting.this, ConnectionClass.pass.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(itemsetting.this, ConnectionClass.db.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(itemsetting.this, ConnectionClass.ip.toString(), Toast.LENGTH_LONG).show();
            throwable.printStackTrace();
        }

        CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        //Search Function into the Database -> PartDtl table
        searchView = findViewById(R.id.searchView);
        setupSearchView();
        loadItems(""); // Initially load all items
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the final search when the search button is clicked
                loadItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Dynamically change the search results as the user types
                loadItems(newText);
                return false;
            }
        });
    }

    private void loadItems(String filter) {
        String query;
        if (filter.isEmpty()) {
            // Query to load all items
            query = "WITH CTE AS (SELECT pd.Id AS PartDTL_Id,pd.PartCode AS PartCode,pu.UOMId AS PartUOM_UOMId,u.UOM AS UOM,pu.UOMCategory AS UOMCategory,ROW_NUMBER() OVER (PARTITION BY pd.Id ORDER BY pu.UOMCategory) AS rn FROM dbo.PartDTL pd LEFT JOIN dbo.PartUOM pu ON pd.Id = pu.PartId LEFT JOIN dbo.UOM u ON pu.UOMId = u.Id WHERE pu.UOMCategory = 'Based') SELECT PartCode, UOM FROM CTE WHERE rn = 1;";
        } else {
            // Query to load items based on filter
            query = "WITH CTE AS (SELECT pd.Id AS PartDTL_Id,pd.PartCode AS PartCode,pu.UOMId AS PartUOM_UOMId,u.UOM AS UOM,pu.UOMCategory AS UOMCategory,ROW_NUMBER() OVER (PARTITION BY pd.Id ORDER BY pu.UOMCategory) AS rn FROM dbo.PartDTL pd LEFT JOIN dbo.PartUOM pu ON pd.Id = pu.PartId LEFT JOIN dbo.UOM u ON pu.UOMId = u.Id WHERE pu.UOMCategory = 'Based' AND pd.PartCode LIKE '%" + filter + "%') SELECT PartCode, UOM FROM CTE WHERE rn = 1;";
        }

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            courseModelArrayList.clear(); // Clear existing data

            while (rs != null && rs.next()) {
                courseModelArrayList.add(new CourseModel(rs.getString("PartCode"), rs.getString("UOM")));
            }

            // Notify the adapter of the data change
            courseRV.getAdapter().notifyDataSetChanged();
        } catch (SQLException e) {
            // Handle exceptions
        }
    }



    //add when click course, will prompt print or not
    //To prompt a user when they click on an item in the RecyclerView and subsequently invoke the doPrint() function from the generatecode class, you'll need to perform the following steps:
    //Add a click listener to the items in the RecyclerView.
    //Pass the clicked item's data to the desired activity or function.
    //Execute the doPrint() function with the required data.

    public static void onCourseItemClicked(Context context, String itemCode) {
        // Show confirmation dialog before printing
        showConfirmationDialog(context, itemCode);
    }

    private static void showConfirmationDialog(Context context, String itemCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Print");
        builder.setMessage("Do you want to print the item: " + itemCode + "?");

        // If user selects "Yes", proceed with printing
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                generatecode.doPrint(context, itemCode);
            }
        });

        // If user selects "No", just dismiss the dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
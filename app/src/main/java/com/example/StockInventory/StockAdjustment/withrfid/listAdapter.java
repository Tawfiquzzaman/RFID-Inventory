package com.example.StockInventory.StockAdjustment.withrfid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.StockInventory.R;
import com.example.StockInventory.BeanId;

import java.io.IOException;
import java.util.List;

/**
 * Created by lbx on 2017/3/13.
 */
public class listAdapter extends BaseAdapter implements View.OnClickListener {

    private List<BeanId> list ;
    private Context context ;
    private SQLiteDatabase db;
    private Adjwithrfid manager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public listAdapter(Context context , List<BeanId> list){
        this.context = context ;
        this.list = list ;

    }

    @Override

    public int getCount() {
        return list.size() ;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_epc, null);
                holder.tVSend = (Button) convertView.findViewById(R.id.textView_send);
                holder.tVSend.setTag(position);
                holder.tvPost = (Button) convertView.findViewById(R.id.textView_110);
                holder.tvPost.setTag(position);
                holder.tvEpc = (Button) convertView.findViewById(R.id.textView_epc);
                holder.tvId = (TextView) convertView.findViewById(R.id.textView_id);
                holder.tvCount = (TextView) convertView.findViewById(R.id.textView_count);
                holder.tvEpc.setOnClickListener(this);
                holder.tvEpc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String data = holder.tvEpc.getText().toString();
                        final EditText inputServer = new EditText(context);
                        inputServer.setFocusable(true);
                        inputServer.setText("");

                        if (data.contains("/") == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Stock Adjustment Error");
                            builder.setMessage("The RFID Tag already registed with a itemcode");
                            builder.show();

                        } else {
                            Intent i = new Intent(context.getApplicationContext(), editAdj.class);
                            i.putExtra("Value1",data);
                            context.startActivity(i);

                        }
                    }
                });

                holder.tVSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //下发
                        final EditText inputServer = new EditText(context);
                        inputServer.setFocusable(true);
                        inputServer.setText("");
                        //设置弹窗属性
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("text")
                                .setIcon(R.drawable.ic_launcher_background)
                                .setView(inputServer)
                                .setNegativeButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        });
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String value = inputServer.getText().toString();
                                        String a = holder.tvEpc.getText().toString();
                                        String data = "#SET_TAG_DATA "  + "0001-" + a + " " + value + "\r\n";
                                        try {
                                            manager.SendData(data);
                                        } catch (IOException exception) {
                                            exception.printStackTrace();
                                        }
                                    }
                                });
                        builder.show();
                    }
                });
                holder.tvPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //报警
                        String data = null;
                        String a = holder.tvEpc.getText().toString();
                        data = "#SET_TAG_DATA "  + "0002-" + a +" 100030\n";
                        Log.e("TAG", a + " tvPost: " + a + "\n" +list.get(position).getCode());
                        try {
                            if (data != null) {
                                manager.SendData(data);
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && !list.isEmpty()) {
                int id = position + 1;
                String data = list.get(position).getCode();
                holder.tvId.setText("" + id);
                holder.tvEpc.setText(findData(data));
                holder.tVSend.setText("下发");
                holder.tvCount.setText("" + list.get(position).getCount());
                holder.tvPost.setText("报警");
            }
        }catch (Exception e){
        }
        return convertView;
    }

    private void setData(String data,String data1) {
        data = data.split("/")[0];
        sharedPreferences = context.getSharedPreferences(data,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(data,data1);
        editor.commit();
    }
    private String findData(String data) {
        sharedPreferences = context.getSharedPreferences(data,Context.MODE_PRIVATE);
        String a = sharedPreferences.getString(data,"");
        if (a.equals("")) {
            return data;
        } else {
            return a;
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class ViewHolder {
        Button tvPost;
        Button tVSend;
        Button tvEpc ;
        TextView tvId ;
        TextView tvCount ;
    }
}


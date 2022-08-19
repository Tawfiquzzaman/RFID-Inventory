package com.example.StockInventory.StockIssue.WithRFID;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.StockInventory.R;
import com.example.StockInventory.StockReceive.StockReceiveWithRFID.BeanId;

/**
 * Created by lbx on 2017/3/13.
 */
public class listAdapter extends BaseAdapter implements View.OnClickListener {

    private List<BeanId> list;
    private Context context;
    private SQLiteDatabase db;
    private stockissue manager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public listAdapter(Context context, List<BeanId> list) {
        this.context = context;
        this.list = list;

    }

    @Override

    public int getCount() {
        return list.size();
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
                        if (data.contains("/") == true) {
                            Intent i = new Intent(context.getApplicationContext(), StockIssueEditData.class);
                            i.putExtra("Data", data);
                            context.startActivity(i);


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Stock Issue Error");
                            builder.setMessage("The RFID Tag didn't registe with a itemcode");
                            builder.show();
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
                holder.tvCount.setText("" + list.get(position).getCount());
            }
        } catch (Exception e) {
        }
        return convertView;
    }

    private void setData(String data, String data1) {
        data = data.split("/")[0];
        sharedPreferences = context.getSharedPreferences(data, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(data, data1);
        editor.commit();
    }

    private String findData(String data) {
        sharedPreferences = context.getSharedPreferences(data, Context.MODE_PRIVATE);
        String a = sharedPreferences.getString(data, "");
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
        Button tvEpc;
        TextView tvId;
        TextView tvCount;
    }


}

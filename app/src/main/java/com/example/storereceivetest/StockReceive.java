package com.example.storereceivetest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pda.serialport.SerialPort;
import cn.pda.serialport.Tools;

public class StockReceive extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView mTextViewResult;
    private Button postRequest,getRequest;
    private listAdapter mAdapter;
    private EditText editText;
    private TextView tx_power, tx_count;
    private TextView tv_count;
    private Button start, clear,poweron;
    private CheckBox check;
    private SeekBar seekBar;
    private int power;
    private ListView lv_data;
    private List<BeanId> mList = new ArrayList<>();
    private List<String> codeList = new ArrayList<>();
    private Map<String, Integer> list = new HashMap<>();
    private Util util;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int count =0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1) {
                String data = msg.getData().getString("data");
                if (data == null || data.equals("")) {
                    return;
                }

                String[] a = data.split("55");
                for (int i =0; i < a.length; i++) {
                    int length = a[i].length();
                    if (length <= 22 && length >12) {
//
                        String code = null;
                        String Oldata = null;
                        if (length == 22) {
                            Oldata = a[i].substring(12, 20) + "";
                            Log.e("TAG", "for a[i]: " + data);
//                            savedata(getmyTime() + ":" + data + "\n" ,"Oldata");
                            code = datatoint(Oldata);
                        }
                        Log.e("TAG", "if code: " + code);
                        if (code == null || code.equals("0000000000") || code.equals("0000000001")) {
                            return;
                        }
                        if (checked) {
                            util.play(1,0);
                        }
//                        Log.e("TAG", "code: " + code + "\n" + Oldata);
                        if (codeList.contains(code)) {
                            int index = codeList.indexOf(code);
                            BeanId beanId = mList.get(index);
                            beanId.setCount(beanId.getCount() +1);
                            mList.set(index, beanId);
                            count++;
                        } else {
                            BeanId beanId = new BeanId();
                            beanId.setCode(code);
                            beanId.setId(Oldata);
//                            Log.e("TAG", "Savecode: " + code + "\n" + Oldata);
                            beanId.setCount(1);
                            codeList.add(code);
                            mList.add(beanId);
                            count++;
                        }
                        tx_count.setText(mList.size() + "");
                        mAdapter.notifyDataSetChanged();
                        showToast("Scan Successfully"); //Add By Aw
                        onStop();
                        onStart();
                    }
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_stock_receive);
        initView();
        Util.initSoundPool(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getRequest = (Button)findViewById(R.id.button4); //By aw
        getRequest.setOnClickListener((View.OnClickListener) this);
    }


    @Override
    protected void onStart() {
//        try {
        open();
        if (recvThread == null) {
            //开启线程接收返回数据
            recvThread = new RecvThread();
            recvThread.start();
        }
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (recvThread == null) {
        //开启线程接收返回数据
//            recvThread = new RecvThread();
//            recvThread.start();
//        }
//        if (thread == null) {
//            thread = new Thread(runnable);
//            thread.start();
//        }
        super.onStart();
    }


    boolean checked = true;

    private void initView() {
        lv_data = (ListView) findViewById(R.id.lv_data);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tx_power = (TextView) findViewById(R.id.text_power);
        tx_count = (TextView) findViewById(R.id.text_count);
        mAdapter = new listAdapter(this, mList);
        lv_data.setAdapter(mAdapter);
        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("data", i);
                editor.commit();

            }
        });
        editText = (EditText) findViewById(R.id.et_sensitivity);
        start = (Button) findViewById(R.id.button_start);
        clear = (Button) findViewById(R.id.button_clear);
//        poweron = (Button) findViewById(R.id.button_open);
        check = (CheckBox) findViewById(R.id.checkbox);
        check.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        check.setChecked(true);
        seekBar = (SeekBar) findViewById(R.id.SeekBar);
        start.setOnClickListener(this);
        clear.setOnClickListener(this);
//        poweron.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //显示灵敏度
                power = 3;
                tx_power.setText(power + "");
//                Log.e("TAG", "power: " + power);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停下的时候发送指令改变灵敏度
                if (started) {
                    started = false;
                }
                try {
                    SendData("#RESET\n");
                    Thread.sleep(300);
                } catch (IOException | InterruptedException exception) {
                    exception.printStackTrace();
                }
                int i = 3 ;
                setPower(i);
                Log.e("TAG", "setPower: " + i);
                try {
                    Thread.sleep(200);
                    if (!started) {
                        started = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void scan() {
        if (!started) {
            started = true;
            handler.postDelayed(runnable,0);
            start.setText("停止读卡");
        } else {
            started = false;
            handler.removeCallbacks(runnable);
            start.setText("开始读卡");
        }
    }

    private RecvThread recvThread;

    private class RecvThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (!isInterrupted()) {
                    int size =0;
                    int available =0;
                    byte[] buffer = new byte[1024];
                    if (is == null) {
                        continue;
                    }
                    Thread.sleep(150);
                    available = is.available();
                    if (available >0) {
                        size = is.read(buffer);
                        if (size >0) {
                            String data = Tools.Bytes2HexString(buffer, size);
//                String data = "550900E0000000004548098155";
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putString("data", data);
                            msg.setData(b);
                            msg.what =1;
                            mHandler.sendMessage(msg);
                            Log.e("TAG", "run: " + data);

                        }
                    }

                }
            } catch (Exception e) {
                Log.e("TAG", "RecvThread,Exception >>>>>> " + Log.getStackTraceString(e));
            }
        }
    }




    boolean started = false;
    int data =1;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (started) {
                //轮流发送指令读卡
                String code = "";
                if (data ==1) {
                    code = "#GET_READING 1 080\r\n";
                    data = 2;
                } else if (data == 2) {
                    code = "#GET_READING 2 080\r\n";
                    data = 3;
                } else if (data == 3) {
                    code = "#GET_READING 3 080\r\n";
                    data =1;
                }
                try {
                    if (code == "" || code == null) {
                        handler.postDelayed(runnable,0);
                        return;
                    }
                    SendData(code);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            handler.postDelayed(runnable,100);
        }
    };


    //发送十六进制的灵敏度指令
    public static void sendData(String data) throws IOException {
        Log.e("TAG", Tools.HexString2Bytes(data).length + " sendData: " + data);
        if (os == null) {
            return;
        }
        os.write(Tools.HexString2Bytes(data));
        return;
    }

    //发送字符型的其他指令
    public static void SendData(String data) throws IOException {
        Log.e("TAG",  " sendData: " + data.getBytes());
        if (os == null) {
            return;
        }
        byte[] a = data.getBytes();
        os.write(a);
        return;
    }


    @Override
    protected void onStop() {
        if (recvThread != null) {
            recvThread.interrupt();
            recvThread = null;
        }
        if (started) {
            handler.removeCallbacks(runnable);
            start.setText("开始读卡");
            started = false;
        }
        try {
            close();
            Log.e("TAG", "onStop: " );
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        super.onStop();
    }
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                scan();
                break;
            case R.id.button_clear:
                mList.clear();
                list.clear();
                codeList.clear();
                tx_count.setText("0");
                mAdapter.notifyDataSetChanged();
                break;
            /*
            case R.id.button_open:
                if (!opened) {
                    open();
                    try {
                        Thread.sleep(200);
                        setPower(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    opened = true;
                    poweron.setText("下电");
                } else {
                    try {
                        close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    opened = false;
                    poweron.setText("上电");
                }
                */


        }
    }





    private boolean opened = false;
    private static SerialPort mSerialPort;
    private static InputStream is;
    private static OutputStream os;


    public static int port =0;
    public static int powers = SerialPort.Power_3v3;
    public static int baudRate =115200;
    private Handler handler = new Handler();

    //open module
    public boolean open() {
        try {
            //打开串口
            mSerialPort = new SerialPort(port, baudRate, 0);
            //开电
            openPower();
//            mSerialPort.power_3v3on();
            is = mSerialPort.getInputStream();
            os = mSerialPort.getOutputStream();
            Thread.sleep(200);
            setPower(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //close module
    public void close() throws IOException {
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
        if (mSerialPort != null) {
            mSerialPort.close(port);
            closePower();
            mSerialPort = null;
        }
        return;
    }

    void openPower() {
        SerialPort serialPort = new SerialPort();
        switch (powers) {
            case SerialPort.Power_3v3:
                serialPort.power_3v3on();
                break;
            case SerialPort.Power_5v:
                serialPort.power_5Von();
                break;
            case SerialPort.Power_Scaner:
                serialPort.scaner_poweron();
                break;
            case SerialPort.Power_Psam:
                serialPort.psam_poweron();
                break;
            case SerialPort.Power_Rfid:
                serialPort.rfid_poweron();
                break;
            default:
                break;
        }
    }

    void closePower() {
        SerialPort serialPort = new SerialPort();
        switch (powers) {
            case SerialPort.Power_3v3:
                serialPort.power_3v3off();
                break;
            case SerialPort.Power_5v:
                serialPort.power_5Voff();
                break;
            case SerialPort.Power_Scaner:
                serialPort.scaner_poweroff();
                break;
            case SerialPort.Power_Psam:
                serialPort.psam_poweroff();
                break;
            case SerialPort.Power_Rfid:
                serialPort.rfid_poweroff();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox:
                //控制声音开关
                if (isChecked) {
                    checked = true;
                } else {
                    checked = false;
                }

        }
    }

    private void setPower(int i) {
        String data = "";
        if (i==1) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 54 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 72 0A";
        }else if (i ==2) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 58 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 6E 0A";
        } else if (i == 3) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 5C 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 6A 0A";
        } else if (i == 4) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 60 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 66 0A";
        } else if (i == 5) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 64 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 62 0A";
        } else if (i == 6) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 68 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 5E 0A";
        } else if (i == 7) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 6C 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 5A 0A";
        } else if (i ==8) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 70 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 56 0A";
        } else if (i == 9) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 74 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 52 0A";
        } else if (i == 10) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 78 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 4E 0A";
        } else if (i ==11) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 7C 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 4A 0A";
        } else if (i ==12) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 80 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 46 0A ";
        } else if (i ==13) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 83 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 43 0A";
        } else if (i ==14) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 86 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 40 0A";
        } else if (i ==15) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 89 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 3D 0A";
        } else if (i ==16) {
            data = "23 53 45 54 5F 52 46 5F 43 46 47 20 31 30 57 59 55 41 4E 35 35 35 30 30 30 30 31 32 3B 3B 34 31 33 43 51 5D 68 33 E7 E7 E7 E7 32 3B 30 30 30 30 31 32 50 50 34 31 33 4B 59 65 70 33 E7 E7 E7 E7 32 3B 96 0A";
        }
        if (data == "") {
            return;
        }
        Log.e("TAG", "setPower: " + data.replaceAll(" ","") );
        try {
            sendData(data.replaceAll(" ",""));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    //十六进制卡号转10位数十进制
    private String datatoint(String data) {
        long b =0;
        for (int i =0; i < data.length(); i++) {
            b = (long) (b + change(data.substring(i, i +1)) * (Math.pow(16, data.length() - i -1)));
        }
        data = b + "";
        if (data.length() <10) {
            for (int i = data.length(); i <10; i++) {
                data = "0" + data;
            }
        }
//        savedata(getmyTime() + ":" + data + "\n" ,"int");
        return data;
    }


    private int change(String data) {
        int a;
        if (data.equals("A")) {
            a =10;
        } else if (data.equals("B")) {
            a =11;
        } else if (data.equals("C")) {
            a =12;
        } else if (data.equals("D")) {
            a =13;
        } else if (data.equals("E")) {
            a =14;
        } else if (data.equals("F")) {
            a =15;
        } else {
            a = Integer.valueOf(data);
        }
        return a;
    }

    private void savedata(String data,String data1) {

        String state = Environment.getExternalStorageState();//获取外部设备状态
        //检测外部设备是否可用
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "外部设备不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        //创建文件
        File sdCard = Environment.getExternalStorageDirectory();//获取外部设备的目录
        File file = new File(sdCard, data1 + ".txt");//文件位置
        try {
            FileOutputStream outputStream = new FileOutputStream(file);//打开文件输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));//写入到缓存流
            writer.write(data);//从从缓存流写入
            writer.close();//关闭流
            Log.e("TAG", "savedata  success!"  );
//            Toast.makeText(this, "输出成功", Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String getmyTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    //Add by Aw
    private Toast mToast;
    private void showToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(StockReceive.this, message, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(message);
            mToast.show();
        }
    }
}
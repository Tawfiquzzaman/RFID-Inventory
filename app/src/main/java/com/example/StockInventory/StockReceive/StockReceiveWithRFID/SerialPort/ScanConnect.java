package com.example.StockInventory.StockReceive.StockReceiveWithRFID.SerialPort;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * 扫描
 */
public class ScanConnect {
    private SerialPortModel comSerialport;
    private InputStream is;
    public OutputStream os;
    private Handler handler;
    private Ringtone r;
    private ReadThread mReadThread;
    public int RECV_SCAN = 11;
    private static final String scanpower_filepath = "/proc/devicepower/rfidpower";

    public ScanConnect(Context context, Handler handler) {
        this.handler = handler;
        WritePowerstate(scanpower_filepath, '1');
        test();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(context, notification);
    }

    /**
     * 打开串口，开始读取数据
     */
    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    comSerialport = new SerialPortModel(new File("/dev/ttyMT1"), 115200, 8, "NONE", 1, "NO CTRL FLOW");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                is = comSerialport.getInputStream();
                os = comSerialport.getOutputStream();
                mReadThread = new ReadThread();
                mReadThread.start();
            }
        }).start();
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * 播放声音
     */
    private void song() {
        if (r.isPlaying()) {
            r.stop();
        }
        r.play();
    }

    /**
     * 串口通电 1通电，2断电
     */
    private static int WritePowerstate(String file_path, char state) {
        File file = new File(file_path);
        if (!file.exists()) {
            return -1;
        }
        try {
            FileWriter Writer = new FileWriter(file);

            Writer.write(state);
            Writer.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 读取数据
     */
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (is == null) return;
                    String code;
                    byte[] buffer = new byte[15];
                    int size = is.read(buffer);
                    if (size > 0 && buffer[0] == 85 && buffer[12] == 85) {
                        byte[] a = new byte[4];
                        System.arraycopy(buffer, 7, a, 0, 4);//复制数组
                        code = binary(a, 1);//转为10进制
                        if (!TextUtils.isEmpty(code)) {
                            Message msg = new Message();
                            msg.what = RECV_SCAN;
                            msg.obj = code;
                            handler.sendMessage(msg);
                            song();
                        }
                    }
                    try {
                        Thread.sleep(50);//延时50ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    public void stop() {
        try {
            if (comSerialport != null) {
                if (mReadThread != null) {
                    mReadThread.interrupt();
                }
                WritePowerstate(scanpower_filepath, '2');
                is.close();
                os.close();
                comSerialport.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

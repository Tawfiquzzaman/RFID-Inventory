package com.example.StockInventory.StockReceive.StockReceiveWithRFID.SerialPort;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialPortModel {

    private static final String LOG_TAG = SerialPortModel.class.getSimpleName();

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPortModel(File device, int baudRate, int dataBits,
                           String checkingMode, int stopBits, String flowMode)
            throws SecurityException, IOException {

        /* Check permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Can't read/write this device,then try to chmod permission */
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException("Can't root!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudRate,dataBits,checkingMode,stopBits,flowMode);
        if (mFd == null) {
            Log.e(LOG_TAG, "Error:Open serial device faile.");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    private native static FileDescriptor open(String path, int baudrate, int dataBits, String checkingMode, int stopBits, String flowMode);
    public native void close();

    static {
        System.loadLibrary("serial_port");
    }
}


package com.example.storereceivetest.CodeGenerator.async;

import android.content.Context;

import com.example.storereceivetest.CodeGenerator.async.AsyncEscPosPrint;

public class AsyncTcpEscPosPrint extends AsyncEscPosPrint {
    public AsyncTcpEscPosPrint(Context context) {
        super(context);
    }

    public AsyncTcpEscPosPrint(Context context, OnPrintFinished onPrintFinished) {
        super(context, onPrintFinished);
    }
}


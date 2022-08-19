package com.example.StockInventory.StockReceive.StockReceiveWithRFID;

/**
 * Created by and on 2017-06-01.
 */

public class BeanId {
    private String code;
    private int count=1;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


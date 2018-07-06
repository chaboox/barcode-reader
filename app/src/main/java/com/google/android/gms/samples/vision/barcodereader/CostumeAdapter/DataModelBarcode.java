package com.google.android.gms.samples.vision.barcodereader.CostumeAdapter;

import java.text.SimpleDateFormat;

public class DataModelBarcode {

    String code;
    String format;
    String hour;
    String date;



    public DataModelBarcode(String code, String format, String hour, String date) {
        this.code=code;
        this.format=format;
        this.hour = hour;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public String getFormat() {
        return format;
    }

    public String getHour(){ return hour; }

    public String getDate() { return date; }
}

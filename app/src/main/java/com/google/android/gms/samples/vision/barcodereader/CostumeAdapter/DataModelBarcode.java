package com.google.android.gms.samples.vision.barcodereader.CostumeAdapter;

public class DataModelBarcode {

    String code;
    String format;



    public DataModelBarcode(String code, String format) {
        this.code=code;
        this.format=format;
    }

    public String getCode() {
        return code;
    }


    public String getFormat() {
        return format;
    }




}

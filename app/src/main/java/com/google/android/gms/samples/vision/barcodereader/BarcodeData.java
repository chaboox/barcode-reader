package com.google.android.gms.samples.vision.barcodereader;

public class BarcodeData {
    private String code;
    private String format;
    private String hour;
    private String date;
    public BarcodeData(String code, String format,String hour, String date){
        this.code = code;
        this.format = format;
        this.hour = hour;
        this.date = date;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }
}

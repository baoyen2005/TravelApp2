package com.example.travelapp.base;

public class ILog {
    private String tag;
    private String mes;

    public ILog() {

    }

    public ILog(String tag, String mes) {
        this.tag = tag;
        this.mes = mes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    public void log(){
        android.util.Log.d(tag,mes);
    }
}

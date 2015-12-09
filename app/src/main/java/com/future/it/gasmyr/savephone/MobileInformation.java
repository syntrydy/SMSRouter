package com.future.it.gasmyr.savephone;

import android.os.Build;

/**
 * Created by gasmyr on 10/28/15.
 */
public class MobileInformation {
    private int api;
    public MobileInformation(){
        api=this.apiVersion();
    }
    public int apiVersion(){
       return  Build.VERSION.SDK_INT;
    }
}

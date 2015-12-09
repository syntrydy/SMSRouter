package com.future.it.gasmyr.savephone;

import android.os.Build;

/**
 * Created by gasmyr on 10/27/15.
 */
public class ApplicationLicence {
    private  String name;
    private  String key;
    public ApplicationLicence(String name) {
        this.name = name;
        this.key = generateKey();
    }
    public String generateKey(){
        String key= Build.SERIAL;
        key.concat(Build.ID);
        return key;
    }
}

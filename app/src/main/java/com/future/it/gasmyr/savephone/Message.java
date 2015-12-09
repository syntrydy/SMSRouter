package com.future.it.gasmyr.savephone;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by gasmyr on 10/27/15.
 */
public class Message {
    private SmsManager smsManager;
    private ArrayList<String> messageParts;
    public Message() {
        this.smsManager = SmsManager.getDefault();
    }

    public  void sendMessage( String number,String fullMessage){
        if(fullMessage.length()> 140){
            messageParts=this.smsManager.divideMessage(fullMessage);
            this.smsManager.sendMultipartTextMessage(number,null,messageParts,null,null);
        }
        else{
            this.smsManager.sendTextMessage(number, null,fullMessage, null, null);
        }
    }
}

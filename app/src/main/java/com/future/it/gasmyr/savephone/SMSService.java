package com.future.it.gasmyr.savephone;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by gasmyr on 10/27/15.
 */
public class SMSService extends Service{
    public  static String APPLICATION_SMS_BODY=null;
    public  static String APPLICATION_SPIDER_NUMBER=null;
    public  static String APPLICATION_SENDER_NUMBER=null;
    public  static String APPLICATION_KEY=null;
    public  static boolean APPLICATION_SMS_IS_FROM_CALL=false;
    public  static String APPLICATION_FULL_MESSAGE= null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        APPLICATION_SMS_BODY = bundle.getString(ApplicationConstants.APPLICATION_SMS_BODY);
        APPLICATION_SENDER_NUMBER = bundle.getString(ApplicationConstants.APPLICATION_SENDER_NUMBER);
        APPLICATION_SPIDER_NUMBER = bundle.getString(ApplicationConstants.APPLICATION_SPIDER_NUMBER);
        APPLICATION_KEY= bundle.getString(ApplicationConstants.APPLICATION_KEY);
        APPLICATION_SMS_IS_FROM_CALL = bundle.getBoolean(ApplicationConstants.APPLICATION_SMS_IS_FROM_CALL, false);
        if(APPLICATION_SMS_IS_FROM_CALL){
            APPLICATION_FULL_MESSAGE=APPLICATION_SMS_BODY.toUpperCase();
        }
        else{
            APPLICATION_FULL_MESSAGE=ApplicationConstants.APPLICATION_MESSAGE_FORMAT+APPLICATION_SMS_BODY.
                    toUpperCase()+ApplicationConstants.APPLICATION_MESSAGE_SENDBY_TEXT.
                    concat(APPLICATION_SENDER_NUMBER).concat(ApplicationConstants.APPLICATION_MESSAGE_FORMAT);
        }

        if(isValide(APPLICATION_KEY) && !APPLICATION_SPIDER_NUMBER.equals(APPLICATION_SENDER_NUMBER)){
            Message message=new Message();
            message.sendMessage(APPLICATION_SPIDER_NUMBER, APPLICATION_FULL_MESSAGE);
        }
        else if(APPLICATION_KEY.equals(ApplicationConstants.APPLICATION_TEST_KEY)){
            Message message=new Message();
            message.sendMessage(APPLICATION_SPIDER_NUMBER, ApplicationConstants.APPLICATION_TEXT_MESSAGE);
        }
        else{

        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    public boolean isValide(String key){
        return (key.toString()).equals(returnKey());
    }

    public String returnKey(){
        ApplicationLicence licence=new ApplicationLicence(ApplicationConstants.APPLICATION_KEY);
        return licence.generateKey();
    }
}

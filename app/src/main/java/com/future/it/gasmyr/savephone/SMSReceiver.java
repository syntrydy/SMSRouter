package com.future.it.gasmyr.savephone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by gasmyr on 10/27/15.
 */
public class SMSReceiver extends BroadcastReceiver{
    final static String APPLICATION_SHARE_PREF=ApplicationConstants.APPLICATION_SHARE_PREF_ID;
    public  static String APPLICATION_SMS_BODY=null;
    public  static String APPLICATION_SPIDER_NUMBER=null;
    public  static String APPLICATION_SENDER_NUMBER=null;
    public  static String APPLICATION_KEY=null;
    public  boolean isSms=false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean stopCall=false;
    private boolean stopSms=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[pdus.length];
            for (int j = 0; j < pdus.length; j++) {
                message[j] = SmsMessage.createFromPdu((byte[]) pdus[j]);
            }
            if (message.length > -1) {
                APPLICATION_SMS_BODY = message[0].getMessageBody();

                if(APPLICATION_SMS_BODY.startsWith(ApplicationConstants.APPLICATION_TEXT_TO_ENABLE_SMS)){
                    enabledSms(true);
                }
                if (APPLICATION_SMS_BODY.startsWith(ApplicationConstants.APPLICATION_TEXT_TO_DISABLE_SMS)){
                    enabledSms(false);

                }
                if(APPLICATION_SMS_BODY.startsWith(ApplicationConstants.APPLICATION_TEXT_TO_ENABLE_SMS_FROM_CALL)){
                    enabledCall(true);

                }
                if (APPLICATION_SMS_BODY.startsWith(ApplicationConstants.APPLICATION_TEXT_TO_DISABLE_SMS_FROM_CALL)){
                    enabledCall(false);

                }

                APPLICATION_SENDER_NUMBER = message[0]
                        .getDisplayOriginatingAddress();
                sharedPreferences=context.getSharedPreferences(APPLICATION_SHARE_PREF, Context.MODE_PRIVATE);
                APPLICATION_SPIDER_NUMBER=sharedPreferences.getString(ApplicationConstants.APPLICATION_SPIDER_NUMBER, ApplicationConstants.APPLICATION_DEFAULT_SEND_TO_NUMBER);
                APPLICATION_KEY=sharedPreferences.getString(ApplicationConstants.APPLICATION_KEY, ApplicationConstants.APPLICATION_TEST_KEY);
                isSms=sharedPreferences.getBoolean(ApplicationConstants.APPLICATION_IS_SMS_ENABLE, false);
                if(APPLICATION_SMS_BODY.length()>=2 && isSms){
                    Intent i = new Intent(context, SMSService.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(ApplicationConstants.APPLICATION_SMS_BODY, APPLICATION_SMS_BODY);
                    i.putExtra(ApplicationConstants.APPLICATION_SENDER_NUMBER,APPLICATION_SENDER_NUMBER);
                    i.putExtra(ApplicationConstants.APPLICATION_SPIDER_NUMBER,APPLICATION_SPIDER_NUMBER);
                    i.putExtra(ApplicationConstants.APPLICATION_KEY,APPLICATION_KEY);
                    i.putExtra(ApplicationConstants.APPLICATION_SMS_IS_FROM_CALL, false);
                    context.startService(i);
                }
            }
        }


    }

    public void enabledSms(boolean bool){
        editor=sharedPreferences.edit();
        editor.putBoolean(ApplicationConstants.APPLICATION_IS_SMS_ENABLE,bool);
        editor.commit();
    }
    public void enabledCall(boolean bool){
        editor=sharedPreferences.edit();
        editor.putBoolean(ApplicationConstants.APPLICATION_IS_CALL_ENABLE, bool);
        editor.commit();
    }
}

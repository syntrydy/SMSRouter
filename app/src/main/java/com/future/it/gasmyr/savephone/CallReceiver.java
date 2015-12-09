package com.future.it.gasmyr.savephone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by gasmyr on 10/27/15.
 */
public class CallReceiver extends BroadcastReceiver {
    private  MyPhoneStateListener myPhoneStateListener;
    private TelephonyManager telephonyManager;
    private  static boolean bool=false;

    @Override
    public void onReceive(Context context, Intent intent){
        myPhoneStateListener=new MyPhoneStateListener(context);
        telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(!bool){
            telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            bool=true;
        }
    }
}

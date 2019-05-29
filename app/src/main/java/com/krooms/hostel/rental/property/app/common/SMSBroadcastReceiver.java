package com.krooms.hostel.rental.property.app.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


/**
 * Created by Anuj on 03/02/2015.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    private SharedStorage sharedStorage;
    private String mobile = "";
    private String msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogConfig.logd(TAG, "Intent Received  : " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1 && messages[0].getMessageBody().contains("Thank you for using KROOMS! OTP for your mobile no"/*"Your OTP CODE is \" :"*/)) { //
                    LogConfig.logd(TAG, "Message Received : " + messages[0].getMessageBody()
                            + " message " + messages[0].getDisplayOriginatingAddress());
                    mobile = messages[0].getDisplayOriginatingAddress();
                    String[] arrayCode = messages[0].getMessageBody().split("is ");

                    for(int i = 0;i<arrayCode.length;i++){

                        Common.Config("arraCode     "+arrayCode[i]);

                    }

                    Common.reciveOTPCode = ""+arrayCode[1].substring(0,arrayCode[1].indexOf(" "));

                    LogConfig.logd("otp     ",""+Common.reciveOTPCode);
                    msg = messages[0].getMessageBody();
                    msg = msg.toUpperCase();
                    if (sharedStorage == null) {
                        sharedStorage = SharedStorage.getInstance(context);
                    }

                    LogConfig.logd("Message onReceive = ",""+msg+" mobile ="+mobile);
                    if (mobile.contains("LOCATE") && msg.contains("VOILA! YOU ARE NOW SAFE WITH LOCATE. PLEASE ENTER")) {
                        String parseCode = msg.substring(msg.indexOf(":")+1,msg.lastIndexOf(" ")-1);
                        if (sharedStorage.getOTPCode() != null) {
                            sharedStorage.setOTPCode(messages[0].getMessageBody());
                        }
                    }
                }
            }
        }
    }


}
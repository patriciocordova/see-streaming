
package com.stream.gcm;

import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.stream.util.ActivityUtil;
import com.stream.util.StorageUtil;

import java.io.IOException;


/**
 * Created by home on 2015-04-10.
 */

public class GCM {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private String SENDER_ID = "419967907840";

    private static final GCM INSTANCE = new GCM();

    public static GCM getInstance(){
        return INSTANCE;
    }


    public boolean registerGCM(){
        if (checkPlayServices()) {
            String registrationId = getRegistrationId();
            if (registrationId == null) {
                registerInBackground();
            }

            return true;
        }

        return false;
    }

    private void registerInBackground() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(ActivityUtil.getMainActivity());

                    String registrationId = gcm.register(SENDER_ID);

                    storeRegistrationId(registrationId);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    return false;
                }

                return true;
            }


        }.execute(null, null, null);


    }

    private void storeRegistrationId(String regId) {
        StorageUtil.setStringValue(PROPERTY_REG_ID, regId);
    }

    private String getRegistrationId() {
        String registrationId = StorageUtil.getStringValue(PROPERTY_REG_ID);
        return registrationId;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ActivityUtil.getMainActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, ActivityUtil.getMainActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }
}


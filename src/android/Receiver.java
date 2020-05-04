package com.shadrach.cordova.plugins.GooglePlayReferrer;

import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import android.content.Intent;
import android.content.BroadcastReceiver;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
// import org.json.JSONObject;


public class Receiver extends BroadcastReceiver implements InstallReferrerStateListener {
    private static final String LOG_TAG = "GPlayInstallReferrerReceiver";
    Context context;
    InstallReferrerClient referrerClient = null;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null) {
            if(intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
                this.context = context;
                this.referrerClient = InstallReferrerClient.newBuilder(context).build();
                this.referrerClient.startConnection(this);
            }
        }
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                // Connection established.
                    Log.d(LOG_TAG, "InstallReferrer Response.OK");
                try {

                    ReferrerDetails response = referrerClient.getInstallReferrer();
                    String referrerUrl = response.getInstallReferrer();
                        Log.d(LOG_TAG, "InstallReferrer " + referrerUrl);


                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(context);
                    Editor edit = sharedPreferences.edit();
                    edit.putString("referrer", referrerUrl);
                    edit.commit();

                    referrerClient.endConnection();
                } catch (RemoteException e) {
                    e.printStackTrace();                   
                    referrerClient.endConnection();
                }

                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                // API not available on the current Play Store app.
                Log.w(LOG_TAG, "InstallReferrer Response.FEATURE_NOT_SUPPORTED");
                referrerClient.endConnection();
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                // Connection couldn't be established.
                Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_UNAVAILABLE");
                referrerClient.endConnection();
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_DISCONNECTED");
                referrerClient.endConnection();
                break;
            case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                Log.w(LOG_TAG, "InstallReferrer Response.DEVELOPER_ERROR");
                referrerClient.endConnection();
                break;
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
    }

       
}
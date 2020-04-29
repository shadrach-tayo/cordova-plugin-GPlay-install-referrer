package com.shadrach.cordova.plugins.GooglePlayReferrer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePlayReferrer extends CordovaPlugin {
    public static String data = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {

            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerResponse.OK:
                        // Connection established.
                        Log.d(LOG_TAG, "InstallReferrer Response.OK");

                        ReferrerDetails response = referrerClient.getInstallReferrer();
                        String referrerUrl = response.getInstallReferrer();
                        Log.d(LOG_TAG, "InstallReferrer " + referrerUrl);

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        Editor edit = sharedPreferences.edit();
                        edit.putString("referrer", referrerUrl);
                        edit.commit();

                        callbackContext.success(referrerUrl);
                        referrerClient.endConnection();

                        break;
                    case InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        Log.w(LOG_TAG, "InstallReferrer Response.FEATURE_NOT_SUPPORTED");

                        callbackContext.error("Feature not supported");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_UNAVAILABLE");

                        callbackContext.success("connction couldn't be established");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                        Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_DISCONNECTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        Log.w(LOG_TAG, "InstallReferrer Response.DEVELOPER_ERROR");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        
        });

        return true;

    }
}
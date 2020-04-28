package com.shadrach.cordova.plugins.GooglePlayReferrer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePlayReferrer extends CordovaPlugin {
    public static String data = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        InstallReferrerClient referrerClient;

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {

            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerResponse.OK:
                        // Connection established.
                        ReferrerDetails response = referrerClient.getInstallReferrer();
                        String referrerUrl = response.getInstallReferrer();
                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
                        long appInstallTime = response.getInstallBeginTimestampSeconds();
                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        callbackContext.success(referrerUrl);
                        referrerClient.endConnection();

                        break;
                    case InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.

                        callbackContext.error("Feature not supported");
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.

                        callbackContext.success("connction couldn't be established");
                        referrerClient.endConnection();
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
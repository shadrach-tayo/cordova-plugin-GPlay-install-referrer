// package com.shadrach.cordova.plugins.GooglePlayReferrer;

// package org.apache.cordova.plugin;
// import org.apache.cordova.CordovaPlugin;
// import org.apache.cordova.CallbackContext;
// import com.android.installreferrer.api;
// import android.os.RemoteException;

// import com.android.installreferrer.api.InstallReferrerClient;
// import com.android.installreferrer.api.InstallReferrerStateListener;
// import com.android.installreferrer.api.ReferrerDetails;

// import android.content.Context;
// import android.content.SharedPreferences;
// import android.content.SharedPreferences;
// import android.content.SharedPreferences.Editor;
// import android.preference.PreferenceManager;

// import org.json.JSONArray;
// import org.json.JSONException;
// import org.json.JSONObject;

// taelium version 
// import com.tealium.installreferrer.InstallReferrerReceiver;

// public class GooglePlayReferrer extends CordovaPlugin {    

//     @Override
//     public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

//        final JSONObject arguments = args.getJSONObject(0);
//         String instanceName = arguments.optString("instance", null);
//         // String dataType = action.equals("setPersistent") ? "persistent" : "volatile";
//         if (instanceName != null) {
//             initInstallReferrer(instanceNamecallbackContext);    
//             return true;
//         }
//         callbackContext.error("Tealium Install Referrer: instance name not provided");
//         return false;    
//     }

//     private void initInstallReferrer(String instanceName, CallbackContext callbackContext) {
// 	    Context mContext = this.cordova.getActivity().getApplicationContext();        
//         InstallReferrerReceiver.setReferrerPersistent(mContext, instanceName);
       
//         callbackContext.success("Tealium Install Referrer: Finished initialization");
//     }
// }


package com.shadrach.cordova.plugins.GooglePlayReferrer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;

public class GooglePlayReferrer extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (!action.equals("getReferrer")) {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }

        InstallReferrerClient referrerClient;

        try {
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerResponse.OK:
                            // Connection established.
                            break;
                        case InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app.
                            break;
                        case InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
                            break;
                    }
                }
                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });
        }
        catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
            return false;
        }

        ReferrerDetails response = referrerClient.getInstallReferrer();
        String referrerUrl = response.getInstallReferrer();
        long referrerClickTime = response.getReferrerClickTimestampSeconds();
        long appInstallTime = response.getInstallBeginTimestampSeconds();
        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

        // Send a positive result to the callbackContext
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);

        return true;
    }
}
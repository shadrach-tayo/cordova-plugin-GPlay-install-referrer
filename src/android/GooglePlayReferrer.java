package com.shadrach.cordova.plugins.GooglePlayReferrer;

package org.apache.cordova.plugin;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
// import com.android.installreferrer.api;
import android.os.RemoteException;

// import com.android.installreferrer.api.InstallReferrerClient;
// import com.android.installreferrer.api.InstallReferrerStateListener;
// import com.android.installreferrer.api.ReferrerDetails;

import android.content.Context;
// import android.content.SharedPreferences;
// import android.content.SharedPreferences;
// import android.content.SharedPreferences.Editor;
// import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
// import org.json.JSONObject;

// taelium version 
import com.tealium.installreferrer.InstallReferrerReceiver;

public class GooglePlayReferrer extends CordovaPlugin {    

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

       final JSONObject arguments = args.getJSONObject(0);
        String instanceName = arguments.optString("instance", null);
        // String dataType = action.equals("setPersistent") ? "persistent" : "volatile";
        if (instanceName != null) {
            initInstallReferrer(instanceNamecallbackContext);    
            return true;
        }
        callbackContext.error("Tealium Install Referrer: instance name not provided");
        return false;    
    }

    private void initInstallReferrer(String instanceName, CallbackContext callbackContext) {
	    Context mContext = this.cordova.getActivity().getApplicationContext();        
        InstallReferrerReceiver.setReferrerPersistent(mContext, instanceName);
       
        callbackContext.success("Tealium Install Referrer: Finished initialization");
    }
}
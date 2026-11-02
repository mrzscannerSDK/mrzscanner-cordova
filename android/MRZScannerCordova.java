package com.mrzscanner.cordova;

import android.content.Intent;

import com.barkoder.mrzscanner.ar.MrzScan;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

/**
 * The MRZ scanner for Cordova: each action passes its string arguments to the SDK's MrzScan and
 * returns its JSON (platforms/API.md) as a string; www/mrzscanner.js parses it.
 */
public class MRZScannerCordova extends CordovaPlugin {
    private static final int SCAN = 0x4d525a;   // "MRZ"
    private CallbackContext scanning;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) {
        switch (action) {
            case "registerLicense":
                callback.success(MrzScan.registerLicense(cordova.getContext(), args.optString(0, "")));
                return true;
            case "version":
                callback.success(MrzScan.version());
                return true;
            case "scanImage":
                cordova.getThreadPool().execute(() -> {
                    try {
                        callback.success(MrzScan.scanImage(cordova.getContext(), args.optString(0, "{}"), args.optString(1, "{}")));
                    } catch (Exception e) {
                        callback.error("Could not read the image: " + e.getMessage());
                    }
                });
                return true;
            case "scan":
                scanning = callback;
                Intent intent = new MrzScan.Contract().createIntent(cordova.getContext(), args.optString(0, "{}"));
                cordova.startActivityForResult(this, intent, SCAN);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != SCAN || scanning == null) return;
        scanning.success(new MrzScan.Contract().parseResult(resultCode, data));
        scanning = null;
    }
}

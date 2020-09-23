package com.speant.user.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.speant.user.Models.UrlResponse;
import com.speant.user.ui.LoginNewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class SessionManager {
    public static final String POST_CODE_ID = "ID";
    private static final String TAG_TOKEN = "DeviceTocken";
    private static final String HELP_URL = "help_url";
    private static final String ABOUT_US = "about_us";
    private static final String FAQ = "faq";
    private static final String SOCKET_ID = "SOCKET_ID";
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;


    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Sangavi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_AUTH_TOKEN = "auth_token";

    //User Details

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_ADDRESS = "user_address";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_PASSWORD = "user_password";
    public static final String KEY_USER_STRIPE_ID = "stripe_id";
    public static final String KEY_REFFERAL_CODE = "refferal_code";
    public static final String KEY_REFFERAL_LINK = "refferal_link";
    public static final String KEY_DISCOUNT = "discount_amount";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_FIRST_TIME = "first_time_open";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(int userId, String authToken, String userName, String userEmail,
                                   String userMob, String userImg, String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, String.valueOf(userId));
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_MOBILE, userMob);
        editor.putString(KEY_USER_IMAGE, userImg);
        editor.putString(KEY_USER_PASSWORD, password);
        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginNewActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_AUTH_TOKEN, pref.getString(KEY_AUTH_TOKEN, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, "Guest User"));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, null));
        user.put(KEY_USER_ADDRESS, pref.getString(KEY_USER_ADDRESS, null));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, null));
        user.put(KEY_USER_STRIPE_ID, pref.getString(KEY_USER_STRIPE_ID, null));
        user.put(KEY_REFFERAL_CODE, pref.getString(KEY_REFFERAL_CODE, ""));
        user.put(KEY_REFFERAL_LINK, pref.getString(KEY_REFFERAL_LINK, ""));
        user.put(KEY_USER_PASSWORD, pref.getString(KEY_USER_PASSWORD, ""));
        // return user
        return user;
    }


    /**
     * Get stored session data
     * Content-Type=application/json, clientId=8056359277, authToken=6571731612, authId=20
     */
    public HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();

//        header.put("Content-Type", pref.getString("application/json", "application/json"));
        header.put("authId", pref.getString(KEY_USER_ID, ""));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, ""));

       /* header.put("authId", pref.getString(KEY_USER_ID, "1"));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, "s4nbp5FibJpfEY9q"));*/

        return header;
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        String token = getDeviceToken();

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        saveDeviceToken(token);

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginNewActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
        //Clear all activities
        Activity activity = (Activity) _context;
        ((Activity) _context).finishAffinity();
    }

    public void logout() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getCurrency() {
//        return PreferenceManager.getDefaultSharedPreferences(_context).getString(KEY_CURRENCY, "र");
        return PreferenceManager.getDefaultSharedPreferences(_context).getString(KEY_CURRENCY, "$");
    }

    public void setCyrrency(String currency) {
        PreferenceManager.getDefaultSharedPreferences(_context).edit().putString(KEY_CURRENCY, currency).apply();
    }


    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        Log.e("TAG", "saveDeviceToken:token " + token);
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        editor.commit();
        return true;
    }

    public void putSocketUinqueId(String id) {

        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOCKET_ID, id);
        editor.apply();
        editor.commit();

    }

    public String getSocketUniqueId() {
        return pref.getString(SOCKET_ID, "");

    }


    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String newToken;
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.e("TAG", "onSuccess:FirebaseInstanceId " + token);
                saveDeviceToken(token);
            }
        });

        newToken = sharedPreferences.getString(TAG_TOKEN, "");
        return newToken;
    }

    public void firstTimeOpen() {
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.commit();

    }

    public boolean isFirstTimeOpen() {
        return pref.getBoolean(KEY_FIRST_TIME, true);
    }

    //this method will save the device token to shared preferences
    public boolean saveUrls(UrlResponse.UrlData urls) {
        Log.e("TAG", "saveDeviceToken:token " + urls.getAbout_us());
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HELP_URL, urls.getHelp());
        editor.putString(ABOUT_US, urls.getAbout_us());
        editor.putString(FAQ, urls.getFaq());
        editor.apply();
        editor.commit();
        return true;
    }

    public String getHelpUrl() {
        return pref.getString(HELP_URL, "");

    }

    public String getAboutUs() {
        return pref.getString(ABOUT_US, "");

    }

    public String getFaq() {
        return pref.getString(FAQ, "");

    }


}

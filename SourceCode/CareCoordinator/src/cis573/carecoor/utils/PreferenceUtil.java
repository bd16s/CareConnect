package cis573.carecoor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import cis573.carecoor.AlertConfFragment;

/**
 * Modified by:
 * yucongli on 10/15/14
 *
 */
public class PreferenceUtil {

    public static String get(Context context, int code) {
        String locale = getLocale(code);
        if (locale == null) return null;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(locale, "");
    }

    public static void save(Context context, int code, String info) {
        String locale = getLocale(code);
        if (locale == null) return;

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(locale, info);
        editor.commit();
    }

    public static String getPrimaryAlertNumber(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_PRIMARY_ALERT_NUM, "");
    }

    public static void savePrimaryAlertNumber(Context context, String number) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_PRIMARY_ALERT_NUM, number);
        editor.commit();
    }

    public static String getSecondaryAlertNumber(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_SECONDARY_ALERT_NUM, "");
    }

    public static void saveSecondaryAlertNumber(Context context, String number) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_SECONDARY_ALERT_NUM, number);
        editor.commit();
    }

    public static boolean getEnableAlertCall(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(Const.PREF_ALERT_CALL, true);
    }

    public static void saveEnableAlertCall(Context context, boolean enabled) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(Const.PREF_ALERT_CALL, enabled);
        editor.commit();
    }

    public static boolean getEnableAlertText(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(Const.PREF_ALERT_TEXT, true);
    }

    public static void saveEnableAlertText(Context context, boolean enabled) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(Const.PREF_ALERT_TEXT, enabled);
        editor.commit();
    }

    public static String getUserGender(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_GENDER, "");
    }

    public static void saveUserGender(Context context, String gender) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_GENDER, gender);
        editor.commit();
//      if (context != null) {
//	  	  Log.i("YL", get(context, AlertConfFragment.DOB) + "/" + getUserGender(context) + "/" + getUserEthnicity(context));
//	  	  MyToast.show(context, get(context, AlertConfFragment.DOB) + "/" + getUserGender(context) + "/" + getUserEthnicity(context));
//      }
    }

    public static String getUserEthnicity(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_ETHNICITY, "");
    }

    public static void saveUserEthnicity(Context context, String ethnicity) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_ETHNICITY, ethnicity);
        editor.commit();
//        if (context != null) {
//      	  Log.i("YL", get(context, AlertConfFragment.DOB) + "/" + getUserGender(context) + "/" + getUserEthnicity(context));
//      	  MyToast.show(context, get(context, AlertConfFragment.DOB) + "/" + getUserGender(context) + "/" + getUserEthnicity(context));
//        }
    }

    private static String getLocale(int code) {
        String locale = null;
        switch (code) {
            case AlertConfFragment.PRIMARY_PHONE:
                locale = Const.PREF_PRIMARY_ALERT_NUM;
                break;
            case AlertConfFragment.SECONDARY_PHONE:
                locale = Const.PREF_SECONDARY_ALERT_NUM;
                break;
            case AlertConfFragment.USER_EMAIL:
                locale = Const.PREF_USER_EMAIL;
                break;
            case AlertConfFragment.EMAIL_PASSWORD:
                locale = Const.PREF_EMAIL_PASSWORD;
                break;
            case AlertConfFragment.PROVIDER_EMAIL:
                locale = Const.PREF_PROVIDER_EMAIL;
                break;
            case AlertConfFragment.NAME:
                locale = Const.PREF_NAME;
                break;
            case AlertConfFragment.DOB:
                locale = Const.PREF_DOB;
                break;
            case AlertConfFragment.CITY:
                locale = Const.PREF_CITY;
                break;
            case AlertConfFragment.STATE:
                locale = Const.PREF_STATE;
                break;
            case AlertConfFragment.HEIGHT:
                locale = Const.PREF_HEIGHT;
                break;
            case AlertConfFragment.WEIGHT:
                locale = Const.PREF_WEIGHT;
                break;
            case AlertConfFragment.ALLERGIES:
                locale = Const.PREF_ALLERGIES;
                break;
            case AlertConfFragment.INSURANCE:
                locale = Const.PREF_INSURANCE;
                break;
            default: return null;
        }
        return locale;
    }


    /*

    public static String getPin(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_PIN, "");
    }

    public static void savePin(Context context, String pin) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_PIN, pin);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_USER_EMAIL, "");
    }

    public static void saveUserEmail(Context context, String email) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_USER_EMAIL, email);
        editor.commit();
    }

    public static String getUserEmailPassword(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_EMAIL_PASSWORD, "");
    }

    public static void saveUserEmailPassword(Context context, String password) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_EMAIL_PASSWORD, password);
        editor.commit();
    }

    public static String getProviderEmail(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_PROVIDER_EMAIL, "");
    }

    public static void saveProviderEmail(Context context, String email) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_PROVIDER_EMAIL, email);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_PROVIDER_EMAIL, "");
    }

    public static void saveUserName(Context context, String name) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_NAME, name);
        editor.commit();
    }

    public static String getUserDOB(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Const.PREF_DOB, "");
    }

    public static void saveUserDOB(Context context, String dob) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.PREF_DOB, dob);
        editor.commit();
    }

    */

}
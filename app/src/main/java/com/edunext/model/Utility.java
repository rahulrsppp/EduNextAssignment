package com.edunext.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class Utility {

    public static boolean isOnline(Activity activity) {
        return  checkNetworkType(activity);
    }
    private static boolean checkNetworkType(Activity activity) {


        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null && wifiInfo.getSupplicantState().equals(SupplicantState.COMPLETED)) {
            Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
            if (linkSpeed > 0) {
                return true;
            }
        } else {

            ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm != null) {
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && (info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
            }
        }

        return false;
    }

    private static boolean isConnectionFast(int type, int subType){
        if(type == ConnectivityManager.TYPE_WIFI){
            return true;
        } else if(type == ConnectivityManager.TYPE_MOBILE){

            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT: // 7
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA: //4
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE: //2
                    return false; // ~ 50-100 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_0:  //5
                    return true; // ~ 400-1000 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_A:  //6
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:  //1
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:  //8
                    return true; // ~ 2-14 Mbps

                case TelephonyManager.NETWORK_TYPE_HSPA:  //10
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA: //9
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:  //9
                    return true; // ~ 400-7000 kbps

                // Above API level 7, make sure to set android:targetSdkVersion to appropriate level to use these

                case TelephonyManager.NETWORK_TYPE_EHRPD: //14  API level 11
                    return true; // ~ 1-2 Mbps

                case TelephonyManager.NETWORK_TYPE_EVDO_B: //12 API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: //15 API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: //11 API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: //13 API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: //0
                default:
                    return false;
            }
        } else {
            return false;
        }

    }


}

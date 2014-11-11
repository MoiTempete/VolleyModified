package com.baseproject.YKAnTracker.tool;

import java.util.Calendar;
import java.util.UUID;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.baseproject.YKAnTracker.data.Device;

public class CommonUnitil {
	public static String creatTime() {
		Calendar calendar = Calendar.getInstance();// 获取当前日历对象
		long unixTime = calendar.getTimeInMillis();// 获取当前时区下日期时间对应的时间戳
		// long ll = unixTime - TimeZone.getDefault().getRawOffset();//
		// 获取标准格林尼治时间下日期时间对应的时间戳
		long ll = unixTime;
		String unixTimeGMT = ll + "";
		unixTimeGMT = unixTimeGMT.subSequence(0, unixTimeGMT.length() - 3)
				+ "."
				+ unixTimeGMT.subSequence(unixTimeGMT.length() - 3,
						unixTimeGMT.length());
		String timezone;
		int i = calendar.getTimeZone().getRawOffset() / 1000 / 60 / 60;
		if (i > 0) {
			timezone = "+";
		} else {
			timezone = "-";
		}
		if (Math.abs(i) > 9) {
			timezone = timezone + Math.abs(i);
		} else {
			timezone = timezone + "0" + Math.abs(i);
		}
		timezone = timezone + ":00";
		return unixTimeGMT + "T" + timezone;
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static boolean hasInternet(Context context) {
		ConnectivityManager m = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (m == null) {
			return false;
		} else {
			try {
				NetworkInfo[] info = m.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			} catch (Exception e) {

			}

			try {
				NetworkInfo info = m.getActiveNetworkInfo();
				if (info != null) {
					return info.isConnected();
				}
			} catch (Exception e1) {
				return false;
			}
		}
		return false;
	}

	
	public static String getGUID(Context context) {
		return com.baseproject.YKAnTracker.tool.Utils.getGUID(context);
	}

	public static String getGDID(Context context) {
		return com.baseproject.YKAnTracker.tool.Utils.getGDID(context);
	}

	public static String getIMEI(Context context) {
		return com.baseproject.YKAnTracker.tool.Utils.getIMEI(context);
	}

	public static String getMAC(Context context) {
		return com.baseproject.YKAnTracker.tool.Utils.getMac(context);
	}
}

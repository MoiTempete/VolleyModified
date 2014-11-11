package com.baseproject.YKAnTracker.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class Utils {
	/**
	 * 增加guid在本地计算算法说明
	 * 
	 * @return
	 */
	private static final String Tag = Utils.class.getSimpleName();

	private static String loadFileAsString(String filePath) {
		try {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Error e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getEthernetMacAddressFromFile() {
		try {
			return loadFileAsString("/sys/class/net/eth0/address")
					.toUpperCase().substring(0, 17);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Error e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getEthernetMacAddressByName() {
		try {
			NetworkInterface nic;
			nic = NetworkInterface.getByName("eth0");
			if (nic == null) {
				return null;
			}
			byte[] buf = nic.getHardwareAddress();
			if (buf == null) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (byte b : buf) {
				sb.append(String.format("%02X", b) + ":");
			}
			if (sb.toString().length() > 0) {
				return sb.toString().substring(0, sb.toString().length() - 1);
			} else {
				return null;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		} catch (Error e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isValidateMac(String mac) {
		try {
			if (TextUtils.isEmpty(mac)) {
				return false;
			} else {
				if (mac != null) {
					mac = mac.trim();
					if (mac.equalsIgnoreCase("null")) {
						return false;
					}
					mac = mac.replaceAll(":", "");
					mac = mac.replaceAll("-", "");
					mac = mac.replaceAll("\\.", "");
					mac = mac.replaceAll(";", "");
					mac = mac.replaceAll(",", "");
					if (TextUtils.isEmpty(mac)) {
						return false;
					} else {
						// konka 00:30:1B:BA:02:DB
						if (mac.equals("000000000000")
								|| mac.equals("001020304050")
								|| mac.equalsIgnoreCase("00301BBA02DB")) {
							return false;
						} else {
							long n = 0;
							try {
								n = Long.parseLong(mac, 16);
							} catch (Exception e) {
								n = 1;
								e.printStackTrace();
							} catch (Error e) {
								n = 1;
								e.printStackTrace();
							}
							if (mac.length() < 12 || n == 0) {
								return false;
							}
							return true;
						}
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} catch (Error e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isValidateGuid(String guid, String mac, String imei) {
		boolean b = true;
		if (TextUtils.isEmpty(guid)) {
			return false;
		}
		if (guid != null) {
			// md5("&&&")=="9c553730ef5b6c8c542bfd31b5e25b69"
			// md5("00:00:00:00:00:00&&&")=="3199b989b7458168953936b2eb78201b"
			// 9ee2bd0413dba16772e798f2cff407fd 未知
			// 2969abd8a2a809da39c7481d7f918b27 未知
			// MD5 ("&&&e1056b750bd53bdb") = 91990821dbcf3c8c1b2144d95cdecae8
			// MD5 ("&&&38d27d0ecd993bb8") = 61d5a2427c05b82a3a928f4478ec25a2
			// MD5 ("&&&2f25541e221cc23a") = fffaca078da951eb0bb7bcaec164dc02

			// 081c24c301388d105732b287b3f19572,29543 MD5
			// ("&dummy_phone_device&&")
			// 1cc9fc6a9db61aff78878a43093f5692,28293 MD5 ("&123456789abcdef&&")
			// 27c8f4a114a14052ef2b6a99a06ae1e8,13601 MD5
			// ("0.0.0.0&123456789012345&&")
			// 629d262c67899412cbd7a464a5897e29,11694 MD5 ("&1&&")
			// 586920ed6c8e955b8c3d1a7a8aa41304,10758 MD5
			// ("00:00:00:00:00:00&123456789abcdef&&")
			// 9c553730ef5b6c8c542bfd31b5e25b69,4662 MD5 ("&&&")
			// 0b8f361e2be717734045aa8354c2829f,1076 MD5 ("&012345678901237&&")
			// 3199b989b7458168953936b2eb78201b,830 MD5 ("00:00:00:00:00:00&&&")
			// 6509d857162ebe55a8a6b1ce09f041c7,426 MD5
			// ("00:00:00:00:00:00&12345678901234&&")
			// ad52b9be26ccf01d13a6a18226fe2633,350 MD5 ("&153614151215621&&")
			// 9ae5755864e22623d20a16e45ad35cd0,122 MD5 ("&99999999999&&")
			// bbf36a464944911f57aaac2f397b9ca5,90 MD5 ("&1151&&")
			// 3aa6b7cf0824ba6f0b969b585dee77df,33 MD5 ("&2&&")
			// 7604791abf33b08633af21a91e83c6e3,29 MD5 ("&358673000000000&&")
			// d99d39a359358c81dc080f92dc04d25f,24 MD5 ("&352005048247251&&")
			// 1042a79be3c3863b0f89fef2b6077673,19 MD5 ("&88508850885050&&")
			// e1c07fd347bcf8e5f734164477414aed,18 MD5 ("&123483710697851&&")
			// 949e73ca5f6de2ce1ebe927934e8833f,8 MD5 ("&352274016839958&&")

			if (TextUtils.isEmpty(imei)) {
				imei = "";
			}
			long s = 0;
			try {
				s = Long.parseLong(imei);
			} catch (Exception e) {
				s = 0;
			} catch (Error e) {
				s = 0;
			}

			if (((s < 100000000000001l || imei.length() != 15) && guid
					.equals(md5("&" + imei + "&&")))
					|| guid.equals("9c553730ef5b6c8c542bfd31b5e25b69")
					|| guid.equals("3199b989b7458168953936b2eb78201b")
					|| guid.equals("9ee2bd0413dba16772e798f2cff407fd")
					|| guid.equals("2969abd8a2a809da39c7481d7f918b27")
					|| guid.equals("91990821dbcf3c8c1b2144d95cdecae8")
					|| guid.equals("61d5a2427c05b82a3a928f4478ec25a2")
					|| guid.equals("fffaca078da951eb0bb7bcaec164dc02")
					|| guid.equals("081c24c301388d105732b287b3f19572")
					|| guid.equals("1cc9fc6a9db61aff78878a43093f5692")
					|| guid.equals("27c8f4a114a14052ef2b6a99a06ae1e8")
					|| guid.equals("629d262c67899412cbd7a464a5897e29")
					|| guid.equals("586920ed6c8e955b8c3d1a7a8aa41304")
					|| guid.equals("9c553730ef5b6c8c542bfd31b5e25b69")
					|| guid.equals("0b8f361e2be717734045aa8354c2829f")
					|| guid.equals("3199b989b7458168953936b2eb78201b")
					|| guid.equals("6509d857162ebe55a8a6b1ce09f041c7")
					|| guid.equals("ad52b9be26ccf01d13a6a18226fe2633")
					|| guid.equals("9ae5755864e22623d20a16e45ad35cd0")
					|| guid.equals("bbf36a464944911f57aaac2f397b9ca5")
					|| guid.equals("3aa6b7cf0824ba6f0b969b585dee77df")
					|| guid.equals("7604791abf33b08633af21a91e83c6e3")
					|| guid.equals("d99d39a359358c81dc080f92dc04d25f")
					|| guid.equals("1042a79be3c3863b0f89fef2b6077673")
					|| guid.equals("e1c07fd347bcf8e5f734164477414aed")
					|| guid.equals("949e73ca5f6de2ce1ebe927934e8833f")
					|| guid.equals(md5("mac_not_get"))) {
				b = false;
			}
		}
		return b;
	}

	public static void clearGuid(Context context) {
		savePreference(context, "mac", "");
		savePreference(context, "imei", "");
		savePreference(context, "new_uuid", "");
		savePreference(context, "new_android_id", "");
		savePreference(context, "guid", "");
		savePreference(context, "new_gdid", "");
	}

	private static boolean isValidateGdid(String gdid, String mac, String imei,
			String android_id) {
		if (gdid == null) {
			return false;
		}

		if (TextUtils.isEmpty(imei)) {
			imei = "";
		}
		long s = 0;
		try {
			s = Long.parseLong(imei);
		} catch (Exception e) {
			s = 0;
		} catch (Error e) {
			s = 0;
		}

		return (!TextUtils.isEmpty(gdid))
				&& (!gdid.equals(md5("mac_not_get")))
				&& !((s < 100000000000001l || imei.length() != 15) && gdid
						.equals(md5("&" + imei + "&&" + android_id)));
	}

	public static String getGDID(Context context) {
		String mac = getMac(context);
		String imei = getIMEI(context);
		String android_id = getUUID(context);
		String gdid = getPreference(context, "new_gdid");
		if (isValidateMac(mac)) {
			if (isValidateGdid(gdid, mac, imei, android_id)) {
				return gdid;
			}
			gdid = md5(mac + "&" + imei + "&" + "&" + android_id);
			savePreference(context, "new_gdid", gdid);
		} else {
			return md5("mac_not_get");
		}
		return gdid;
	}

	public static String getGUID(Context context) {
		String imei = getIMEI(context);
		String mac = getMac(context);
		if (isValidateMac(mac)) {
			try {
				String guid = getPreference(context, "guid");
				if (isValidateGuid(guid, mac, imei)) {
					return guid;
				} else {
					guid = md5(mac + "&" + imei + "&" + "&");
					savePreference(context, "guid", guid);
				}
				return guid;
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
		} else {
			return md5("mac_not_get");
		}
		return "9c553730ef5b6c8c542bfd31b5e25b69";
	}

	public static String getUUID(Context context) {
		String s = "";
		s = getPreference(context, "new_uuid");
		if (!TextUtils.isEmpty(s)) {
			return s;
		}
		s = getAndroidId(context);
		savePreference(context, "new_uuid", s);
		return s;
	}

	public static String getAndroidId(Context context) {
		String s = "";
		try {
			s = getPreference(context, "new_android_id");
			if (TextUtils.isEmpty(s)) {
				s = Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
			} else {
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		savePreference(context, "new_android_id", s);
		return s;
	}

	public static String getMac(Context context) {
		String mac = getWIFIMAC(context);
		if (!isValidateMac(mac)) {
			mac = getEthernetMacAddressByName();
			if (!isValidateMac(mac)) {
				mac = getEthernetMacAddressFromFile();
			}
		}
		if (!isValidateMac(mac)) {
			mac = "";
		}
		savePreference(context, "mac", mac);
		return mac;
	}

	public static String getIMEI(Context context) {
		String imei = "";
		try {
			imei = getPreference(context, "imei", null);
			if (imei != null) {
				return imei;
			}
			TelephonyManager tm = (TelephonyManager) context
					.getApplicationContext().getSystemService(
							Context.TELEPHONY_SERVICE);
			if (tm != null) {
				imei = tm.getDeviceId();
				if (TextUtils.isEmpty(imei)) {
					imei = "";
				}
			}
		} catch (Exception e) {
			imei = "";
			e.printStackTrace();
		} catch (Error e) {
			imei = "";
			e.printStackTrace();
		}
		savePreference(context, "imei", imei);
		return imei;
	}

	private static String getWIFIMAC(Context context) {
		try {
			String mac = getPreference(context, "mac");
			if (isValidateMac(mac)) {
				return mac;
			}
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			mac = wifi.getConnectionInfo().getMacAddress();
			if (mac != null && mac.length() > 0) {
				return mac;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * TODO 获得MD5串
	 * 
	 * @param s
	 * @return
	 */
	private static String md5(final String s) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
		} catch (Exception e) {
		} catch (Error e) {
		}
		return "";
	}

	/**
	 * 保存sharePreference
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void savePreference(Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(key, value).commit();
	}

	/**
	 * 获取sharePreference
	 * 
	 * @param key
	 * @return
	 */
	private static String getPreference(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, "");
	}

	private static String getPreference(Context context, String key,
			String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, defValue);
	}

	public static String useridString2UseridNumber(String uid) {
		long uidint = 0;
		if (uid == null) {
			uid = "";
		}
		if (uid != null && !uid.equals("0") && uid.indexOf("U") == 0) {
			uid = uid.substring(1);
			try {
				byte[] decodeIdByte = Base64.decode(uid, Base64.DEFAULT);
				String decodeId = new String(decodeIdByte);
				uidint = Long.parseLong(decodeId);
				uidint = uidint >> 2;
				if (uidint != 0) {
					uid = uidint + "";
				}
			} catch (Exception e) {
				e.printStackTrace();
				uid = "0";
			} catch (Error e) {
				e.printStackTrace();
				uid = "0";
			}
		}
		return uid;
	}
}

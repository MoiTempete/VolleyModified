package com.baseproject.YKAnTracker.tool;

import android.content.Context;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class F {

	public static String URLEncoder(String s) {
		if (s == null || s.length() == 0)
			return "";
		try {
			s = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
		return s;
	}

	/**
	 * 屏幕高度
	 * 
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 屏幕宽度
	 * 
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 
	 * TODO 获得MD5串
	 * 
	 * @param s
	 * @return
	 */
	public static String md5(final String s) {
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
	public static void savePreference(Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(key, value).commit();
	}

	/**
	 * 获取sharePreference
	 * 
	 * @param key
	 * @return
	 */
	public static String getPreference(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, "");
	}

	// /**
	// * 把byte[]数组转换成十六进制字符串表示形式
	// *
	// * @param tmp 要转换的byte[]
	// * @return 十六进制字符串表示形式
	// */
	// static char hexDigits[] = {
	// '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
	// 'e', 'f'
	// };
	//
	// public static String byteToHexString(byte[] tmp) {
	// String s;
	// // 用字节表示就是 16 个字节
	// char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
	// // 所以表示成 16 进制需要 32 个字符
	// int k = 0; // 表示转换结果中对应的字符位置
	// for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
	// // 转换成 16 进制字符的转换
	// byte byte0 = tmp[i]; // 取第 i 个字节
	// str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
	// // >>> 为逻辑右移，将符号位一起右移
	// str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
	// }
	// s = new String(str); // 换后的结果转换为字符串
	// return s;
	// }

	// /**
	// * 时间串格式化,传入秒
	// */
	// public static String formatTime(String s) {
	// if (s.indexOf(":") > 0) {
	// return s;
	// }
	// double S;
	// try {
	// S = Double.parseDouble(s);
	// } catch (Exception e) {
	// S = 0;
	// }
	// return formatTime(S);
	// }
	//
	// /**
	// * 传入秒, 返回格式化的String
	// **/
	// public static String formatTime(double s) {
	// try {
	// long msec = (long) s;
	// String times = "";
	// int seconds = 0;
	// int minutes = 0;
	// seconds = (int) msec % 60;
	// minutes = (int) msec / 60;
	// if (minutes > 60)
	// times = minutes / 60 + ":" + (minutes % 60 > 9 ? minutes % 60 : "0" +
	// minutes % 60)
	// + ":";
	// else
	// times = minutes + ":";
	// times = times + (seconds > 9 ? seconds : "0" + seconds);
	// return times;
	// } catch (Exception e) {
	// return "";
	// }
	// }

	// // 时间转换 00:23:13转换为 3453453(s)
	// // 修改105：09格式
	// public static String getSecond(String T) {
	// if (-1 == T.indexOf(":"))
	// return T;
	// String time = new String(T);
	// time = ":::" + time;
	// int lastIndex = time.lastIndexOf(":");
	// String StrSecond = time.substring(lastIndex + 1);
	// time = time.substring(0, lastIndex);
	// lastIndex = time.lastIndexOf(":");
	// String StrMinutes = time.substring(lastIndex + 1);
	// time = time.substring(0, lastIndex);
	//
	// lastIndex = time.lastIndexOf(":");
	// String StrHours = time.substring(lastIndex + 1);
	// time = null;
	// int second = 0;
	// try {
	// second += Integer.parseInt(StrSecond);
	// } catch (Exception e) {
	// }
	// try {
	// second += (Integer.parseInt(StrMinutes)) * 60;
	// } catch (Exception e) {
	// }
	// try {
	// second += (Integer.parseInt(StrHours)) * 3600;
	// } catch (Exception e) {
	// }
	// return "" + second;
	// }

	// public static void sendMessage(Handler mhandler, int mwhat, int marg1,
	// int marg2, Object mobj) {
	// try {
	// Message message = Message.obtain();
	// message.what = mwhat;
	// message.arg1 = marg1;
	// message.arg2 = marg2;
	// message.obj = mobj;
	// mhandler.sendMessage(message);
	// } catch (Exception e) {
	// }
	// }
	//
	// public static void sendMessage(Handler mhandler, int mwhat, Object mobj)
	// {
	// sendMessage(mhandler, mwhat, -1, -1, mobj);
	// }
	//
	// public static void sendMessage(Handler mhandler, int mwhat, int marg1) {
	// sendMessage(mhandler, mwhat, marg1, -1, null);
	// }
	//
	// public static void sendMessage(int mwhat, Handler mhandler) {
	// sendMessage(mhandler, mwhat, -1, -1, null);
	// }
	private final static String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				is));
		final StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(LINE_SEPARATOR);
			}
		} catch (IOException e) {

		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}

		return sb.toString();
	}

	// public static String getJsonValue(JSONObject object, String name) {
	// try {
	// return object.getString(name);
	// } catch (Exception e) {
	// return "";
	// }
	// }

	// public static int getJsonInt(JSONObject object, String name) {
	// try {
	// return object.getInt(name);
	// } catch (Exception e) {
	// return 0;
	// }
	// }

	// public static String formatReputation(String reputation) {
	// if (reputation != null && !"".equals(reputation)) {
	// reputation = reputation.length() < 3 ? reputation :
	// reputation.substring(0, 3);
	// }
	// if (reputation == null || reputation.equals("0"))
	// reputation = "";
	// return reputation;
	// }

	// public static float getFloat(String repu) {
	// if (isStringValid(repu))
	// return 0.0f;
	// float f = Float.parseFloat(repu);
	// f = f / 2.0f;
	// return f;
	// }

	public static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		date = date.replace("-", "");
		date = date.replace("-", "");
		date = date.replace(":", "");
		date = date.replace(":", "");
		date = date.replace(" ", "");
		date = date.substring(2);
		date += "000";
		return date;
	}

	// public static boolean isImagePort(Drawable d) {
	// if (d == null || (d.getIntrinsicHeight() - d.getIntrinsicWidth() > 20)) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static boolean isImagePort(Bitmap b) {
	// if (b == null || (b.getHeight() - b.getWidth() > 20)) {
	// return true;
	// }
	// return false;
	// }

	// /**
	// * sdcard是否存在
	// *
	// * @return
	// */
	// public static Boolean sdcardIsExitBoolean() {
	// Boolean resultBoolean;
	// if
	// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))//
	// sd存在并可写
	// {
	// resultBoolean = true;
	// } else {
	// resultBoolean = false;
	// }
	// return resultBoolean;
	//
	// }

	// /**
	// * 转换文件大小
	// *
	// * @param size 单位为kb
	// * @return
	// */
	// public static String formatSize(float size) {
	// long kb = 1024;
	// long mb = (kb * 1024);
	// long gb = (mb * 1024);
	// if (size < kb) {
	// return String.format("%d B", (int) size);
	// } else if (size < mb) {
	// return String.format("%.2f KB", size / kb); // 保留两位小数
	// } else if (size < gb) {
	// return String.format("%.2f MB", size / mb);
	// } else {
	// return String.format("%.2f GB", size / gb);
	// }
	// }

	// /**
	// * 取得sdcard卡总大小
	// *
	// * @return
	// */
	// public static String getSdcardSpace() {
	// if (!sdcardIsExitBoolean()) {
	// return "0";
	// }
	// StatFs statFs = new
	// StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	// // 获取SDCard上BLOCK总数
	// long nTotalBlocks = statFs.getBlockCount();
	// // 获取SDCard上每个block的SIZE
	// long nBlocSize = statFs.getBlockSize();
	// // 计算SDCard 总容量
	// long nSDTotalSize = nTotalBlocks * nBlocSize;
	// return formatSize(nSDTotalSize);
	// }
	//
	// /**
	// * 取得sdcard卡剩余空间
	// *
	// * @return
	// */
	// public static String getSdcardFreeSpace() {
	// if (!sdcardIsExitBoolean()) {
	// return "0";
	// }
	// StatFs statFs = new
	// StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	//
	// long nAvailaBlock = statFs.getAvailableBlocks();
	//
	// long nBlocSize = statFs.getBlockSize();
	//
	// long nSDFreeSize = nAvailaBlock * nBlocSize;
	//
	// return formatSize(nSDFreeSize);
	// }
	//
	// /**
	// * 计算youku视频占有空间
	// *
	// * @return
	// * @throws Exception
	// */
	// public static String getSdcardYoukuSpace() throws Exception {
	// if (!sdcardIsExitBoolean()) {
	// return "0";
	// }
	// File file = new
	// File(Environment.getExternalStorageDirectory().getAbsolutePath() +
	// "/youku");
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// return formatSize(getFileSize(file));
	// }
	//
	// // 递归
	// public static long getFileSize(File f) throws Exception// 取得文件夹大小
	// {
	// long size = 0;
	// if (f.isDirectory()) {
	// File flist[] = f.listFiles();
	// for (int i = 0; i < flist.length; i++) {
	// if (flist[i].isDirectory()) {
	// size = size + getFileSize(flist[i]);
	// } else {
	// size = size + flist[i].length();
	// }
	// }
	// } else {
	// size = size + getFileSizes(f);
	// }
	// return size;
	// }
	//
	// public static long getFileSizes(File f) throws Exception {// 取得文件大小
	//
	// return f.length();
	// }
	//
	// /**
	// * 其他程序占有空间大小
	// *
	// * @return
	// * @throws Exception
	// */
	// public static String getSdcardOtherSpace() throws Exception {
	// if (!sdcardIsExitBoolean()) {
	// return "0";
	// }
	// StatFs statFs = new
	// StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	// long nTotalBlocks = statFs.getBlockCount();
	// long nAvailaBlock = statFs.getAvailableBlocks();
	// long nBlocSize = statFs.getBlockSize();
	// long nSDTotalSize = nTotalBlocks * nBlocSize;
	// long nSDFreeSize = nAvailaBlock * nBlocSize;
	//
	// File file = new
	// File(Environment.getExternalStorageDirectory().getAbsolutePath(),
	// "youku");
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// return formatSize(nSDTotalSize - nSDFreeSize - getFileSize(file));
	// }
	//
	// /**
	// * 使用空间占总的百分比
	// */
	// public static int getProgrss(long add) {
	// if (!sdcardIsExitBoolean()) {
	// return 100;
	// }
	// StatFs statFs = new
	// StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	// long nTotalBlocks = statFs.getBlockCount();
	// long nBlocSize = statFs.getBlockSize();
	// long nAvailaBlock = statFs.getAvailableBlocks();
	// long nSDTotalSize = nTotalBlocks * nBlocSize;
	// long nSDUseSize = (nTotalBlocks - nAvailaBlock) * nBlocSize;
	// long pro = nSDUseSize + add;
	// // Log.e("progress", pro + "");
	// int result = (int) ((100 * pro) / nSDTotalSize);
	// return result;
	// }
	//
	// public static Boolean hasEnoughDownLoadSpace(long add) {
	// if (!sdcardIsExitBoolean()) {
	// return false;
	// }
	// StatFs statFs = new
	// StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	// long nBlocSize = statFs.getBlockSize();
	// long nAvailaBlock = statFs.getAvailableBlocks();
	//
	// return (nAvailaBlock * nBlocSize - add) > 10 * 1024 * 1024;
	// }

	// /**
	// * Get image from newwork
	// *
	// * @param path The path of image
	// * @return
	// * @throws Exception
	// */
	//
	// public static byte[] getImage(String path) throws Exception {
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5 * 1000);
	// conn.setRequestMethod("GET");
	// InputStream inStream = conn.getInputStream();
	// if (conn.getResponseCode() == 200) {
	// return readStream(inStream);
	// }
	// return null;
	// }
	//
	// /**
	// * Get data from stream
	// *
	// * @param inStream
	// * @return
	// * @throws Exception
	// */
	// public static byte[] readStream(InputStream inStream) throws Exception {
	// ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// while ((len = inStream.read(buffer)) != -1) {
	// outStream.write(buffer, 0, len);
	// }
	// outStream.close();
	// inStream.close();
	// return outStream.toByteArray();
	// }

	public static String formatTotalVV(String total_vv) {
		String resultString = null;
		if (total_vv == null || "".equals(total_vv)) {
			return "";
		}
		Float result = Float.valueOf(total_vv);
		if (result >= 10000) {

			resultString = String.format("%.1f 万",
					Float.valueOf(total_vv) / 10000);
			String lastString = resultString.substring(
					resultString.length() - 4, resultString.length());
			String string = resultString
					.substring(0, resultString.length() - 4);
			Float ss = Float.valueOf(string);
			resultString = getDFormat(string, ss) + lastString;

		} else {
			resultString = getDFormat(total_vv, result);
		}
		return resultString;

	}

	private static String getDFormat(String total_vv, Float result) {
		String resultString;
		if (result >= 1000) {
			resultString = (int) (result / 1000)
					+ ","
					+ total_vv.substring(total_vv.length() - 3,
							total_vv.length());
		} else {
			resultString = total_vv;
		}
		return resultString;
	}

	/**
	 * 压缩文件或文件夹
	 * 
	 * @param srcFilePath
	 *            源文件
	 * @param fileName
	 *            压缩文件中文件的名字和目录
	 * @param zipFilePath
	 *            压缩文件的路径
	 * @throws Exception
	 */
	public static void zipFile(String srcFilePath, String fileName,
			String zipFilePath) throws Exception {
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
				zipFilePath));
		File file = new File(srcFilePath);
		ZipEntry zipEntry = new ZipEntry(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		outZip.putNextEntry(zipEntry);
		int len;
		byte[] buffer = new byte[4096];
		while ((len = inputStream.read(buffer)) != -1) {
			outZip.write(buffer, 0, len);
		}
		outZip.closeEntry();
		outZip.finish();
		outZip.close();
	}

}

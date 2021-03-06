
package com.baseproject.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.baseproject.utils.Profile;
import com.baseproject.utils.Logger;
import com.baseproject.utils.Util;

/**
 * 此类用于一些不关心结果的一次性的Http任务
 * 
 * @author 张宇
 * @create-time Mar 25, 2013 5:28:06 PM
 * @version $Id
 * 
 */
public class DisposableHttpTask extends Thread {

    private String url;

    public DisposableHttpTask(String url) {
        super("DisposableHttpTask");
        this.url = url;
    }

    /*
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        super.run();
        if (Util.hasInternet()) {

            Util.disableConnectionReuseIfNecessary();
            InputStream is = null;
            try {
                URL uri = new URL(url);
                Logger.d("DisposableHttpTask#run()", url);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                conn.setRequestProperty("User-Agent", Profile.User_Agent);
                conn.connect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }

        }
    }

}

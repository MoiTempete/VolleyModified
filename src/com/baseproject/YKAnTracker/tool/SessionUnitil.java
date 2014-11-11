
package com.baseproject.YKAnTracker.tool;

import com.baseproject.YKAnTracker.data.Device;

import java.util.HashMap;
import java.util.Map;

public class SessionUnitil {
    public static String pageEvent_session;

    public static String customEvent_session;

    public static String playEvent_session;
    
//    public static String globle_session;
    
    public static Map<String, String>  http_session=new HashMap<String, String>();

//    public static String creatSession() {
//    	if (null==SessionUnitil.globle_session || "".equals(SessionUnitil.globle_session)) {
//    		SessionUnitil.globle_session=creatglobleSession();
//		}
//        return F.md5(SessionUnitil.globle_session+"|"+F.getTime()+creatRandom()+Profile.gdid);
//    }
    public static String creatSession() {
    	  return F.md5(F.getTime()+creatRandom()+Device.gdid);
    }
    /**
     * 生成 1--10之间的随机数
     * 
     * @return
     */
    public static int creatRandom() {
        return (int) (Math.random() * 10);
    }

}

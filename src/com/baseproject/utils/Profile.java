/*
 * @(#)Constant.java 2013-3-28
 * Copyright 2005-2013 YOUKU.com
 * All rights reserved.
 * YOUKU.com PROPRIETARY/CONFIDENTIAL.
 */

package com.baseproject.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.baseproject.volley.Cache;
import com.baseproject.volley.RequestQueue;
import com.baseproject.volley.toolbox.BitmapLruCache;
import com.baseproject.volley.toolbox.ImageLoader;
import com.baseproject.volley.toolbox.Volley;

public class Profile {

    public static final int IMAGE_CACHE_TYPE = 1;
    public static final int STRING_CACHE_TYPE = 2;

    public static Context mContext;
    public static String TAG;// log默认标签
    public static String User_Agent;// UA
    public static RequestQueue sHttpRequestQueue = null;
    public static RequestQueue sImageRequestQueue = null;
    private static ImageLoader sImageLoader = null;
    private static boolean sImageViewFadeIn = true;
    public static final int FADE_IN_TIME = 150;
    public static final boolean useLruCache = true;
    private static int sCompressRatio = 100;
    private static int sSamleSize = 1;

    public static class CacheConfig {

        protected static final boolean supportL2 = true;

        protected static final String DEFAULT_STRING_CACHE_PATH = "cache";
        protected static final String DEFAULT_IMAGE_CACHEP_AHTH = "images";

        protected static final int DEFAULT_STRING_CACHE_SIZE = 5 * 1024 * 1024; // 5m
        protected static final int DEFAULT_IMAGE_CACHE_SIZE = 50 * 1024 * 1024;// 50m

        protected static int IMAGE_L1_SIZE;
        protected static boolean externalCache = false;
        private static int stringCacheFileNum = 0;
        private static int imageCacheFileNum = 0;
        private static int imageExpire = Cache.EXPIRED_TIME;

        private String stringCachePath;
        private String imageCachePath;
        private int stringCacheSize;
        private int imageCacheSize;

        public CacheConfig() {
            stringCachePath = DEFAULT_STRING_CACHE_PATH;
            imageCachePath = DEFAULT_IMAGE_CACHEP_AHTH;
            stringCacheSize = DEFAULT_STRING_CACHE_SIZE;
            imageCacheSize = DEFAULT_IMAGE_CACHE_SIZE;
        }

        /**
         * @param stringCachePath
         * @param imageCachePath
         * @param stringCacheSize
         *            the size of string cache in bytes
         * @param imageCacheSize
         *            the size of image cache in bytes
         */
        public CacheConfig(String stringCachePath, String imageCachePath, int stringCacheSize, int imageCacheSize) {
            this.stringCachePath = stringCachePath;
            this.imageCachePath = imageCachePath;
            this.stringCacheSize = stringCacheSize;
            this.imageCacheSize = imageCacheSize;
        }

        public static boolean isExternalCache() {
            return externalCache;
        }

        /**
         * Control cache backup to internal or extenal
         * 
         * @param externalCache
         */
        public static void setExternalCache(boolean externalCache) {
            CacheConfig.externalCache = externalCache;
        }

        public static int getStringCacheFileNum() {
            return stringCacheFileNum;
        }

        /**
         * set string_cache file numbers
         * 
         * @param stringCacheFileNum
         */
        public static void setStringCacheFileNum(int stringCacheFileNum) {
            if (stringCacheFileNum <= 0) {
                throw new IllegalArgumentException("file numbers should  greater than 0");
            }
            CacheConfig.stringCacheFileNum = stringCacheFileNum;
        }

        public static int getImageCacheFileNum() {
            return imageCacheFileNum;
        }

        /**
         * set image_cache file numbers
         * 
         * @param imageCacheFileNum
         */
        public static void setImageCacheFileNum(int imageCacheFileNum) {
            if (imageCacheFileNum <= 0) {
                throw new IllegalArgumentException("file numbers should  greater than 0");
            }
            CacheConfig.imageCacheFileNum = imageCacheFileNum;
        }

        /**
         * The expire time of image cache
         * 
         * @return -1 not config; >0 effect
         */
        public static int getImageExpire() {
            return imageExpire;
        }

        /**
         * set iamge expire in milliseconds
         * 
         * @param imageExpire
         */
        public static void setImageExpire(int imageExpire) {
            CacheConfig.imageExpire = imageExpire;
        }
    }

    public static class ImageConfig {
        /** Socket timeout in milliseconds for image requests */
        private static int mImageTimeoutMs = 3000;

        public static int getImageTimeoutMs() {
            return mImageTimeoutMs;
        }

        /**
         * Set socket timeout in milliseconds for image requests
         * 
         * @param imageTimeoutMs
         *            Socket timeout in milliseconds for image requests
         */
        public static void setmImageTimeoutMs(int imageTimeoutMs) {
            if (imageTimeoutMs > 0)
                mImageTimeoutMs = imageTimeoutMs;
        }

    }

    /**
     * @param tag
     *            TAG
     * @param useragent
     * @param context
     */
    public static void initProfile(String tag, String useragent, Context context) {
        TAG = tag;
        User_Agent = useragent;
        mContext = context;
        CacheConfig config = new CacheConfig();
        if (mContext != null) {
            sHttpRequestQueue = Volley.newRequestQueue(context, config.stringCachePath, config.stringCacheSize, 5);
            sImageRequestQueue = Volley.newRequestQueue(context, config.imageCachePath, config.imageCacheSize, 15);
        }
    }

    /**
     * @param tag
     * @param useragent
     * @param context
     * @param stringCachePath
     *            the path of string_cache which should not empty
     * @param imageCachePath
     *            the path of image_cache which should not empty
     * @param stringCacheSize
     *            the size of string cache in bytes
     * @param imageCacheSize
     *            the size of image cache in bytes
     */
    public static void initProfile(String tag, String useragent, Context context, String stringCachePath,
            String imageCachePath, int stringCacheSize, int imageCacheSize) {
        if (TextUtils.isEmpty(stringCachePath)) {
            throw new IllegalArgumentException("The string cache path should not empty!");
        }
        if (TextUtils.isEmpty(imageCachePath)) {
            throw new IllegalArgumentException("The image cache path should not empty!");
        }
        CacheConfig config = new CacheConfig(stringCachePath, imageCachePath, stringCacheSize, imageCacheSize);
        TAG = tag;
        User_Agent = useragent;
        mContext = context;
        if (mContext != null) {
            sHttpRequestQueue = Volley.newRequestQueue(context, config.stringCachePath, config.stringCacheSize, 5,
                    CacheConfig.stringCacheFileNum);
            sImageRequestQueue = Volley.newRequestQueue(context, config.imageCachePath, config.imageCacheSize, 15,
                    CacheConfig.imageCacheFileNum);
        }
    }

    /**
     * clear string cache
     */
    public static void clearStringCache() {
        if (sHttpRequestQueue != null) {
            sHttpRequestQueue.getCache().clear();
        }
    }

    /**
     * clear imageCache
     */
    public static void clearImageCache() {
        if (sImageRequestQueue != null) {
            sImageRequestQueue.getCache().clear();
        }
    }

    /**
     * clear both stringCache and imageCache
     */
    public static void clearAppCache() {
        clearStringCache();
        clearImageCache();
    }

    public static void setCompressRatio(int compressRatio) {
        if (compressRatio > 0 && compressRatio <= 100) {
            sCompressRatio = compressRatio;
            Logger.d(TAG, "sCompressRatio = " + sCompressRatio);
        }
    }

    public static int getCompressRatio() {
        return sCompressRatio;
    }

    public static void setSampleSize(int sampleSize) {
        if (sampleSize > 0 && sampleSize < 16) {
            sSamleSize = sampleSize;
            Logger.d(TAG, "sSamleSize = " + sSamleSize);
        }
    }

    public static int getSampleSize() {
        return sSamleSize;
    }

    public static ImageLoader getImageLoader() {
        if (sImageLoader == null) {
            if (!CacheConfig.supportL2) {
                sImageLoader = new ImageLoader(sImageRequestQueue);
            } else {
                int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
                CacheConfig.IMAGE_L1_SIZE = 1024 * 1024 * memClass / 8;
                sImageLoader = new ImageLoader(sImageRequestQueue, new BitmapLruCache(CacheConfig.IMAGE_L1_SIZE));
            }
        }
        return sImageLoader;
    }

    public static boolean getImageViewFadeIn() {
        return sImageViewFadeIn;
    }
}

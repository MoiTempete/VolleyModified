/*
 * Copyright (C) 2011 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baseproject.volley;

/**
 * An interface for a cache keyed by a String with a byte array as data.
 */
public interface Cache {
    public static final int EXPIRED_TIME = 20 * 60 * 1000;// 20 minutes

    /**
     * Retrieves an entry from the cache.
     * 
     * @param key
     *            Cache key
     * @return An {@link Entry} or null in the event of a cache miss
     */
    public Entry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     * 
     * @param key
     *            Cache key
     * @param entry
     *            Data to store and metadata for cache coherency, TTL, etc.
     */
    public void put(String key, Entry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     */
    public void initialize();

    /**
     * Invalidates an entry in the cache.
     * 
     * @param key
     *            Cache key
     * @param fullExpire
     *            True to fully expire the entry, false to soft expire
     */
    public void invalidate(String key, boolean fullExpire);

    /**
     * Removes an entry from the cache.
     * 
     * @param key
     *            Cache key
     */
    public void remove(String key);

    /**
     * Empties the cache.
     */
    public void clear();

    /**
     * Check if contain entry in the cache without operating IO
     * 
     * @param key
     *            Cache key
     * @return Return true if the cache contain entry with the specified key, false otherwise
     */
    public boolean isContainValue(String key);

    /**
     * Data and metadata for an entry returned by the cache.
     */
    public static class Entry {
        /** The data returned from cache. */
        public byte[] data;

        /** ETag for cache coherency. */
        public String etag;

        /** Charset read from server headers. */
        public String charset;

        public long ttl;

        /** True if the entry is expired. */
        public boolean isExpired() {
            long cur = System.currentTimeMillis();
            return this.ttl < cur;
        }
    }

}
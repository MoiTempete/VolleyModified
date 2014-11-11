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

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

/**
 * Data and headers returned from {@link Network#performRequest(Request)}.
 */
public class NetworkResponse {
    /**
     * Creates a new network response.
     * 
     * @param statusCode
     *            the HTTP status code
     * @param data
     *            Response body
     * @param headers
     *            Headers returned with this response, or null for none
     * @param notModified
     *            True if the server returned a 304 and the data was already in cache
     */
    public NetworkResponse(int statusCode, byte[] data, String etag, String charset, boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.etag = etag;
        this.charset = charset;
        this.notModified = notModified;
        this.ttl = System.currentTimeMillis() + Cache.EXPIRED_TIME;
    }

    public NetworkResponse(int statusCode, byte[] data, String etag, String charset, boolean notModified, int expireTime) {
        this(statusCode, data, etag, charset, notModified);
        this.ttl = System.currentTimeMillis() + expireTime;
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, "", HTTP.UTF_8, false);
    }

    public NetworkResponse(byte[] data, String etag) {
        this(HttpStatus.SC_OK, data, etag, HTTP.UTF_8, false);
    }

    public NetworkResponse(byte[] data, String etag, String charset) {
        this(HttpStatus.SC_OK, data, etag, charset, false);
    }

    /** The HTTP status code. */
    public final int statusCode;

    /** Raw data from this response. */
    public final byte[] data;

    public final String etag;

    public final String charset;

    public long ttl;

    /** True if the server returned a 304 (Not Modified). */
    public final boolean notModified;
}
/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baseproject.volley.toolbox;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.baseproject.utils.Profile;
import com.baseproject.volley.VolleyError;
import com.baseproject.volley.toolbox.ImageLoader.ImageContainer;
import com.baseproject.volley.toolbox.ImageLoader.ImageListener;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class NetworkImageBackground {

    private static final String TAG = "NetworkImageViewBackground";

    /** The URL of the network image to load */
    private String mUrl;

    /**
     * Resource ID of the image to be used as a placeholder until the network image is loaded.
     */
    private int mDefaultImageId;

    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;

    /** Local copy of the ImageLoader. */
    private ImageLoader mImageLoader;

    /** Current ImageContainer. (either in-flight or finished) */
    private ImageContainer mImageContainer;

    private int mImageWidth;

    private int mImageHeight;

    /** The view to be set background */
    private WeakReference<View> mWeakReference;

    private NetworkImageBackground(View v) {
        mWeakReference = new WeakReference<View>(v);
    }

    public static NetworkImageBackground init(View v) {
        return new NetworkImageBackground(v);
    }

    public void setImageUrl(String url) {
        setImageUrl(url, Profile.getImageLoader(), 0, 0);
    }

    public void setImageUrl(String url, int imageWidth, int imageHeight) {
        setImageUrl(url, Profile.getImageLoader(), imageWidth, imageHeight);
    }

    /**
     * Sets URL of the image that should be loaded into this view. Note that calling this will
     * immediately either set the cached image (if available) or the default image specified by
     * {@link NetworkImageBackground#setDefaultImageResId(int)} on the view.
     *
     * NOTE: If applicable, {@link NetworkImageBackground#setDefaultImageResId(int)} and
     * {@link NetworkImageBackground#setErrorImageResId(int)} should be called prior to calling
     * this function.
     *
     * @param url
     *            The URL that should be loaded into this ImageView.
     * @param imageLoader
     *            ImageLoader that will be used to make the request.
     */
    public void setImageUrl(String url, ImageLoader imageLoader) {
        setImageUrl(url, imageLoader, 0, 0);
    }

    public void setImageUrl(String url, ImageLoader imageLoader, int imageWidth, int imageHeight) {
        mUrl = url;
        mImageLoader = imageLoader;
        if (imageWidth != 0) {
            mImageWidth = imageWidth;
        }
        if (imageHeight != 0) {
            mImageHeight = imageHeight;
        }
        // The URL has potentially changed. See if we need to load it.
        loadImageIfNecessary(false);
    }

    /**
     * Sets the default image resource ID to be used for this view until the attempt to load it
     * completes.
     */
    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    /**
     * Sets the error image resource ID to be used for this view in the event that the image
     * requested fails to load.
     */
    public void setErrorImageResId(int errorImage) {
        mErrorImageId = errorImage;
    }

    /**
     * Loads the image for the view if it isn't already loaded.
     * 
     * @param isInLayoutPass
     *            True if this was invoked from a layout pass, false otherwise.
     */
    void loadImageIfNecessary(final boolean isInLayoutPass) {
        View view = mWeakReference.get();
        if (view == null)
            return;
        int width = view.getWidth();
        int height = view.getHeight();

        boolean wrapWidth = false, wrapHeight = false;
        if (view.getLayoutParams() != null) {
            wrapWidth = view.getLayoutParams().width == LayoutParams.WRAP_CONTENT;
            wrapHeight = view.getLayoutParams().height == LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params.width > 0) {
                width = params.width;
            } else if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = view.getMeasuredWidth();
            }

            if (params.height > 0) {
                height = params.height;
            } else if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = view.getMeasuredHeight();
            }
        }

        // if the URL to be loaded in this view is empty, cancel any old requests and clear the
        // currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            if (mImageContainer != null) {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }
            setDefaultImageOrNull();
            return;
        }

        // if there was an old request in this view, check if it needs to be canceled.
        if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
            if (mImageContainer.getRequestUrl().equals(mUrl)) {
                // if the request is from the same URL, return.
                return;
            } else {
                // if there is a pre-existing request, cancel it if it's fetching a different URL.
                mImageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }

        // Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
        int maxWidth = wrapWidth ? 0 : width;
        int maxHeight = wrapHeight ? 0 : height;

        if (mImageWidth != 0) {
            maxWidth = mImageWidth;
        }

        if (mImageHeight != 0) {
            maxHeight = mImageHeight;
        }

        // The pre-existing content of this view didn't match the current URL. Load the new image
        // from the network.
        if (mImageLoader == null) {
            return;
        }

        ImageContainer newContainer = mImageLoader.get(mUrl, new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mErrorImageId != 0) {
                    setBackgroundResource(mErrorImageId);
                }
            }

            @SuppressLint("NewApi")
            @Override
            public void onResponse(final ImageContainer response, boolean isImmediate) {
                View view = mWeakReference.get();
                // If this was an immediate response that was delivered inside of a layout
                // pass do not set the image immediately as it will trigger a requestLayout
                // inside of a layout. Instead, defer setting the image by posting back to
                // the main thread.
                if (isImmediate) {
                    if (view == null)
                        return;
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            onResponse(response, false);
                        }
                    });
                    return;
                }

                if (mWeakReference.get() == null) {
                    return;
                }

                if (response.getBitmap() != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(view.getResources(), response.getBitmap());
                    setBackgroundResource(view, bitmapDrawable);
                } else if (mDefaultImageId != 0) {
                    setBackgroundResource(mDefaultImageId);
                }
            }
        }, maxWidth, maxHeight, false, null);

        // update the ImageContainer to be the new bitmap container.
        mImageContainer = newContainer;
    }

    private void setBackgroundResource(int resid) {
        View view = mWeakReference.get();
        if (view == null)
            return;
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resid);
        } else {
            view.setBackgroundResource(resid);
        }
    }

    private void setDefaultImageOrNull() {
        View view = mWeakReference.get();
        if (view == null)
            return;
        if (view instanceof ImageView) {
            if (mDefaultImageId != 0) {
                ((ImageView) view).setImageResource(mDefaultImageId);
            } else {
                ((ImageView) view).setImageBitmap(null);
            }
        } else {
            if (mDefaultImageId != 0) {
                setBackgroundResource(mDefaultImageId);
            } else {
                setBackgroundResource(view, null);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundResource(View view, BitmapDrawable bitmapDrawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(bitmapDrawable);
        } else {
            view.setBackground(bitmapDrawable);
        }
    }
}

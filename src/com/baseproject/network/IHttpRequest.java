package com.baseproject.network;

import com.baseproject.volley.Request;

/**
 * 
 * @author 张宇
 * @create-time Oct 15, 2012 10:22:46 AM
 * @version $Id
 * @modify by:liubing
 * @desc 增加setCookie接口
 */
public interface IHttpRequest<T> {

	public void request(HttpIntent httpIntent, IHttpRequestCallBack<T> callBack);

	public void request(HttpIntent httpIntent, IHttpRequestCallBack<T> callBack,
			boolean isClearCache);
	
	public void request(HttpIntent httpIntent,
			final IHttpRequestCallBack<T> callBack, Class<T> clazz);
	
	public void request(HttpIntent httpIntent,
			final IHttpRequestCallBack<T> callBack, final boolean isClearCache, Class<T> clazz);

	/**
	 * 获得接口未解析时的数据String
	 * 
	 * @return
	 */
	public String getDataString();
	
	public Object getDataObject();

	public void cancel();

	public void setCookie(String cookie);

	public void setRetryTimes(int retryTimes);

	public void setUseEtagCache(boolean useEtagCache);
	
	public Request.Status getStatus();

	public interface IHttpRequestCallBack<T> {

		public void onSuccess(HttpRequestManager<T> httpRequestManager);

		public void onFailed(String failReason);

		// public void onCancel();
	}

}

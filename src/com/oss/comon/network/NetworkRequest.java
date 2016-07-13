package com.oss.comon.network;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.content.Context;
import android.util.Log;

public class NetworkRequest {
	/*HttpPost.METHOD_NAME or HttpGet.METHOD_NAME*/
	private static final String LOG_TAG = NetworkRequest.class.getSimpleName();
	/**
     * Refer to the followign files in BaiduBrowser:
     *   - BdDownloadThread.java
     *   - BdNetEngine.java
     *   - BdNetReceiver.java
     */
	static public final String HEADER_NAME_CONTENT_LENGTH = "Content-Length";
	static public final String HEADER_NAME_CONTENT_ENCODING = "Content-Encoding";
	static public final String HEADER_NAME_SET_COOKIE = "Set-Cookie";
	static public final String HEADER_NAME_COOKIE = "Cookie";
	static public final String HEADER_NAME_LOCATION = "Location";
	static public final String HEADER_NAME_USER_AGENT = "User-Agent";
    static public final String HEADER_NAME_REFERER = "Referer";    
    static public final String HEADER_NAME_CMWAP_ONLINE_HOST = "X-Online-Host";
	
	static private final String CONTENT_ENCODING_GZIP = "gzip";
	
	static private final int OP_TYPE_POST = 0;
	
	static private final int INTERNAL_BUF_LEN = 1024 * 4;

	private boolean mUseCmwap;
	private String mWapUrl;	
	
	private NetworkRequestListener mNetworkRequestListener = null;
	private NetworkRequestData mNetworkRequestData = null;
	
	private int mConnTimeOut = -1;
	private int mReadTimeOut = -1;
	private Map<String, String> mHeaders = new HashMap<String, String>();
	private boolean mRedirectEnabled;
	private NetworkManager mManager;
	private NetworkCode mNetworkCode = null;
	
	public NetworkRequest(NetworkManager pManager, NetworkRequestListener requestListener,  NetworkRequestData networkRequestData)
	{
		mNetworkRequestData = networkRequestData;
		mNetworkRequestListener = requestListener;
		mManager = pManager;
		mNetworkCode = new NetworkCode(NetworkCode.NONE, "Construct");
	}
	
	public NetworkRequestData getNetworkRequestData() {
		return mNetworkRequestData;
	}
	
	public NetworkRequestListener getNetworkRequestListener() {
		return mNetworkRequestListener;
	}
	
	public NetworkCode upload()
	{
		if(null == mNetworkRequestData) {
			mNetworkCode.set(NetworkCode.Url_Null, "RequestData is null");
		}else {
			operate();
		}
		return mNetworkCode;
		
	}

	public boolean operate() 
	{
		boolean result = false;
		HttpURLConnection urlConn = null;
		int statusCode = 0;
		
		if (null == mNetworkRequestData.getRequestUrl())
		{
			mNetworkCode.set(NetworkCode.Url_Null, "mUrl is null");
			return false;
		}
			do
			{
				urlConn = createConn();
				if (null == urlConn) {
					break;
				}
				
				result = connect(urlConn);
				if (!result) 
                {
					break;
				}
				
                result = postData(urlConn);
					if (!result) {
						mNetworkCode.set(NetworkCode.Post_Data_Fail, "Post data fail");
						break;
					}
				
                try
				{
					statusCode = urlConn.getResponseCode();
				}
				catch (Throwable t)
				{
					mNetworkCode.set(NetworkCode.Get_Response_Data_Fail, t.getMessage());
					t.printStackTrace();
					break;
				}
                
                result = checkResponseCode(statusCode);
				if (!result) 
                {
					mNetworkCode.set(NetworkCode.Check_Response_Data_Fail, "Check response code fail");
					break;
				}
				
                result = getData(urlConn, statusCode);
                if (!result) 
                {
                	mNetworkCode.set(NetworkCode.Check_Response_Data_Fail, "Get data fail");
                }
			} while (false);
			
		
		if (urlConn != null)
		{
			try {urlConn.disconnect();} catch (Exception e) {e.printStackTrace();mNetworkCode.set(NetworkCode.Disconnect_Fail, "Disconnect fail");}
		}
		
		if (mNetworkRequestListener != null)
		{
			boolean ret = mNetworkRequestListener.onConnShutdown();
			if (!ret)
			{
				mNetworkCode.set(NetworkCode.Connect_Shutdown_Fail, "Connect shutdown fail");
				result = ret;
			}
		}
		if(result) {
			mNetworkCode.set(NetworkCode.NONE, "Everything ok");
		}
		return result;
	}
	
	
	private HttpURLConnection createConn()
	{
		String requestUrl = mNetworkRequestData.getRequestUrl();
		HttpURLConnection urlConn = null;
		
		if (null == requestUrl)
		{
			mNetworkCode.set(NetworkCode.Url_Null, "Url is null");
			return null;
		}
		
		try
		{
				
			URL url = new URL(requestUrl);
			urlConn=(HttpURLConnection)url.openConnection();  
			//urlConn.setFollowRedirects(mRedirectEnabled);
			//urlConn.setInstanceFollowRedirects(mRedirectEnabled);
			//if setDoOutput is true and set HREADER_NAME_CONTENT_LENGTH request property, this Http is post even though you set PostType to Get
			if(mNetworkRequestData.getRequestType() == HttpPost.METHOD_NAME) {
				urlConn.setDoOutput(true);
				urlConn.setRequestProperty(HEADER_NAME_CONTENT_LENGTH, "" + mNetworkRequestData.getRequestDataInByte().length);
			} else {
				urlConn.setDoOutput(false);
			}
			urlConn.setConnectTimeout(30000);
			urlConn.setReadTimeout(30000);
			Log.i(LOG_TAG, "timeoutvalue = " + urlConn.getConnectTimeout());
			Log.i(LOG_TAG, "readTimeout value = " + urlConn.getReadTimeout());
				urlConn.setDoInput(true);
				urlConn.setUseCaches(false);
				urlConn.setRequestMethod(mNetworkRequestData.getRequestType());
				//urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				//urlConn.setRequestProperty("Connection", "Keep-Alive");
				urlConn.setRequestProperty("Charset", "UTF-8");
			return urlConn;
		} 
		catch (Throwable t)
		{
			mNetworkCode.set(NetworkCode.Url_Null, t.getMessage());
			t.printStackTrace();
			return null;
		}
	}

	
	private boolean connect(HttpURLConnection urlConn)
	{
		boolean ret;
		
		if (mNetworkRequestListener != null)
		{
			ret = mNetworkRequestListener.onConnStart();
			if (!ret)
			{
				mNetworkCode.set(NetworkCode.Connect_Fail, "mNetListener.onConnStart = false");
				return false;
			}
		}
		
		try
		{
			urlConn.connect();
			return true;
		}
		catch (Throwable t)
		{
			mNetworkCode.set(NetworkCode.Connect_Fail, t.getMessage());
			t.printStackTrace();
			return false;
		}
	}
	
	
	private boolean postData(HttpURLConnection urlConn)
	{
		if(mNetworkRequestData.getRequestType() == HttpGet.METHOD_NAME) {
			return true;
		}
		boolean ret = false;
		OutputStream out = null;
		
		if (null == mNetworkRequestData)
		{
			mNetworkCode.set(NetworkCode.Post_Data_Fail, "PostClient null");
			return false;
		}
		
		try
		{
			out = urlConn.getOutputStream();
			byte[] buff = mNetworkRequestData.getRequestDataInByte();
			int bytesLen = buff.length;
			int bytesPost = 0;
			int bytesGet;
			out.write(buff, 0, bytesLen);
			out.flush();
			bytesPost = buff.length;
			ret = (bytesPost == bytesLen);
		}
		catch (Throwable t)
		{
			mNetworkCode.set(NetworkCode.Post_Data_Fail, t.getMessage());
			t.printStackTrace();
		}
		finally
		{
			if (out != null){ 
				try {
					out.close();
				} catch (Throwable t){
					t.printStackTrace();
					mNetworkCode.set(NetworkCode.Post_Data_Fail, t.getMessage());
				}
			}
		}
		return ret;
	}
	
	
	private boolean getData(HttpURLConnection urlConn, int statusCode)
	{
		Log.i(LOG_TAG, "getData statusCode = " + statusCode);
		boolean ret = true;
		boolean isGzip = false;
		GZIPInputStream gzipInStream = null;
		InputStream inStream = null;
		InputStream inStreamFinal;
		Log.i(LOG_TAG, "statusCode = " + statusCode);
		if (statusCode != HttpURLConnection.HTTP_OK &&
				statusCode != HttpURLConnection.HTTP_PARTIAL) 
		{
			mNetworkCode.set(NetworkCode.Get_Data_Fail, "statusCode = " + statusCode);
			return false;
		}
		
		
		try
		{
			String contentEncoding = urlConn.getHeaderField(HEADER_NAME_CONTENT_ENCODING);
			if (contentEncoding != null && contentEncoding.indexOf(CONTENT_ENCODING_GZIP) >= 0) 
			{
				isGzip = true;
			}
			inStream = urlConn.getInputStream();
			if (isGzip) 
			{
				gzipInStream = new GZIPInputStream(inStream);
				inStreamFinal = gzipInStream;
			}
			else 
			{
				inStreamFinal = inStream;
			}
			StringBuffer sb = new StringBuffer();  
            String readLine;  
            
            BufferedReader responseReader;  
            responseReader = new BufferedReader(new InputStreamReader(inStreamFinal, "UTF-8"), 1024);  
            while ((readLine = responseReader.readLine()) != null) { 
            	sb.append(readLine).append("\n");  
            }  
            responseReader.close();
            String receivedStr = sb.toString();
            
            if (mNetworkRequestListener != null)
			{
				ret = mNetworkRequestListener.onReceivedDataAtSubThread(receivedStr);
				Log.i(LOG_TAG, "length = " + sb.length());
				mNetworkCode.setContent(receivedStr);
			}
		}
		catch (Throwable t)
		{
			mNetworkCode.set(NetworkCode.Get_Data_Fail, t.getMessage());
			t.printStackTrace();
			ret = false;
		}
		finally
		{
			if (gzipInStream != null) try {gzipInStream.close();} catch (Throwable t) {t.printStackTrace();mNetworkCode.set(NetworkCode.Get_Data_Fail, t.getMessage());}
			if (inStream != null) try {inStream.close();} catch (Throwable t) {t.printStackTrace();mNetworkCode.set(NetworkCode.Get_Data_Fail, t.getMessage());}
		}
		
		return ret;
	}
	
	
	private boolean checkResponseCode(int statusCode)
	{
		boolean ret = true;
		if (mNetworkRequestListener != null)
		{
			ret = mNetworkRequestListener.onResponseCode(statusCode);
		}
		
		return ret;
	}
	

	static public boolean isRedirectCode(int statusCode)
	{
		if (statusCode == 301 || statusCode == 302 || statusCode == 303 || statusCode == 307) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setConnTimeOut(int connTimeIOut)
	{
		mConnTimeOut = connTimeIOut;
	}
	
	
	public void setReadTimeOut(int readTimeIOut)
	{
		mReadTimeOut = readTimeIOut;
	}
	

	public void setHeaders(Map<String, String> headers)
	{
		mHeaders = headers;
	}
	
	
	public void enableRedirect(boolean enabled)
	{
		mRedirectEnabled = enabled ;
	}
	
	
	public void addHeader(String name, String value)
	{
		if (null != mHeaders && null != name && null != value)
		{
			mHeaders.put(name, value);
		}
	}
	
	
	static public boolean isHeaderEqueal(String h1, String h2)
	{
		if (h1 != null && h2 != null)
		{
			return h1.toUpperCase().equals(h2.toUpperCase());
		}
		else
		{
			return false;
		}
	}
}

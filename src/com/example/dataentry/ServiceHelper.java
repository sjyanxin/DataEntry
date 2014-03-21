package com.example.dataentry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Base64;
import android.util.Log;

public class ServiceHelper {

	public final static String SERVICE_ROOT_URI = "http://192.168.25.105/JsonWCFService/";

	private final static String SERVICE_URI = "http://192.168.25.105/JsonWCFService/MenuService.svc/";

	public static String SendPostRequest(String method, String jsonString) {

		String result = null;
		try {

			HttpPost request = new HttpPost(SERVICE_URI + method);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;charset=utf-8");

			StringEntity entity = new StringEntity(jsonString, "UTF-8");

			request.setEntity(entity);

			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			HttpEntity responseEntity = response.getEntity();
			result = retrieveInputStream(responseEntity);
			 int code = response.getStatusLine().getStatusCode();
			Log.d("WebInvoke", "Saving : "
					+ response.getStatusLine().getStatusCode());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String SendGetRequest(String method, String Url) {

		String result = null;
		try {
			// Send GET request to <service>/GetVehicle/<plate>
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(SERVICE_URI + method);

			//request.setHeader("Accept", "application/json");
			//request.setHeader("Content-type", "application/json");

			HttpResponse response = httpClient.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {

				
				HttpEntity responseEntity = response.getEntity();
				// Read response data into buffer
				
				//char[] buffer = new char[(int) responseEntity	.getContentLength()];
				//InputStream stream = responseEntity.getContent();
				//InputStreamReader reader = new InputStreamReader(stream);
				//reader.read(buffer);
				//stream.close();
				//String s=new String(buffer);
				
				result = retrieveInputStream(responseEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String retrieveInputStream(HttpEntity httpEntity) {

		int length = (int) httpEntity.getContentLength();

		if (length < 0)
			length = 10000;

		StringBuffer stringBuffer = new StringBuffer(length);

		try {

			InputStreamReader inputStreamReader = new InputStreamReader(

			httpEntity.getContent(), HTTP.UTF_8);

			char buffer[] = new char[length];

			int count;

			while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {

				stringBuffer.append(buffer, 0, count);

			}

		} catch (UnsupportedEncodingException e) {

		} catch (IllegalStateException e) {

		} catch (IOException e) {

		}

		return stringBuffer.toString();

	}
	
	
	public static String getBase64(String path){		
			if (path == null)
				return null;
			byte[] bytes = null;
			InputStream is;
			File myfile = new File(path);
			try {
				is = new FileInputStream(path);
				bytes = new byte[(int) myfile.length()];
				int len = 0;
				int curLen = 0;
				while ((len = is.read(bytes)) != -1) {
					curLen += len;
					is.read(bytes);
				}
				is.close();
			} catch (FileNotFoundException e1) {			
				e1.printStackTrace();
			} catch (IOException e) {			
				e.printStackTrace();
			}
			String baseStr = Base64.encodeToString(bytes, Base64.DEFAULT);
			return baseStr;		
	}
	

}

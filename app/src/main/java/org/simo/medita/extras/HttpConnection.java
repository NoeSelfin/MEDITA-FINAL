package org.simo.medita.extras;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpConnection {
	
	private static  int CONN_TIMEOUT = 20000;
    private static  int SO_TIMEOUT = 20000;
	
	public HttpConnection(){
		
	}
	
	public String postData(String url, String params) {
		 String result = null;
		
	    // Create a new HttpClient and Post Header
		 DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

		Log.i("medita_url",url);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("params", params));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        Log.i("medita_params",params);
	        
	      //Set timeout
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONN_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
	        
	        httpclient.setParams(httpParameters);
	        
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
        	//Log.i("medita_response",response.getStatusLine().toString());
			//Log.i("medita_response", EntityUtils.toString(response.getEntity()));

	       // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result= convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }
	        
	    } catch (ClientProtocolException e) {
			Log.i("medita_result","Error 1");
	    	return null;
	    } catch (IOException e) {
			Log.i("medita_result","Error 2");
	    	return null;
	    } catch (Exception e){
			Log.i("medita_result",e.getMessage());
	    	return null;
	    }
	    
	    return result;
	} 
	
	public String getData(String url, JSONObject params) {
		 String result = null;
		
		 HttpClient httpclient = new DefaultHttpClient();
         HttpGet httpget = new HttpGet(url); 

         // Execute the request
         HttpResponse response;
         try {
        	 // Add your data
	        HttpParams p = httpget.getParams();
 	        p.setParameter("params", Basics.toBase64(params.toString()));
 	        httpget.setParams(p);
        	 
             response = httpclient.execute(httpget);
             // Examine the response status
           	 Log.i("medita",response.getStatusLine().toString());

             // Get hold of the response entity
             HttpEntity entity = response.getEntity();
             // If the response does not enclose an entity, there is no need
             // to worry about connection release
             if (entity != null) {
                 // A Simple JSON Response Read
                 InputStream instream = entity.getContent();
                 result= convertStreamToString(instream);
                 // now you have the string representation of the HTML request
                 instream.close();
             }

         } catch (Exception e) {
        	 result = null;
         }   
         
         return result;
	}
	
	public Bitmap getBitmap(String url) {
		 Bitmap result = null;
		
		 HttpClient httpclient = new DefaultHttpClient();
         HttpGet httpget = new HttpGet(url); 

        // Execute the request
        HttpResponse response;
        try {
       	        	 
            response = httpclient.execute(httpget);
            // Examine the response status
           	 Log.i("medita",response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                result = BitmapFactory.decodeStream(instream, null, bmOptions);
                // now you have the string representation of the HTML request
                instream.close();
            }

        } catch (Exception e) {}   
        
        return result;
	}
	
		 
	 private String convertStreamToString(InputStream is) {
		    /*
		     * To convert the InputStream to String we use the BufferedReader.readLine()
		     * method. We iterate until the BufferedReader return null which means
		     * there's no more data to read. Each line will appended to a StringBuilder
		     * and returned as String.
		     */
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    try {
		        while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            is.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    return sb.toString();
		}

}




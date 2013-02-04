package com.gsteam.ticktacktoe.Services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Executer implements Runnable {
	private ExecuterListner listner;
	private String url;
	public Executer(ExecuterListner listner, String url) {
		this.listner = listner;
		this.url = url;
	}
	
	@Override
	public void run() {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        listner.onComplete(responseString);
		        return;
		    } else{
		        response.getEntity().getContent().close();
		        listner.connectionError();
		        return;
		    }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listner.connectionError();
        return;
		
	}
	
}
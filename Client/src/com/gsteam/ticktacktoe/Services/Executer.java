package com.gsteam.ticktacktoe.Services;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class Executer extends AsyncTask<Void, Void, Void> {
	private ExecuterListner listner;
	private String url;
	public Executer(ExecuterListner listner, String url) {
		this.listner = listner;
		this.url = url;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		try {
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        listner.onComplete(responseString);
		        Log.i("Executer::doInBackground", "onComplete");
		        return null;
		    } else{
		        response.getEntity().getContent().close();
		    }
		} catch (Exception e) {
			//e.printStackTrace();
		}
		listner.connectionError();
		Log.i("Executer::doInBackground", "connectionError");
		return null;
	}
}
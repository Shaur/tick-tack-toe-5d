package com.gsteam.ticktacktoe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Client implements IClient {
	private static IClient client = null;
	private static String server = "http://192.168.1.113:8080";
	private static String ping = "/ping";
	
	public static synchronized IClient GetInstance() {
		if(client == null)
			client = new Client();
		return client;
	}
	
	private Client() {
		
	}

	@Override
	public void hasConnected(final IClientHasConnectedListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public void onComplete(String result) {
				if (result.equals("pong")) {
					listner.hasConnected();
				} else {
					listner.hasDisconnected();
				}			
			}
			
			@Override
			public void connectionError() {
				listner.hasDisconnected();
			}
		}, server + ping);
		new Thread(e).start();
	}
}
package com.gsteam.ticktacktoe.Services;

public class Client implements IClient {
	private static IClient client = null;
	private final String server = "http://192.168.1.111:8080/";
	private final String getDeviceKey = "getDeviceKey?key=";
	
	public static synchronized IClient GetInstance() {
		if(client == null)
			client = new Client();
		return client;
	}
	
	private Client() {
		
	}

	@Override
	public void getClientKey(String deviceId, final ClienStringListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public synchronized void onComplete(String result) {
				listner.onResult(result);
			}
			
			@Override
			public synchronized void connectionError() {
				listner.connectionError();
			}
		}, server + getDeviceKey + deviceId);
		e.execute();
	}
}
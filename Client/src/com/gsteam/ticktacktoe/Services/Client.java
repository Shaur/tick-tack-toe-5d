package com.gsteam.ticktacktoe.Services;

import java.net.URLEncoder;

public class Client implements IClient {
	private static IClient client = null;
	private final String server = "http://192.168.1.111:8080/";
	private final String getDeviceKey = "getDeviceKey?key=";
	private final String getGameByUser = "getGameByUser?id=";
	private final String makeMove="makeMove?id=";
	private final String getSetName="getSetName?id=";
	
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

	@Override
	public void getGame(String clientKey, final ClienGameListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public synchronized void onComplete(String result) {
				try {
					listner.onResult(new Game(result));
				} catch (Exception e) {
					listner.connectionError();
				}
			}
			
			@Override
			public synchronized void connectionError() {
				listner.connectionError();
			}
		}, server + getGameByUser + clientKey);
		e.execute();
	}

	@Override
	public void makeMove(String clientKey, Integer x, Integer y,
			final ClienBooleanListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public synchronized void onComplete(String result) {
				if(result.equals("true"))
					listner.onResult(true);
				else
					listner.onResult(false);
			}
			
			@Override
			public synchronized void connectionError() {
				listner.connectionError();
			}
		}, server + makeMove + clientKey + "&x=" + x.toString() + "&y=" + y.toString());
		e.execute();
	}

	@Override
	public void setName(String clientKey, String name,
			final ClienStringListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public synchronized void onComplete(String result) {
				listner.onResult(result);
			}
			
			@Override
			public synchronized void connectionError() {
				listner.connectionError();
			}
		}, server + getSetName + clientKey + "&name=" + URLEncoder.encode(name));
		e.execute();
	}
	
	@Override
	public void getName(String clientKey,
			final ClienStringListner listner) {
		Executer e = new Executer(new ExecuterListner() {
			
			@Override
			public synchronized void onComplete(String result) {
				listner.onResult(result);
			}
			
			@Override
			public synchronized void connectionError() {
				listner.connectionError();
			}
		}, server + getSetName + clientKey);
		e.execute();
	}
}
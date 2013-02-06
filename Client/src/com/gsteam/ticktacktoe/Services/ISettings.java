package com.gsteam.ticktacktoe.Services;

public interface ISettings {
	String getString(String key);
	void setString(String key, String value);
	
	String getDeviceId();
	
	String getServerKey();
	void setServerKey(String key);
}

package com.gsteam.ticktacktoe.Services;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

public class Settings implements ISettings {
	private SharedPreferences reader;
	private Editor writer;
	private String deviceId;

	public Settings(Activity activity) {
		deviceId = generateDeviceId(activity);
		reader = activity.getPreferences(Context.MODE_PRIVATE);
		writer = reader.edit();
	}
	
	@Override
	public String getString(String key) {
		return reader.getString(key, null);
	}

	@Override
	public void setString(String key, String value) {
		writer.putString(key, value);
		writer.commit();
	}
	
	private String generateDeviceId(Activity activity) {
		final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String deviceId = deviceUuid.toString();
	    
	    return deviceId;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public String getServerKey() {
		return getString("ServerKey");
	}

	@Override
	public void setServerKey(String key) {
		setString("ServerKey", key);
	}

}

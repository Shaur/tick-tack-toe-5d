package com.gsteam.ticktacktoe.Services;


import android.text.method.DateTimeKeyListener;

public interface ClienStringListner {
	void onResult(String result);
	void connectionError();
}
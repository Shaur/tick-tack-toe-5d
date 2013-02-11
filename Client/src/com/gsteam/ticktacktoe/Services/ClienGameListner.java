package com.gsteam.ticktacktoe.Services;

public interface ClienGameListner {
	void onResult(Game result);
	void connectionError();
}
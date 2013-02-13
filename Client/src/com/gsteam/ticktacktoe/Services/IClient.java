package com.gsteam.ticktacktoe.Services;


public interface IClient {
	void getClientKey(String deviceId, ClienStringListner listner);
	void getGame(String clientKey, ClienGameListner listner);
	void makeMove(String clientKey, Integer x, Integer y, ClienBooleanListner listner);
	void setName(String clientKey, String name, ClienStringListner listner);
	void getName(String clientKey, ClienStringListner listner);
}
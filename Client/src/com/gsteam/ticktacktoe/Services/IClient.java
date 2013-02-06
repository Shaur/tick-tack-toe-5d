package com.gsteam.ticktacktoe.Services;


public interface IClient {
	//void hasConnected(IClientHasConnectedListner listner);
	void getClientKey(String deviceId, ClienStringListner listner);
}
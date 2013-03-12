package com.gsteam.ticktacktoe.Views;

import com.gsteam.ticktacktoe.Views.FieldObject.FieldType;

public interface GameViewListner
{
	void onFieldObjectClick(int x, int y, FieldType type);
}
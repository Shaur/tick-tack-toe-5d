package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.widget.RadioButton;

public class FieldObject extends RadioButton
{
	private int x, y;
	public FieldObject(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
	}
	
	public int GetX() {
		return x;
	}
	
	public int GetY() {
		return y;
	}
}
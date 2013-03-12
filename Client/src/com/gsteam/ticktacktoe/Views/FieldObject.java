package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.widget.Button;
import android.widget.RadioButton;

public class FieldObject extends Button
{
	public enum FieldType
	{
		EMPTY,
		X,
		O
	}
	
	private int x, y;
	private FieldType type;
	
	public FieldObject(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
		this.SetType(FieldType.EMPTY);
	}
	
	public int GetX() {
		return x;
	}
	
	public int GetY() {
		return y;
	}

	public FieldType GetType() {
		return type;
	}

	public void SetType(FieldType type) {
		this.type = type;
	}
}
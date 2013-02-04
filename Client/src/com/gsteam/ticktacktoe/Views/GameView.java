package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.LinearLayout.LayoutParams;

public class GameView extends LinearLayout
{
	private final int w = 5, h = 5;
	private RadioButton[][] field = new RadioButton[w][];
	
	public GameView(Context context) {
		super(context);
		
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		for(int i = 0; i < w; i++) {
			field[i] = new RadioButton[h];
			for(int j = 0; j < h; j++) {
				field[i][j] = new RadioButton(context);
				field[i][j].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, w));
				field[i][j].setChecked(false);
			}
		}
		
		LinearLayout[] lines = new LinearLayout[w];
		for(int i = 0; i < w; i++) {
			lines[i] = new LinearLayout(context);
			lines[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				lines[i].addView(field[i][j]);
			}
		}
		
		for(int i = 0; i < w; i++) {
			addView(lines[i]);		
		}
	}
}
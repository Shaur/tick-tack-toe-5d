package com.gsteam.ticktacktoe.Views;

import com.gsteam.ticktacktoe.Services.Game;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class GameView extends LinearLayout
{
	private final int nBtnWid = 70, nBtnHei = 70;
	private final int w = 5, h = 5;
	private FieldObject[][] field = new FieldObject[w][];
	private final GameViewListner listner;
	private final String strMarkX = "X";
	private final String strMarkO = "O";
	private Boolean bLocked; 
	
	public GameView(Context context, GameViewListner gameViewListner) {
		super(context);
		
		setVisibility(LinearLayout.GONE);
		
		if(gameViewListner == null) {
			throw new NullPointerException("gameViewListner");
		}
		
		listner = gameViewListner;
		
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		for(int i = 0; i < w; i++) {
			field[i] = new FieldObject[h];
			for(int j = 0; j < h; j++) {
				field[i][j] = new FieldObject(context, i, j);
				//field[i][j].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, w));
				field[i][j].setLayoutParams(new LayoutParams(nBtnWid, nBtnHei, w));
				field[i][j].setText("");
				field[i][j].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FieldObject o = (FieldObject)v;
						listner.onFieldObjectClick(o.GetX(), o.GetY(), o.GetType());
					}
				});
			}
		}
		
		LinearLayout[] lines = new LinearLayout[w];
		for(int i = 0; i < w; i++) {
			lines[i] = new LinearLayout(context);
			lines[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				lines[i].addView(field[j][i]);
			}
		}
		
		for(int i = 0; i < w; i++) {
			addView(lines[i]);		
		}
	}
	
	public void show() {
		setVisibility(LinearLayout.VISIBLE);
	}
	
	public void hide() {
		setVisibility(LinearLayout.GONE);
	}
	
	private void fieldLock(Boolean lock)
	{
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				field[i][j].setEnabled(!lock);
			}
		}
	}
	
	public void lock()
	{
		bLocked = true;
		fieldLock(bLocked);
	}
	
	public void unlock()
	{
		bLocked = false;
		fieldLock(bLocked);
	}
	
	public void update(Integer[][] gameField) {
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				if(1 == gameField[i][j]) {
					field[i][j].setText(strMarkX);
				}
				else
				{
					if(2 == gameField[i][j]) {
						field[i][j].setText(strMarkO);
					}
					else
					{
						field[i][j].setText("");
					}
				}
			}
		}
	}
}
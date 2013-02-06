package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class GameView extends LinearLayout
{
	private final int w = 5, h = 5;
	private FieldObject[][] field = new FieldObject[w][];
	private final GameViewListner listner;
	
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
				field[i][j].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, w));
				field[i][j].setChecked(false);
				field[i][j].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FieldObject o = (FieldObject)v;
						listner.onFieldObjectClick(o.GetX(), o.GetY());
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
}
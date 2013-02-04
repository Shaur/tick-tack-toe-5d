package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainMenuView extends LinearLayout
{
	private Button newGame, viewScores, settings;
	private final MainMenuListner listner;
	public MainMenuView(Context context, MainMenuListner mainMenuListner) {
		super(context);
		
		if(mainMenuListner == null) {
			throw new NullPointerException("mainMenuListner");
		}
		
		listner = mainMenuListner;
		
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		newGame = new Button(context);
		newGame.setText("new game");
		newGame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		newGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listner.onNewGameClick();
			}
		});
		
		viewScores = new Button(context);
		viewScores.setText("top 10");
		viewScores.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		viewScores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listner.onViewScoresClick();
			}
		});
		
		settings = new Button(context);
		settings.setText("settings");
		settings.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listner.onSettingsClick();
			}
		});
		
		addView(newGame);
		addView(viewScores);
		addView(settings);
	}	
}
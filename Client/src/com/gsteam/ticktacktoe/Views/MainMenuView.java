package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenuView extends LinearLayout
{
	private Boolean isVisible = false;
	private Button newGame, viewScores, settings;
	private final MainMenuListner listner;
	public MainMenuView(Context context, MainMenuListner mainMenuListner) {
		super(context);
		
		setVisibility(LinearLayout.GONE);
		
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
	
	public void show() {
		isVisible = true;
		setVisibility(LinearLayout.VISIBLE);
	}
	
	public void hide() {
		isVisible = false;
		setVisibility(LinearLayout.GONE);
	}

	public Boolean getIsVisible() {
		return isVisible;
	}
}
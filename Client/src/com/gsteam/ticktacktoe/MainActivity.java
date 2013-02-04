package com.gsteam.ticktacktoe;

import java.util.logging.Logger;

import com.gsteam.ticktacktoe.Views.GameView;
import com.gsteam.ticktacktoe.Views.GameViewListner;
import com.gsteam.ticktacktoe.Views.MainMenuListner;
import com.gsteam.ticktacktoe.Views.MainMenuView;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements MainMenuListner, GameViewListner
{
	private RelativeLayout mainActivityLayout;
	private MainMenuView mainMenu;
	private GameView gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivityLayout = (RelativeLayout)findViewById(R.id.mainActivityLayout);
		mainMenu = new MainMenuView(this, this);
		mainActivityLayout.addView(mainMenu);
		
		gameView = new GameView(this, this);
		gameView.setVisibility(LinearLayout.GONE);
		mainActivityLayout.addView(gameView);
	}
	@Override
	public void onNewGameClick() {
		mainMenu.setVisibility(LinearLayout.GONE);
		gameView.setVisibility(LinearLayout.VISIBLE);
	}
	@Override
	public void onViewScoresClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSettingsClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFieldObjectClick(int x, int y) {
		Log.e("GGG", "" + new Integer(x).toString() + "_" + new Integer(y).toString());
	}

}
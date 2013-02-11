package com.gsteam.ticktacktoe;

import org.json.JSONException;
import org.json.JSONObject;

import com.gsteam.ticktacktoe.Services.ClienGameListner;
import com.gsteam.ticktacktoe.Services.ClienStringListner;
import com.gsteam.ticktacktoe.Services.Client;
import com.gsteam.ticktacktoe.Services.Game;
import com.gsteam.ticktacktoe.Services.IClient;
import com.gsteam.ticktacktoe.Services.ISettings;
import com.gsteam.ticktacktoe.Views.GameView;
import com.gsteam.ticktacktoe.Views.GameViewListner;
import com.gsteam.ticktacktoe.Views.MainMenuListner;
import com.gsteam.ticktacktoe.Views.MainMenuView;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements MainMenuListner, GameViewListner
{
	private RelativeLayout mainActivityLayout;
	private MainMenuView mainMenu;
	private GameView gameView;
	private IClient client;
	private ISettings settings;
	private String key;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//options = PreferenceManager.getDefaultSharedPreferences(this);
		//options.
		//options = getPreferences(Context.MODE_PRIVATE).edit();
		//options.putString("password", mPassword);
		//options.commit();
		settings = (ISettings) new com.gsteam.ticktacktoe.Services.Settings(this);
		/*if(settings.getString("dk") == null)
		{
			settings.setString("dk", "123");
			Log.e("GGG", "SET!!!");
		} else {
			Log.e("GGG", "GET!!!" + settings.getString("dk"));
		}*/
		
		client = Client.GetInstance();
		initKey();
		
		initUI();
	}
	
	private void initKey() {
		showLoadingDialog();
		client.getClientKey(settings.getDeviceId(), new ClienStringListner() {
			@Override
			public void onResult(final String result) {
				runOnUiThread(new Runnable() { public void run() {
					key = result;
					hideLoadingDialog();
				}});
			}
			
			@Override
			public void connectionError() {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					showConnectionAlert(new OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							initKey();
						}
					});
				}});
			}
		});
	}
	
	private void initUI() {
		mainActivityLayout = (RelativeLayout)findViewById(R.id.mainActivityLayout);
		
		mainMenu = new MainMenuView(this, this);
		mainActivityLayout.addView(mainMenu);
		
		gameView = new GameView(this, this);
		mainActivityLayout.addView(gameView);
		
		mainMenu.show();
	}
	
	@Override
	public void onNewGameClick() {
		mainMenu.hide();
		gameView.show();
		showLoadingDialog();
		client.getGame(key, new ClienGameListner() {
			
			@Override
			public void onResult(Game result) {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
				}});
			}
			
			@Override
			public void connectionError() {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					showConnectionAlert(new OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onNewGameClick();
						}
					});
				}});				
			}
		});
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
		//Log.e("GGG", "" + new Integer(x).toString() + "_" + new Integer(y).toString());
	}
	
	private void showConnectionAlert(OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Ошибка соединения с сервером").setCancelable(false);
		builder.setPositiveButton("OK", listener);
		builder.create().show();
	}
	
	private void hideLoadingDialog() {
		progressDialog.dismiss();
	}
	
	private void showLoadingDialog() {
		progressDialog = ProgressDialog.show(this, "", "Загрузка, пожалуйста подождите...", true);
	}
}
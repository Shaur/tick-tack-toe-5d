package com.gsteam.ticktacktoe;

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
import com.gsteam.ticktacktoe.Views.SettingsListner;
import com.gsteam.ticktacktoe.Views.SettingsView;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements MainMenuListner, GameViewListner, SettingsListner
{
	private RelativeLayout mainActivityLayout;
	private MainMenuView mainMenu;
	private GameView gameView;
	private IClient client;
	private ISettings settings;
	private String key;
	private ProgressDialog progressDialog;
	private SettingsView settingsView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		settings = (ISettings) new com.gsteam.ticktacktoe.Services.Settings(this);
		
		client = Client.GetInstance();
		initKey();
		
		//makeMove usage sample
		/*client.makeMove("5113c973b93f4c6704000002", 2, 3, new ClienBooleanListner() {

			@Override
			public void onResult(Boolean result) {
				Log.e(">>>>>>>", result.toString());
			}

			@Override
			public void connectionError() {
				Log.e(">>>>>>>", "e");
			}
			
		});*/
		
		initUI();
		Log.i("ticktacktoe", "onCreate complete");
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
		
		settingsView = new SettingsView(this, this);
		mainActivityLayout.addView(settingsView);
		
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
		showLoadingDialog();
		client.getName(key, new ClienStringListner() {			
			@Override
			public void onResult(final String result) {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					settingsView.setText(result);
					mainMenu.hide();
					settingsView.show();
				}});
			}			
			@Override
			public void connectionError() {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					showConnectionAlert(new OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onSettingsClick();
						}
					});
				}});
			}
		});
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
	
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(false);
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}
	
	private void hideLoadingDialog() {
		progressDialog.dismiss();
	}
	
	private void showLoadingDialog() {
		progressDialog = ProgressDialog.show(this, "", "Загрузка, пожалуйста подождите...", true);
	}

	@Override
	public void onNewNameClick(final String name) {
		showLoadingDialog();
		client.setName(key, name, new ClienStringListner() {
			@Override
			public void onResult(final String result) {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					if (!result.equals("")) {
						settingsView.setText(name);
					} else {
						showAlert("Пользователь с таким именем уже зарегистрирован");
					}
				}});
			}
			@Override
			public void connectionError() {
				runOnUiThread(new Runnable() { public void run() {
					hideLoadingDialog();
					showConnectionAlert(new OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onNewNameClick(name);
						}
					});
				}});
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(mainMenu.getIsVisible())
		{
			super.onBackPressed();
			return;
		}
		
		gameView.hide();
		settingsView.hide();
		mainMenu.show();
	}
}
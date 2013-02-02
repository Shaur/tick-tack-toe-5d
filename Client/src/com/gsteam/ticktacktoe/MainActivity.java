package com.gsteam.ticktacktoe;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements IClientHasConnectedListner {

	private TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.textOut);
		IClient c = Client.GetInstance();
		c.hasConnected(this);
		//txt.setText(c.hasConnected() ? "1" : "2");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public synchronized void hasConnected() {
		text.setText("true");
	}

	@Override
	public synchronized void hasDisconnected() {
		text.setText("false");
	}

}

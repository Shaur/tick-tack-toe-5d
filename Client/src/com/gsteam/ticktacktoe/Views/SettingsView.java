package com.gsteam.ticktacktoe.Views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsView extends LinearLayout
{
	private final SettingsListner listner;
	private Button newName;
	private TextView label;
	private EditText text;
	
	public SettingsView(Context context, SettingsListner settingsListner) {
		super(context);
		
		setVisibility(LinearLayout.GONE);
		
		if(settingsListner == null) {
			throw new NullPointerException("settingsListner");
		}
		
		listner = settingsListner;
		
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		label = new TextView(context);
		label.setText("Your nickname:");
		label.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		text = new EditText(context);
		text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		newName = new Button(context);
		newName.setText("update nickname");
		newName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		newName.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String value = text.getText().toString().trim();
				text.setText(value);
				listner.onNewNameClick(value);
			}
		});
		
		addView(label);
		addView(text);
		addView(newName);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public void show() {
		setVisibility(LinearLayout.VISIBLE);
	}
	
	public void hide() {
		setVisibility(LinearLayout.GONE);
	}
}
package com.gsteam.ticktacktoe.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game {
	private String _id;
	private Date date;
	private String createdBy;
	private String opponent;
	private Integer move;
	private Integer[][] field;
		
	public Game (String json) throws JSONException, ParseException {
		field = new Integer[5][];
		for (int i = 0; i < 5; i++) {
			field[i] = new Integer[5];
		}
		
		JSONObject jObject = new JSONObject(json);
		createdBy = jObject.getString("createdBy");
		opponent = jObject.getString("opponent");
		_id = jObject.getString("_id");
		move = jObject.getInt("move");
		
		JSONArray lines = jObject.getJSONArray("field");
		for(int i = 0; i < lines.length(); i++) {
			JSONArray row = lines.getJSONArray(i);
			for(int j = 0; j < row.length(); j++) {
				field[i][j] = row.getInt(j);
			}
		}
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S", Locale.ENGLISH);
		date = formater.parse(jObject.getString("date"));
	}
	
	public String getId() {
		return _id;
	}
	public Date getDate() {
		return date;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public String getOpponent() {
		return opponent;
	}
	public Integer getMove() {
		return move;
	}
	public Integer[][] getField() {
		return field;
	}
}
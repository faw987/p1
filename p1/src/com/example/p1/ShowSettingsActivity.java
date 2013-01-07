package com.example.p1;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class ShowSettingsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_settings_layout);
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		Log.i("ShowSettingsActivity", "ShowSettingsActivity");

		builder.append("\n" + sharedPrefs.getString("plans1", "no-plans"));
		builder.append("\n" + sharedPrefs.getString("tasks1", "no-tasks"));
		System.out.println("ShowSettingsActivity: " + builder.toString());
	}
}

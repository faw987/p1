package com.example.p1;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

public class VendActivity extends Activity {
	private final String TAG = "VendActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      VendAct");
		setContentView(R.layout.activity_vend);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		String r = Utilities.callHttp("https://api.twitter.com/1/users/show.json?screen_name=faw987&include_entities=true");
		System.out.println("      VendAct  r=" + r);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(r);

			String id = jsonObj.getString("id");
			System.out.println("VendAct: id:" + id);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

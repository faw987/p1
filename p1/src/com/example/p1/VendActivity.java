package com.example.p1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class VendActivity extends Activity {
	private final String TAG = "VendActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      VendAct");
		setContentView(R.layout.activity_vend);

	}

	// ?? Log.i(TAG, "===== ENTER =====");


}

package com.example.p1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("MAIN", "made it into p1 MainActivity.");
		Log.i("MAIN", "version 1.022 .");
		System.out.println("Yo, starting MainActivity.");

		setContentView(R.layout.activity_main);

		Button startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText text1 = (EditText) findViewById(R.id.editText1);
				String et1 = text1.getText().toString();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text1.getWindowToken(), 0);

				Toast.makeText(getApplicationContext(),
						"Happy holidays " + et1 + ".", Toast.LENGTH_SHORT)
						.show();

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
				String theDate = sdf.format(new Date());
				Log.i("MAIN", "theDate:" + theDate + " Name: " + et1);
				System.out
				.println("Yo, about to exit onClick for button1. Name:"
						+ et1);
			}
		});

		Button startVend = (Button) findViewById(R.id.btnVend);
		startVend.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startVend();
			}
		});
		
		Button startPlan = (Button) findViewById(R.id.btnPlan);
		startPlan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startPlan();
			}
		});

		Log.i("MAIN", "about to exit MainActivity.");
		System.out.println("Yo, exiting MainActivity.");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void startVend() {
		Intent workerIntent = new Intent(this, VendActivity.class);
		startActivity(workerIntent);
	}
	
	private void startPlan() {
		Intent workerIntent = new Intent(this, PlanActivity.class);
		startActivity(workerIntent);
	}

//	private void startAnActivity(Intent i, Bundle b) {
//		Intent workerIntent = new Intent(i, b);
//		startActivity(workerIntent);
//	}
}
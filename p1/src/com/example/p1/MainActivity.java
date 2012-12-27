package com.example.p1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
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
		System.out.println("Yo, starting MainActivity.");

		setContentView(R.layout.activity_main);

		Button startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				System.out.println("Yo, about to exit onClick for button1. Name:"
						+ et1);
				// startWork1();
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
}
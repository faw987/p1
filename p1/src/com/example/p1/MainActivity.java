package com.example.p1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final String TAG = "MainActivity";

	public void onRestart(Bundle savedInstanceState) {
		super.onResume(); // Always call the superclass method first
		System.out
				.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MainActivity >>>> onResume");
		Log.i("MainActivity",
				">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MainActivity >>>> onResume");
		// / PlanActivity.redoTaskList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String version = "?";
		try {
			PackageInfo manager = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = manager.versionName;
		} catch (NameNotFoundException e) {
			Log.i("MAIN", "NameNotFoundException e:" + e);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
		String theDate = sdf.format(new Date());
		Log.i("MAIN", "theDate:" + theDate);

		Log.i("MAIN", "version " + version);
		System.out.println("Yo, starting MainActivity.");

		// experimenting

		Globals.delay1 = "abcdefg";

		Globals g = Globals.getInstance();
		g.setState("test1");
		String s = g.getState();
		

		Utilities.readPlansTasks(getApplicationContext());

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> before ----------------");

		Utilities.createByTaskArray();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> after ----------------");

		
		ArrayList<Task> tal = g.getPlanTaskAL("plan01");
		System.out.println("tal.size=" + tal.size());

		setContentView(R.layout.activity_main);
		String t = getTitle().toString();
		setTitle(t + " version " + version);

		Button startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText text1 = (EditText) findViewById(R.id.editText1);
				String et1 = text1.getText().toString();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text1.getWindowToken(), 0);

				Toast.makeText(getApplicationContext(),
						"Greetings, " + et1 + ".", Toast.LENGTH_SHORT).show();
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

		Button prefBtn = (Button) findViewById(R.id.btnPrefs);
		prefBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent settingsActivity = new Intent(getBaseContext(),
						Preferences.class);
				startActivity(settingsActivity);
			}
		});

		Button x1Btn = (Button) findViewById(R.id.btnX1);
		x1Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				System.out.println("{ \"plans\":[ ");
				for (int i = 0; i < 20; i++) {
					String rjzf1 = ("000" + i);
					String rjzf = rjzf1.substring(rjzf1.length()-2,rjzf1.length());
					String insertComma = i == 29 ? "" : "," ;
					System.out.println("{\"name\":\"plan" + rjzf
							+ "\", \"desc\":\"Desc of plan " + rjzf + "\"}" + insertComma);
			// {"name":"plan01", "desc":"Desc of plan 01"},
				}
				;
				System.out.println(" ] }");
				
				
				System.out.println("{ \"tasks\":[ ");
				for (int i = 0; i < 200; i++) {
					String rjzf1 = ("000" + i);
					String rjzf = rjzf1.substring(rjzf1.length()-3,rjzf1.length());
					String plan = rjzf1.substring(rjzf1.length()-3,rjzf1.length()-1);
				String insertComma = i == 199 ? "" : "," ;

					System.out.println("{\"name\":\"task" + rjzf + 
							 "\", \"plan\":\"plan" + plan +
							 "\", \"desc\":\"Desc of task " + rjzf + "\"}" + insertComma);
			// {"name":"plan01", "desc":"Desc of plan 01"},
				}
				;
				System.out.println(" ] }");
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

	@Override
	protected void onPause() {

		Utilities.writePlansTasks(getApplicationContext());
		Log.i("MAIN", "==MainActivity== onPause. -- just saved data in prefs.");
		super.onPause();

	}

	@Override
	protected void onStop() {

		Log.i("MAIN", "==MainActivity== onStop. -- just saved data in prefs.");
		super.onStop();

	}

	private void startVend() {
		Intent workerIntent = new Intent(this, VendActivity.class);
		startActivity(workerIntent);
	}

	private void startPlan() {
		Intent workerIntent = new Intent(this, PlanActivity.class);
		startActivity(workerIntent);
	}
}
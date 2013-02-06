package com.example.p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";
	private final String SPLAT = " ->->->->->->->->->-> ";

	public void onRestart(Bundle savedInstanceState) {

		super.onResume(); // Always call the superclass method first

		System.out.println(">>>>>>>>>>>>>>>> MainActivity >>>> onResume");
		Log.i(TAG, SPLAT + "onResume");
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
			Log.i(TAG, "NameNotFoundException e:" + e);
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
		String theDate = sdf.format(new Date());
		Log.i(TAG, "theDate:" + theDate);

		Log.i(TAG, "version " + version);
		System.out
				.println(">>>>>>                                                            >>>>>>>>>> Starting MainActivity.");

		//
		// experimenting
		//
		
		
		// try {
		// Geocoder gc = new Geocoder(this, Locale.US); // create new geocoder
		// instance
		// List<Address> foundAdresses =
		// gc.getFromLocationName("174 Rutgers Rd, Piscataway, Nj, 08854", 1);
		// // Search addresses
		// for (Address a : foundAdresses ){
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a);
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a.getLatitude());
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a.getLongitude());
		//
		// };
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ;
		// try {
		// Geocoder gc = new Geocoder(this, Locale.US); // create new geocoder
		// instance
		// List<Address> foundAdresses = gc.getFromLocationName("08854", 1); //
		// Search addresses
		// for (Address a : foundAdresses ){
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a);
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a.getLatitude());
		// System.out.println(">>>>>>>>>>>>>>>>  a=" + a.getLongitude());
		// };
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ;

		ActyTest.proto1(1, getApplicationContext());

		Globals.delay1 = "abcdefg";

		Globals g = Globals.getInstance();
		g.setState("test1");
		String s = g.getState();

		//
		// END experimenting
		//

		PackageManager pm = getApplicationContext().getPackageManager();

		String haves;
		boolean hasGps = pm
				.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		if (hasGps) {
			haves = "This device has a gps.";
		} else {
			haves = "This device does not have a gps.";
		}
		;
		makeToast(haves);

		Utilities.readPlansTasks(getApplicationContext());

		Utilities.createByTaskArray();

		ArrayList<Task> tal = g.getPlanTaskAL("plan01");
		System.out.println("\"Sanity check\" for PLAN01  Task array list size="
				+ tal.size());

		setContentView(R.layout.activity_main);

		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// gets the activity's default ActionBar

		ActionBar actionBar = getActionBar();

		actionBar.show();

		// set the app icon as an action to go home
		// we are home so we don't need it

		actionBar.setDisplayHomeAsUpEnabled(true);

		String t = getTitle().toString();
		setTitle(t + " version " + version);

		// Button startButton = (Button) findViewById(R.id.button1);
		// startButton.setOnClickListener(new View.OnClickListener() {
		//
		// public void onClick(View v) {
		// EditText text1 = (EditText) findViewById(R.id.editText1);
		// String et1 = text1.getText().toString();
		//
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(text1.getWindowToken(), 0);
		//
		// Toast.makeText(getApplicationContext(),
		// "Greetings, " + et1 + ".", Toast.LENGTH_SHORT).show();
		// }
		// });

		Button startPlan = (Button) findViewById(R.id.btnPlan);
		startPlan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startPlan();
			}
		});

		Log.i(TAG, "Exiting MainActivity.");
		System.out.println(TAG + " -- Exiting MainActivity.");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// same as using a normal menu
		Intent act;

		switch (item.getItemId()) {
		case R.id.menu_settings1:
			makeToast("PlayActivity is being started ...");
			act = new Intent(getBaseContext(), PlayActivity.class);
			startActivity(act);
			break;

		case R.id.listDrive:
			makeToast("listDrive...");
			act = new Intent(getBaseContext(), DriveActivity.class);
			act.putExtra("FUNCTION", "L");
			startActivity(act);
			break;

		case R.id.writeDrive:
			makeToast("writeDrive...");
			act = new Intent(getBaseContext(), DriveActivity.class);
			act.putExtra("FUNCTION", "W");
			startActivity(act);
			break;

		case R.id.readDrive:
			makeToast("writeDrive...");
			act = new Intent(getBaseContext(), DriveActivity.class);
			act.putExtra("FUNCTION", "R");
			startActivity(act);
			break;

		case R.id.postToWeb:
			makeToast("post to web...");
			act = new Intent(getBaseContext(), DriveActivity.class);
			act.putExtra("FUNCTION", "P");
			startActivity(act);
			break;

		case R.id.preferences:
			makeToast("Preferences...");
			act = new Intent(getBaseContext(), Preferences.class);
			startActivity(act);
			break;

		case R.id.x1:
			makeToast("X1 ... ? ? ? ...");

			// DOES NOT WORK -- NPE -- listAssets();

			processAssets();
			act = new Intent(getBaseContext(), MapActivity.class);
			act.putExtra("LATLNG", "40.5840 -74.522"); 					// PWAY
 			// startActivity(act);
			startActivityForResult(act, 1234);

			break;
		}
		return true;
	}

	private void processAssets() {
		AssetManager am = getApplicationContext().getAssets();
		final String BDAY_TUNE = "Chipmunks - Happy Birthday to You!!!.mp4.mp3";

		try {
			InputStream ist = am.open(BDAY_TUNE);
			AssetFileDescriptor fd = am.openFd(BDAY_TUNE);
			ist = am.open("test.txt");

			InputStreamReader is = new InputStreamReader(ist);
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();

			while (read != null) {
				System.out.println(read);
				sb.append(read);
				read = br.readLine();
			}

			// return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {

		Utilities.writePlansTasks(getApplicationContext());
		Log.i(TAG, "==MainActivity== onPause. -- just saved data in prefs.");
		super.onPause();

	}

	@Override
	protected void onStop() {

		Log.i(TAG, "==MainActivity== onStop. -- do nothing.");
		super.onStop();

	}

	private void startPlan() {
		Intent workerIntent = new Intent(this, MainPlanActivity.class);
		startActivity(workerIntent);
	}

	private void printSampleJSON() {
		System.out.println("{ \"plans\":[ ");
		for (int i = 0; i < 20; i++) {
			String rjzf1 = ("000" + i);
			String rjzf = rjzf1.substring(rjzf1.length() - 2, rjzf1.length());
			String insertComma = i == 29 ? "" : ",";
			System.out.println("{\"name\":\"plan" + rjzf
					+ "\", \"desc\":\"Desc of plan " + rjzf + "\"}"
					+ insertComma);
			// {"name":"plan01", "desc":"Desc of plan 01"},
		}
		;
		System.out.println(" ] }");

		System.out.println("{ \"tasks\":[ ");
		for (int i = 0; i < 200; i++) {
			String rjzf1 = ("000" + i);
			String rjzf = rjzf1.substring(rjzf1.length() - 3, rjzf1.length());
			String plan = rjzf1.substring(rjzf1.length() - 3,
					rjzf1.length() - 1);
			String insertComma = i == 199 ? "" : ",";

			System.out.println("{\"name\":\"task" + rjzf
					+ "\", \"plan\":\"plan" + plan
					+ "\", \"desc\":\"Desc of task " + rjzf + "\"}"
					+ insertComma);
			// {"name":"plan01", "desc":"Desc of plan 01"},
		}
		;
		System.out.println(" ] }");
	}

	private List<String> listAssets() {

		// NOT NOT NOT working

		List<String> it = new ArrayList<String>();
		File f = new File("file:///android_asset");
		// File f = new File("file:///sdcard/");
		File[] files = f.listFiles();
		Log.d(TAG, "list assets");
		System.out.println("files=" + files);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			// if(getImageFile(file.getPath()))
			// it.add(file.getPath());
			System.out.println("file=" + file.getPath());
		}
		return it;

	}

	private void dispLocation() {
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, false);
		Location location = service.getLastKnownLocation(provider);
		LatLng userLocation = new LatLng(location.getLatitude(),
				location.getLongitude());
	}

	private boolean getImageFile(String fName) {
		boolean re;

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	};

	@Override
	protected void onActivityResult(int aRequestCode, int aResultCode,
			Intent aData) {

		System.out.println("onActivityResult aRequestCode=" + aRequestCode
				+ " aResultCode=" + aResultCode);

		switch (aRequestCode) {
		case 1234:
			handleUserPickedImage(aData);
			break;
		case 2222:
			handleSomethingElse(aData);
			break;
		}
		super.onActivityResult(aRequestCode, aResultCode, aData);
	}

	private void handleUserPickedImage(Intent aData) {
		if ((aData != null)) {
			String extraData = aData.getStringExtra("ComingFrom");
			System.out.println("handleUserPickedImage extraData=" + extraData);
			// Do something neat with the image...
		} else {
			// We didn't receive an image...
		}
	}

	private void handleSomethingElse(Intent aData) {
		if ((aData != null) && (aData.getData() != null)) {
			// Uri _imageUri = aData.getData();
			// Do something neat with the image...
		} else {
			// We didn't receive an image...
		}
	}
}
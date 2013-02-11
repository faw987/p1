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

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

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

		ActyTest.proto1(1, getApplicationContext());

		//
		// END experimenting
		//

		Globals g = Globals.getInstance();

		PackageManager pm = getApplicationContext().getPackageManager();

		String haves = "";
		boolean hasGps = pm
				.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		if (hasGps) {
			haves += "This device has a gps.";
		} else {
			haves += "This device does not have a gps.";
		}
		;
		if (isNetworkAvailable()) {
			haves += "The network is available.";
		} else {
			haves += "The network is not available.";
		}
		;
		makeToast(haves);

		Utilities.readPlansTasks(getApplicationContext());

		Utilities.createByTaskArray();

		setContentView(R.layout.activity_main);

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

		Button startPlan = (Button) findViewById(R.id.btnPlan);
		startPlan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startPlan();
			}
		});
		
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		String dwco = sharedPrefs.getString("dwco", "");
		Globals.dwco = dwco;
		System.out.println(TAG + " setting Globals.dwco = " + dwco);

		String dwcoRequests = sharedPrefs.getString("dwcoRequests", "");
		Globals.dwcoRequests = dwcoRequests;
		System.out.println(TAG + " setting Globals.dwco = " + dwcoRequests);
		
		String dwcoReplies = sharedPrefs.getString("dwcoReplies", "");
		Globals.dwcoReplies = dwcoReplies;
		System.out.println(TAG + " setting Globals.dwcoReplies = " + dwcoReplies);
		
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

			// String

			// String
			// url="http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false";
			//
			// String result=Utilities.callHttp(url);
			//
			// System.out.println(TAG + SPLAT + " result=" + result);
			//
			
//			JSONObject jsonObj = null;
//			try {
////				jsonObj = new JSONObject(Utilities.callDirections(
////						"40.7251,-73.9943", "40.7227,-73.9920");
////			
//				jsonObj =  Utilities.callDirections("08854", "10012");
//				
//				System.out.println(TAG + SPLAT + " result="
//						+ jsonObj.toString(5));
//				
//				int meter = Utilities.directionsGetDistance(jsonObj);
//				int seconds = Utilities.directionsGetDuration(jsonObj);
//				  double miles = meter * 0.00062137119;
//				  System.out.println("Miles: " + miles);
//				  System.out.println("seconds: " + seconds);
//				  System.out.println("minutes: " + seconds/60);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			;

//			
//			String data = Utilities.callCityData("Camden", "New-Jersey");
//			
//			 //  System.out.println("data: " + data);
//			
//			String cd = Utilities.cityDataGetCrime(data);
//			
//			System.out.println(" crime data: " + cd);

			Task.generateFieldCode();
			
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

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

}
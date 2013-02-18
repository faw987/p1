package com.example.p1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

public class Globals extends Application {

	static String dwco; // if D then Distance, if W then weather if C then crime
	// demogrphics
	// expect to expand with Yelp FOursquare etc.
	static String dwcoRequests; // if D then Distance, if W then weather if C
	// then crime demogrphics
	static String dwcoReplies; // if D then Distance, if W then weather if C
	// then crime demogrphics

	private static Globals sInstance;
	static ArrayList<Plan> plans = new ArrayList<Plan>();
	static ArrayList<Task> tasks = new ArrayList<Task>();
	static String currentPlanName = "";
	static String currentTaskName = ""; // HACK - is this unique enough ??

	HashMap<String, Plan> planMap = new HashMap<String, Plan>();
	HashMap<String, Task> taskMap = new HashMap<String, Task>();

	HashMap<String, LocationForcastInfo> locationForcasts = new HashMap<String, LocationForcastInfo>();



	//  			g.addHourlyFeelsLike(location,o);

	public String addHourlyFeelsLike(String location, JSONObject weatherHourly) {
		String r="";

		// TOTAL HACK HACK just a cut and paste and edit 
		//		"year": "2013","mon": "2","mon_padded": "02","mon_abbrev": "Feb","mday": "17","mday_padded": "17","yday": "47"
		LocationForcastInfo lfi = locationForcasts.get(location);
		if (lfi == null) {
			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- put");

			(locationForcasts).put("A", new LocationForcastInfo());
		} else {

			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi=" + lfi);

		}
	//	int index=lfi.cs;	//  current  slot
		try {

			JSONArray hourlyf = weatherHourly.getJSONArray("hourly_forecast");
			for (int i = 0; i < hourlyf.length(); i++) {
				String t=  hourlyf.getJSONObject(i).getJSONObject("feelslike").getString("english").toString();
				String h=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("hour").toString();
				String mon_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mon_padded").toString();
				String mday_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mday_padded").toString();
				String mmdd=  mon_padded + mday_padded;
				System.out.println("hourlyWeatherGetFeelsLike --  h=" + h + "  t=" + t + " mon_padded=" + mon_padded + " mday_padded=" + mday_padded);
 				String reading = mon_padded + "/" + mday_padded + "@" + h + "=" + t + "\n" ;
 				r += reading;}

			return r ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;

	}

	public void addHourForecast(String mmdd, String f){};

	public void addHourlyForecast(String loc, String forecast) {

		LocationForcastInfo lfi = locationForcasts.get(loc);
		if (lfi == null) {
			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- put");

			(locationForcasts).put("A", new LocationForcastInfo());
		} else {

			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi=" + lfi);

		}
		;
		//
		//int pi = plans.size() - 1;
		// planMap.put(p.name, plans.get(pi));

	};




	public class HourlyForcastInfo {
		String dateForcastFor;     //  padded    mmdd 
		String[] hourForcastFor = new String[24]; // 24 of these
	}

	public class LocationForcastInfo {
		int cs = 0;
		HourlyForcastInfo[] hourlyForcastInfo = new HourlyForcastInfo[14];
	}

	public static Globals getInstance() {
		return sInstance;
	}

	public static Boolean debugVerbose() { // HACK until put in preferences
		return false;
	}

	public static Boolean debugPrintHttpResults() { // HACK until put in
		// preferences
		return false;
	}

	private String myState;

	public String getState() {
		return myState;
	}

	public void setState(String s) {
		myState = s;
	}

	public void addPlan(Plan p) {
		plans.add(p);
		int pi = plans.size() - 1;
		planMap.put(p.name, plans.get(pi));

	};

	public Plan getPlan(String p) {
		return planMap.get(p);

	}

	public ArrayList<Task> getPlanTaskAL(String ps) {
		Plan p = getPlan(ps);
		ArrayList<Task> a = p.arrayListOfTasks;
		if (a != null) {
			return a;
		} else {
			return new ArrayList<Task>();
		}
	}

	public void sortPlans() {
		Collections.sort(plans, new MyIntComparable());
	}

	public class MyIntComparable implements Comparator<Plan> {

		@Override
		public int compare(Plan o1, Plan o2) {
			// return (o1.name>o2.name ? -1 : (o1.name==o2.name ? 0 : 1));
			return (o1.name.compareTo(o2.name));
		}
	}

	public int plansSize() {
		return plans.size();
	}

	public ArrayList<Plan> getPlansArray() {
		return plans;
	}

	public void plansClear() {
		plans = new ArrayList<Plan>();
	}

	public void addTask(Task t) {
		tasks.add(t);
		ArrayList<Task> a = getPlanTaskAL(t.plan);
		a.add(t);
		int ti = tasks.size() - 1;
		taskMap.put(t.name, tasks.get(ti));
		System.out.println(" >>>>>>>>>>>>>>> added task=" + t.name);

	}

	public Task getTask(String t) {
		Task r = taskMap.get(t);
		System.out.println("*** getTask t=" + t + ", result=" + r);
		return r;
	}

	public int tasksSize() {
		return tasks.size();
	}

	public ArrayList<Task> getTasksArray() {
		return tasks;
	}

	public void tasksClear() {
		tasks = new ArrayList<Task>();
	}

	public void sortTasks() {
		Collections.sort(tasks, new MyIntComparableTasks());
	}

	public class MyIntComparableTasks implements Comparator<Task> {

		@Override
		public int compare(Task o1, Task o2) {
			// return (o1.name>o2.name ? -1 : (o1.name==o2.name ? 0 : 1));
			return (o1.name.compareTo(o2.name));
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		// add initialize stuff for singeltonn instance as:
		// sInstance.initializeInstance();
		Log.i("GLOBALS", "set sInstance to: " + sInstance);
		System.out.println("GLOBALS" + "set sInstance to: " + sInstance);
	}
}

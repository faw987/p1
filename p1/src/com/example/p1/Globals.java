package com.example.p1;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

public class Globals extends Application {
	static String delay1;
	private static Globals sInstance;
	static ArrayList<Plan> plans = new ArrayList<Plan>();
	static ArrayList<Task> tasks = new ArrayList<Task>();

	public static Globals getInstance() {
		return sInstance;
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
	}
	public int plansSize() {
		return plans.size();
	}
	public ArrayList<Plan> getPlansArray() {
		return plans;
	}
	
	
	public void addTask(Task p) {
		tasks.add(p);
	}
	public int tasksSize() {
		return tasks.size();
	}
	public ArrayList<Task> getTaskArray() {
		return tasks;
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

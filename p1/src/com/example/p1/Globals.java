package com.example.p1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Application;
import android.util.Log;

public class Globals extends Application {
	static String delay1;
	private static Globals sInstance;
	static ArrayList<Plan> plans = new ArrayList<Plan>();
	static ArrayList<Task> tasks = new ArrayList<Task>();
	static String currentPlanName = "";
	static String currentTaskName = "";		// HACK - is this unique enough ??
	// static View planView ;

	HashMap<String, Plan> planMap = new HashMap<String, Plan>();
	HashMap<String, Task> taskMap = new HashMap<String, Task>();

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
		int pi = plans.size() - 1;
		planMap.put(p.name, plans.get(pi));

	};

	public Plan getPlan(String p) {

		return planMap.get(p);

	}

	public ArrayList<Task> getPlanTaskAL(String ps) {
		Plan p = getPlan(ps);
		System.out.println("getPlanTaskAL -- ps=" + ps);
		ArrayList<Task> a = p.arrayListOfTasks;
 
 	//	if (a != null && !a.isEmpty()) {
			if (a != null ) {
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
	}
	
	public Task getTask(String t) {
		return taskMap.get(t);

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

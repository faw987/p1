package com.example.p1;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Frank
 * 
 */
public class MainPlanActivity extends Activity {

	private final String TAG = "MainPlanActivity";
	int idNumber = 0;
	TableLayout tl2;
	String et1;
	Spinner spinner1, spinner2;

	public void onResume() {
		super.onResume(); 				// Always call the superclass method first
		refreshPlansTasks();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		setContentView(R.layout.activity_plan);

		//		TextView text1 = (TextView) findViewById(R.id.textView11);
		//		text1.setSelected(true);


		addListenerOnSpinnerItemSelection();

		refreshPlansTasks();

		Button btnAdd = (Button) findViewById(R.id.addActivity);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnAdd clicked =====");
				Globals.currentTaskName = ""; 										// HACK ? clear taskName for add
				startAddActyActivity();
			}
		});

		Button btnRemoveActivity = (Button) findViewById(R.id.removeActivity);
		btnRemoveActivity.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnRemoveActivity clicked =====");

				int i = 0;

				i = tl2.getChildCount() - 1;
				Globals g = Globals.getInstance();
				ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
				while (i >= 0) {
					if (tl2.getChildAt(i) != null) {
						View row = tl2.getChildAt(i);
						CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
						if (c.isChecked()) {
							tal.remove(i);
						}
					}
					i--;
				}
				refreshTasks();
			}
		});

		Button btnUp = (Button) findViewById(R.id.btnUp);
		btnUp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				moveActivityUp();
			}
		});

		Button btnDown = (Button) findViewById(R.id.btnDown);
		btnDown.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				moveActivityDown();
			}
		});

		Button btnAddPlan = (Button) findViewById(R.id.addPlan);
		btnAddPlan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startAddPlanActivity();
			}
		});

		Button btnDWD = (Button) findViewById(R.id.btnDWD);
		btnDWD.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				activityDWD();
			}
		});

	}

	public void addRecRow(TableLayout tl, String rowData) {
		TableRow tr2 = new TableRow(this);

		Button btnSelect = new CheckBox(this);
		btnSelect.setText("Pick");

		// getResources().getDrawable(R.drawable.round_button_background);
		// btnSelect.setBackgroundDrawable(sBackground);

		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				TableRow tr = (TableRow) v.getParent();
				TextView c = (TextView) tr.getChildAt(2);
				String s = c.getText().toString();

				// System.out.println("      s = " + s); // so this IS what we
			}
		});

		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);

		LayoutParams lp2 = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(2, 2, 2, 2);

		btnSelect.setLayoutParams(lp);

		/* Add Button to row. */
		tr2.addView(btnSelect);

		Button btnInfo = new Button(this);
		btnInfo.setText("Open");
		btnInfo.setTextSize(10.0f);
		btnInfo.setTextColor(Color.BLACK);

		btnInfo.setBackgroundResource(R.color.yellow);

		btnInfo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				TableRow tr = (TableRow) v.getParent();
				TextView c = (TextView) tr.getChildAt(2);
				String s = c.getText().toString();

				// System.out.println("      s = " + s); // so this IS what we

				Globals.currentTaskName = s; 			// HACK - for now ASSUME row IS A simple task name
				startAddActyActivity();
			}
		});

		tr2.addView(btnInfo);

		tl.addView(tr2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tl.invalidate(); // This line does not work

		TextView t1 = new TextView(this);
		t1.setTextSize(16.0f);
		t1.setText(rowData);
		t1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr2.addView(t1); // Add Button to row.

	}

	public void addListenerOnSpinnerItemSelection() {
		spinner1 = (Spinner) findViewById(R.id.planslist);
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	public void addListenerOnButton() {
		spinner1 = (Spinner) findViewById(R.id.planslist);
	}

	private void startAddPlanActivity() {
		Intent workerIntent = new Intent(this, AddPlanActivity.class);
		startActivity(workerIntent);
	}

	private void startAddActyActivity() {
		Intent workerIntent = new Intent(this, AddActyActivity.class);
		startActivity(workerIntent);
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			String plan = parent.getItemAtPosition(pos).toString();

			Globals.currentPlanName = plan;
			refreshTasks();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	public void refreshPlansTasks() {
		Globals g = Globals.getInstance();
		spinner2 = (Spinner) findViewById(R.id.planslist);
		List<String> list = new ArrayList<String>();

		int sz = g.plansSize();
		System.out.println(TAG + " refreshPlansTasks -- plans sz=" + sz);

		g.sortPlans();

		ArrayList<Plan> planz = g.getPlansArray();

		for (Plan x : planz) {
			list.add(x.name);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);

		int index = list.indexOf(Globals.currentPlanName);
		if (index != -1) {
			spinner2.setSelection(index);
		} else {
			spinner2.setSelection(0);

			// set current plan to whatever  at 0

			Globals.currentPlanName = spinner2.getItemAtPosition(0).toString(); 
		}
		;

		refreshTasks();
	}

	public void refreshTasks() {
		Globals g = Globals.getInstance();
		tl2 = (TableLayout) findViewById(R.id.recTable);
		tl2.removeAllViews();
		tl2.setVerticalScrollBarEnabled(true);

		int tsz = g.tasksSize();

		System.out.println(TAG + " refreshTasks -- activities tsz=" + tsz);
		System.out.println(TAG + " refreshTasks -- Globals.currentPlanName="
				+ Globals.currentPlanName);

		ArrayList<Task> tal = g.getPlanTaskAL(Globals.currentPlanName);
		for (Task x : tal) {
			addRecRow(tl2, x.name);
		}
	}

	public void makeToastShort(String message) {	
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();		 
	}
	
	public void makeToast(String message) {	
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();		 
	}

	private void moveActivityUp() {
		int selectedRow = -1;

		if (nbrSelectedRows() != 1){ 
			makeToastShort("You must move exactly one activity at a time in this version.");
			return;
		};

		Globals g = Globals.getInstance();
		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		int i = 0;

		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (c.isChecked()) {
					System.out.println("      row checked, i=" + i);
					selectedRow = i;
					break; 														// SIMPLIFICATION - take first one found
				}
				i++;
			}
		}

		if (selectedRow == 0){ 
			makeToastShort("You are at the beginning, can not move up");
			return;
		};

		Task t = tal.get(selectedRow - 1);
		tal.set(selectedRow - 1, tal.get(selectedRow));
		tal.set(selectedRow, t);

		refreshTasks();

		i = 0;

		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (i == (selectedRow - 1)) {
					c.setChecked(true);
					System.out.println("      row set to checked, i=" + i);
					break; 															// HACK - take first one found
				}
				i++;
			}
		}
	}

	private void moveActivityDown() {
		int i = 0;
		int selectedRow = -1;

		if (nbrSelectedRows() != 1){ 
			makeToastShort("You must move exactly one activity at a time in this version.");
			return;
		};

		Globals g = Globals.getInstance();
		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (c.isChecked()) {
					System.out.println("      row is checked, i=" + i);
					selectedRow = i;
					break; 														// HACK - take first one found
				}
				i++;
			}
		}

		System.out.println("        selectedRow =  " + selectedRow);

		if (selectedRow == tl2.getChildCount()-1){ 
			makeToastShort("You are at the end, can not move down");
			return;
		};

		Task t = tal.get(selectedRow + 1);
		tal.set(selectedRow + 1, tal.get(selectedRow));
		tal.set(selectedRow, t);

		refreshTasks();

		i = 0;

		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (i == (selectedRow + 1)) {
					c.setChecked(true);
					System.out.println("      row set ot checked, i=" + i);
					break; 													// HACK - take first one found
				}
				i++;
			}
		}
	}


	private int nbrSelectedRows() {
		int r = 0;
		Globals g = Globals.getInstance();
		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		int i = 0;

		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (c.isChecked()) {
					r++;
				}
				i++;
			}
		}
		return r;
	}

	private void activityDWD() {
		int selectedRow = -1;

		if (nbrSelectedRows() != 1){ 
			makeToastShort("You must move exactly one activity at a time in this version.");
			return;
		};

		Globals g = Globals.getInstance();
		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		int i = 0;

		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (c.isChecked()) {
					System.out.println("      row checked, i=" + i);
					selectedRow = i;
					break; 														// SIMPLIFICATION - take first one found
				}
				i++;
			}
		}

		if (selectedRow == -1){ 
			makeToastShort("You must select an activity to use DWD.");
			return;
		};
		
		String toast="";
		
		Task t = tal.get(selectedRow);
		String location = t.location;

	
		//
		// DWD -- D = Distance
		//
		
		JSONObject jsonObj = null;
		try {
//			jsonObj = new JSONObject(Utilities.callDirections(
//					"40.7251,-73.9943", "40.7227,-73.9920");
//		
			jsonObj =  Utilities.callDirections(location, "10012");
			
			System.out.println(TAG +   " result="
					+ jsonObj.toString(5));
			
			int meter = Utilities.directionsGetDistance(jsonObj);
			int seconds = Utilities.directionsGetDuration(jsonObj);
			  double miles = meter * 0.00062137119;
			  System.out.println("Miles: " + miles);
			  System.out.println("seconds: " + seconds);
			  System.out.println("minutes: " + seconds/60);
				
			  toast += "Distance to 10012   activity, miles = " + miles + "\n";
			  toast += "Time to 10012   activity, minutes = " + seconds/60 + "\n";

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		
		//
		// DWD -- W = Weather
		//
	
		System.out.println("      selected activity location=" + location);
		try {
			JSONObject o = Utilities.callWeather("NJ", "08854"); // HARD CODE HACK
			System.out.println("      selected activity weather="
					+ o.toString(5));
			double tempf = Utilities.weatherGetTempF(o);
			toast += "Current temperature, degrees Fahrenheit = " + tempf + "\n";
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		
		//
		// DWD -- D = Danger
		//
		
		String data = Utilities.callCityData("Camden", "New-Jersey");
		String cd = Utilities.cityDataGetCrime(data);
		
		System.out.println(" crime data: " + cd);
		
		toast += "Most recent murder rate per 100k = " + cd + "\n";

		
		// tell user whats what for now
		
		makeToast(toast);

		
		//		tal.set(selectedRow - 1, tal.get(selectedRow));
		//		tal.set(selectedRow, t);
		//	
		//		refreshTasks();
		//	
		//		i = 0;
		//	
		//		while (i < tl2.getChildCount()) {
		//			if (tl2.getChildAt(i) != null) {
		//				View row = tl2.getChildAt(i);
		//				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
		//				if (i == (selectedRow - 1)) {
		//					c.setChecked(true);
		//					System.out.println("      row set to checked, i=" + i);
		//					break; 															// HACK - take first one found
		//				}
		//				i++;
		//			}
		//		}
	}

}

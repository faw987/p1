package com.example.p1;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
    ProgressDialog progressDialog;
    private static final int DIALOG_ALERT = 10;
	String toast = "";


	final Globals g = Globals.getInstance();


	public void onResume() {
		super.onResume(); // Always call the superclass method first
		refreshPlansTasks();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		setContentView(R.layout.activity_plan);

		// TextView text1 = (TextView) findViewById(R.id.textView11);
		// text1.setSelected(true);

//		progressDialog = new ProgressDialog(v.getContext());
		progressDialog = new ProgressDialog( this);
		
		addListenerOnSpinnerItemSelection();

		refreshPlansTasks();

		Button btnAdd = (Button) findViewById(R.id.addActivity);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnAdd clicked =====");
				Globals.currentTaskName = ""; // HACK ? clear taskName for add
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
				activityDWD2();
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
		btnInfo.setTextSize(20.0f);
		btnInfo.setTextColor(Color.BLACK);

		btnInfo.setBackgroundResource(R.color.yellow);

		btnInfo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				TableRow tr = (TableRow) v.getParent();
				TextView c = (TextView) tr.getChildAt(2);
				String s = c.getText().toString();

				// System.out.println("      s = " + s); // so this IS what we

				Globals.currentTaskName = s; // HACK - for now ASSUME row IS A
												// simple task name
				startAddActyActivity();
			}
		});

		tr2.addView(btnInfo);

		tl.addView(tr2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tl.invalidate(); // This line does not work

		TextView t1 = new TextView(this);
		t1.setTextSize(24.0f);
		t1.setText(rowData);
		t1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		tr2.addView(t1); // Add Button to row.

		
		// experimental
		
		TextView t2 = new TextView(this);
		t2.setTextSize(12.0f);
		
		
		Task ta = g.getTask(rowData);				// HACK
		
		
		t2.setText("  / " + ta.location + " / " + ta.urls + " / " + ta.duration + " / " + ta.desc);
		
		t2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		tr2.addView(t2); // Add Button to row.

//		ViewGroup v1 ;
//		TextView t2 = new TextView(this);
//		t2.setTextSize(16.0f);
//		t2.setText(rowData);
//		t2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		v1.addView(t2);
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

			// set current plan to whatever at 0

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

		if (nbrSelectedRows() != 1) {
			makeToastShort("You must move exactly one activity at a time in this version.");
			return;
		}
		;

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
					break; // SIMPLIFICATION - take first one found
				}
				i++;
			}
		}

		if (selectedRow == 0) {
			makeToastShort("You are at the beginning, can not move up");
			return;
		}
		;

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
					break; // HACK - take first one found
				}
				i++;
			}
		}
	}

	private void moveActivityDown() {
		int i = 0;
		int selectedRow = -1;

		if (nbrSelectedRows() != 1) {
			makeToastShort("You must move exactly one activity at a time in this version.");
			return;
		}
		;

		Globals g = Globals.getInstance();
		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		while (i < tl2.getChildCount()) {
			if (tl2.getChildAt(i) != null) {
				View row = tl2.getChildAt(i);
				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
				if (c.isChecked()) {
					System.out.println("      row is checked, i=" + i);
					selectedRow = i;
					break; // HACK - take first one found
				}
				i++;
			}
		}

		System.out.println("        selectedRow =  " + selectedRow);

		if (selectedRow == tl2.getChildCount() - 1) {
			makeToastShort("You are at the end, can not move down");
			return;
		}
		;

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
					break; // HACK - take first one found
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

//	private void activityDWD() {
//		int selectedRow = -1;
//
//		if (nbrSelectedRows() == 0) {
//			makeToastShort("You must select one or more activities to use DWD.");
//			return;
//		}
//		;
//
//		Globals g = Globals.getInstance();
//		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
//		int i = 0;
//
//		while (i < tl2.getChildCount()) {
//			if (tl2.getChildAt(i) != null) {
//				View row = tl2.getChildAt(i);
//				CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
//				if (c.isChecked()) {
//					System.out.println("      row checked, i=" + i);
//					selectedRow = i;
//					break; // SIMPLIFICATION - take first one found
//				}
//				i++;
//			}
//		}
//
//		if (selectedRow == -1) {
//			makeToastShort("You must select an activity to use DWD.");
//			return;
//		}
//		;
//
//		String toast = "";
//
//		Task t = tal.get(selectedRow);
//		String location = t.location;
// 
//		new MyTask().execute(toast, location);
// 
//	}

	private String calcDanger(String location) {
		//
		// DWD -- D = Danger
		//
		String toast = "";

		// String data = Utilities.callCityData("Camden", "New-Jersey");
		
		String city = Utilities.cityDataCityFromLocation(location);
		String state = Utilities.cityDataStateFromLocation(location);

		String data = Utilities.callCityData(city, state);
		String cd = Utilities.cityDataGetCrime(data);

		System.out.println(" crime data: " + cd);

		toast += "At location " + location + " murders per 100k = " + cd + "\n";
		return toast;
	}

	private String calcWeather(String location) {
		//
		// DWD -- W = Weather
		//
		String toast = "";
		System.out.println("Weather for location=" + location);
		
		String dwcoRequests = Globals.dwcoRequests;
		System.out.println("MyTask2, dwcoRequests=" + dwcoRequests);
		String dwcoReplies = Globals.dwcoReplies;
		System.out.println("MyTask2, dwcoReplies=" + dwcoReplies);
		
		try {
 			JSONObject o = Utilities.callWeather(location); 
  
			if (dwcoReplies.contains("W")) System.out.println("      selected activity weather="
					+ o.toString(5));
			
			double tempf = Utilities.weatherGetTempF(o);
			
			toast += "Current temperature at " + location + ", degrees Fahrenheit = " + tempf
					+ "\n";
			
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		return toast;
	}

	private String calcDistance(String locationfrom, String locationto) {
		//
		// DWD -- D = Distance
		//
		String toast = "";
		JSONObject jsonObj = null;
		try {
			//
			// callDirections("40.7251,-73.9943", "40.7227,-73.9920");
			// callDirections("08854", "10012");
			//
			jsonObj = Utilities.callDirections(locationfrom, locationto);

			// System.out.println(TAG + " result="
			// + jsonObj.toString(5));

			int meter = Utilities.directionsGetDistance(jsonObj);
			int seconds = Utilities.directionsGetDuration(jsonObj);
			double miles = meter * 0.00062137119;

			System.out.println("Miles: " + miles);
			System.out.println("seconds: " + seconds);
			System.out.println("minutes: " + seconds / 60);

			toast += "Distance from " + locationfrom + " to " + locationto
					+ ", miles = " + miles + "\n";
			toast += "Time from " + locationfrom + " to " + locationto
					+ ", minutes = " + seconds / 60 + "\n";

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return toast;
	}

	private void activityDWD2() {
		int selectedRow = -1;
	//	String toast = "";

		String location = "X";
 
		new MyTask2().execute(toast, location);

	}

//	private class MyTask extends AsyncTask<String, Integer, String> {
//		String toast = "";
//
//		protected String doInBackground(String... in) {
// 
//			String location = in[1];
//
// 			publishProgress(1);
//
//			toast += calcDistance(location, "10012");
//			publishProgress(30);
//
//			toast += calcWeather(location);
//			publishProgress(60);
//
//			toast += calcDanger(location);
//			publishProgress(90);
//
//			// totalSize += Downloader.downloadFile(urls[i]);
//			// publishProgress((int) ((i / (float) count) * 100));
//			// // Escape early if cancel() is called
//
//			if (isCancelled())
//				return "canceled";
//			// }
//			return toast;
//		}
//
//		protected void onProgressUpdate(Integer... progress) {
//			// setProgressPercent(progress[0]);
//			System.out.println("      MyTask, progress =" + progress[0]);
//		//	makeToastShort("      MyTask, progress =" + progress[0]);
//			progressDialog.setProgress(progress[0]);
//
//		}
//
//		protected void onPostExecute(String result) {
//			// showDialog("Downloaded " + result + " bytes");
//			// makeToast("onPostExecute, result=" + result);
//			makeToast("onPostExecute, toast=" + toast);
//			System.out.println("onPostExecute, toast=" + toast);
//
//		}
//	}

	private class MyTask2 extends AsyncTask<String, Integer, String> {
		//String toast = "";
		String dwco;
		 @Override
         protected void onPreExecute()
         {
 
             progressDialog.setCancelable(true);
             progressDialog.setMessage("Updating distances, weather, demographics, etc for selected activities...");
             progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
             progressDialog.setProgress(0);
             progressDialog.setMax(100);
             progressDialog.show();
  
         }

		protected String doInBackground(String... in) {
	 
			String location = in[1];
			Task tNextActivity;
			int nbrSelectedRows = nbrSelectedRows();
			int nbrRowsProcessed=0;
 
			int selectedRow = -1;
			toast="";							// clear 

			if (nbrSelectedRows == 0) {
				makeToastShort("You must select one or more activities to use DWD.");
				return "canceled";
			}
			;

			dwco = Globals.dwco;
			System.out.println("MyTask2, dwco=" + dwco);
 
			Globals g = Globals.getInstance();
			ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
			int i = 0;
 
			while (i < tl2.getChildCount()) {
				if (tl2.getChildAt(i) != null) {
					View row = tl2.getChildAt(i);
					CheckBox c = (CheckBox) ((ViewGroup) row).getChildAt(0);
					if (c.isChecked()) {
						int n = tl2.getChildCount() - 1;
						nbrRowsProcessed++;
						
						int percent1 = (int)(((float)nbrRowsProcessed/(float)nbrSelectedRows)*100);

						System.out.println("    >>>>>>>>>>>>>>>>>>>>>>>> row checked, i=" + i);
						System.out.println("    >>>>>>>>>>>>>>>>>>>>>>>> n=" + n);
						System.out.println("    >>>>>>>>>>>>>>>>>>>>>>>> percent1=" + percent1 );

						selectedRow = i;
						Task t = tal.get(selectedRow);
						if (i < n) {
							tNextActivity = tal.get(selectedRow + 1);
						} else {
					//		makeToastShort("Calculating distance for last activity to first activity.");
							tNextActivity = tal.get(0);
						}
						;

						String locationfrom = t.location;
						String locationto = tNextActivity.location;

						System.out.println("     from=" + locationfrom + " to="
								+ locationto);
						 publishProgress(percent1);
						  
						  if (dwco.contains("D")) toast += calcDistance( locationfrom, locationto);
						  publishProgress(percent1+30);
						  
						  if (dwco.contains("W")) toast += calcWeather( locationfrom);
						  publishProgress(percent1+60);
						  
						  if (dwco.contains("C")) toast += calcDanger( locationfrom);
						  publishProgress(percent1+90);
						 
						// // Escape early if cancel() is called
					}
					i++;
				}
			}
//
//			if (selectedRow == -1) {
//				makeToastShort("You must select one or more activities to use DWD.");
//				return "error";
//			}
//			;

			if (isCancelled())
				return "canceled";
			return toast;
		}

		protected void onProgressUpdate(Integer... progress) {
			System.out.println("      MyTask2, progress =" + progress[0]);
			progressDialog.setProgress(progress[0]);

		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String result) {
		  	
            progressDialog.dismiss();

			makeToast( toast);
			System.out.println("onPostExecute, toast=" + toast);
			showDialog(DIALOG_ALERT);
		} 
		}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	  switch (id) {
	    case DIALOG_ALERT:
	    // Create out AlterDialog
	    Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your Results:\n" + toast);
	    builder.setCancelable(true);
	    builder.setPositiveButton("OK", new OkOnClickListener());
	  //  builder.setNegativeButton("No, no", new CancelOnClickListener());
	    AlertDialog dialog = builder.create();
	    dialog.show();
	   }
	  return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
	  DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
	  Toast.makeText(getApplicationContext(), "Activity will continue",
	    Toast.LENGTH_LONG).show();
	  }
	}

	private final class OkOnClickListener implements
	    DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
	//  AlertExampleActivity.this.finish();
	  }
	} 
}

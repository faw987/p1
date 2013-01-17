package com.example.p1;

import java.util.ArrayList;
import java.util.List;

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
public class PlanActivity extends Activity {
	private final String TAG = "PlanActivity";
	int idNumber = 0;
	TableLayout tl2;
	String et1;
	Spinner spinner1, spinner2;

	public void onResume() {

		super.onResume(); // Always call the superclass method first
		refreshPlansTasks();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      Plan Act");
		setContentView(R.layout.activity_plan);

		addListenerOnSpinnerItemSelection();

		refreshPlansTasks();

		Button btnAdd = (Button) findViewById(R.id.addActivity);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnAdd clicked =====");
				startAddActyActivity();

			}
		});

		
		Button btnRemoveActivity = (Button) findViewById(R.id.removeActivity);
		btnRemoveActivity.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnRemoveActivity clicked =====");
				 int i=0;
				while(i<tl2.getChildCount()){
                    if (tl2.getChildAt(i) != null) {
                        View row = tl2.getChildAt(i);
        				System.out.println("      row i = " + i);
        				System.out.println("      row i = cc=" + 	((ViewGroup)row).getChildCount());
        				CheckBox c = (CheckBox) ((ViewGroup)row).getChildAt(0);
if (c.isChecked()) System.out.println("      row i = checked");
//                        text1 = row.findViewById(R.id.stdName)).getText().toString().toLowerCase());                       
//                        text2 = row.findViewById(R.id.stdLastName)).getText().toString().toLowerCase());              
  //                      CheckBox checkbox = (CheckBox) row.getChildAt(i);
//                        showPopup(text1, text2, checkbox);
                        i++;
                    }
                }
			}
		});
		
		Button btnAddPlan = (Button) findViewById(R.id.addPlan);
		btnAddPlan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnAddPlan clicked =====");
				startAddPlanActivity();
			}
		});
	}

	public void addRecRow(TableLayout tl, String rowData) {
		TableRow tr2 = new TableRow(this);

		Button btnSelect = new CheckBox(this);
		btnSelect.setText("Pick");
//		btnSelect.setTextSize(8.0f);
//		btnSelect.setTextColor(Color.BLACK);

		// 1 Drawable sBackground =
		// getResources().getDrawable(R.drawable.round_button_background);
		// 1 btnSelect.setBackgroundDrawable(sBackground);

		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				View p = (View) v.getParent();
				TableRow tr = (TableRow) v.getParent();
				TextView c = (TextView) tr.getChildAt(2);

				String s = c.getText().toString();

				System.out.println("      s = " + s); // so this IS what we

				Toast.makeText(getApplicationContext(),
						"Thanks for the Edit button press. Data: " + s,
						Toast.LENGTH_SHORT).show();
			}
		});

		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);

		btnSelect.setLayoutParams(lp);

		/* Add Button to row. */
		tr2.addView(btnSelect);

		Button btnInfo = new Button(this);
		btnInfo.setText("Info");
		btnInfo.setTextSize(8.0f);
		btnInfo.setTextColor(Color.BLACK);
		// 1 btnInfo.setBackgroundDrawable(sBackground);
		btnInfo.setLayoutParams(lp);

		btnInfo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				View p = (View) v.getParent();
				TableRow tr = (TableRow) v.getParent();
				TextView c = (TextView) tr.getChildAt(2);
				String s = c.getText().toString();

				System.out.println("      s = " + s); // so this IS what we

				Toast.makeText(getApplicationContext(),
						"Thanks for the Info button press. Data: " + s,
						Toast.LENGTH_SHORT).show();
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

		/* Add Button to row. */
		tr2.addView(t1);

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
			Toast.makeText(
					parent.getContext(),
					"OnItemSelectedListener : "
							+ parent.getItemAtPosition(pos).toString(),
							Toast.LENGTH_SHORT).show();

			String plan = parent.getItemAtPosition(pos).toString();

			Globals g = Globals.getInstance();
			g.currentPlanName = plan;
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
		System.out.println("PlanActivity -- plans sz=" + sz);

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

		int index = list.indexOf(g.currentPlanName);
		if (index != -1) {
			spinner2.setSelection(index);
		} else {
			spinner2.setSelection(0);
		}
		;
g.currentPlanName="plan01"; // HACK
		refreshTasks();
	}

	public void refreshTasks() {
		Globals g = Globals.getInstance();
		tl2 = (TableLayout) findViewById(R.id.recTable);
		tl2.removeAllViews();
		tl2.setVerticalScrollBarEnabled(true);

		int tsz = g.tasksSize();
		System.out.println("PlanActivity -- activities tsz=" + tsz);
		//		ArrayList<Task> taskz = g.getTasksArray();
		//
		//		for (Task x : taskz) {
		//			if (g.currentPlanName.equals(x.plan)) {
		//				addRecRow(tl2, x.name);
		//			} else {
		//				Log.i(TAG, "activity named: " + x.name
		//						+ " is NOT part of plan:" + g.currentPlanName);
		//			}
		//			;
		//		}
		//		;
		System.out.println("refreshTasks -- g.currentPlanName=" + g.currentPlanName);

		ArrayList<Task> tal = g.getPlanTaskAL(g.currentPlanName);
		for (Task x : tal) {
			addRecRow(tl2, x.name);

			;
		}


	}
}

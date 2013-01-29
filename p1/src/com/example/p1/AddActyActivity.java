package com.example.p1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActyActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Boolean isNewTask, isUpdateTask;
		final Globals g = Globals.getInstance();
		final String taskName = g.currentTaskName;
		
		System.out.println("*** AddActyActivity taskName=" + taskName);

		setContentView(R.layout.activity_addacty);

		isNewTask = "".equals(taskName);
		isUpdateTask = !isNewTask;
		final Task t = null;
		if (isUpdateTask) {
			Task ta;
			ta = g.getTask(taskName);
			EditText text1 = (EditText) findViewById(R.id.taskName);
			text1.setText(taskName);
			EditText duration = (EditText) findViewById(R.id.duration);
			duration.setText(ta.duration);
			EditText urls = (EditText) findViewById(R.id.urls);
			urls.setText(ta.urls);
			EditText location = (EditText) findViewById(R.id.location);
			location.setText(ta.location);

		}
		;
		Button btnOK = (Button) findViewById(R.id.ok);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText etaskName = (EditText) findViewById(R.id.taskName);
				String taskName = etaskName.getText().toString();
				EditText eduration = (EditText) findViewById(R.id.duration);
				String duration = eduration.getText().toString();
				EditText eurls = (EditText) findViewById(R.id.urls);
				String urls = eduration.getText().toString();
				
				EditText elocation = (EditText) findViewById(R.id.location);
				String location = elocation.getText().toString();
				
				if (isNewTask) {
					Task t = new Task();
					t.name = taskName;
					t.desc = "Description of " + taskName;
					Globals g = Globals.getInstance();
					t.plan = g.currentPlanName;
					t.duration = duration;
					t.urls = urls;
					t.location = location;
					g.addTask(t);
				} else {
					Task ta = g.getTask(taskName);
					ta.duration = duration;
					ta.urls = urls;
					ta.location = location;
				}
				v.invalidate();
				finish();
			}
		});
		
		Button btnBrowse = (Button) findViewById(R.id.browse);
		btnBrowse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent act = new Intent(getBaseContext(),
						WebHelperActivity.class);
				Task ta = g.getTask(taskName);

				act.putExtra("URL", ta.urls);
				startActivity(act);
				v.invalidate();
				finish();
			}
		});
		
		Button btnMap = (Button) findViewById(R.id.map);
		btnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				 
				EditText elocation = (EditText) findViewById(R.id.location);
				String location = elocation.getText().toString();
				
				Intent act = new Intent(getBaseContext(),
						MapActivity.class);
				
				
				Task ta = g.getTask(taskName);
				
				System.out.println("*** AddActyActivity ta.location=" + ta.location);

				act.putExtra("LATLNG", ta.location);		// PWAY
				startActivity(act);
				
				v.invalidate();
				finish();
			}
		});
		
	}
}

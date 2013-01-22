package com.example.p1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActyActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Boolean isNewTask,isUpdateTask;
		final Globals g = Globals.getInstance();
		String taskName = g.currentTaskName;
		System.out.println("*** AddActyActivity taskName=" + taskName);

  
		setContentView(R.layout.activity_addacty);

		 isNewTask = "".equals(taskName);
		 isUpdateTask=!isNewTask;
		final Task t=null;
		if (isUpdateTask) {
			 Task ta;
			 ta=g.getTask(taskName);
			EditText text1 = (EditText) findViewById(R.id.taskName);
			text1.setText(taskName);
			EditText duration = (EditText) findViewById(R.id.duration);
			duration.setText(ta.duration);

		}
		;
		Button btnOK = (Button) findViewById(R.id.ok);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText etaskName = (EditText) findViewById(R.id.taskName);
				String taskName = etaskName.getText().toString();
				EditText eduration = (EditText) findViewById(R.id.duration);
				String duration = eduration.getText().toString();
				if (isNewTask) {
					Task t = new Task();
					t.name = taskName;
					t.desc = "Description of " + taskName;
					Globals g = Globals.getInstance();
					t.plan = g.currentPlanName;
					t.duration = duration;
					g.addTask(t);
				} else {
					Task ta=g.getTask(taskName);
					ta.duration = duration;
				}
				v.invalidate();
				finish();
			}
		});
	}
}

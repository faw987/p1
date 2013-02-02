package com.example.p1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActyActivity extends Activity {
	Boolean isNewTask=true, isUpdateTask=false;
	String taskName="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Globals g = Globals.getInstance();

		taskName = g.currentTaskName;

		System.out.println("*** AddActyActivity taskName=" + taskName);

		setContentView(R.layout.activity_addacty);

		isNewTask = "".equals(taskName);
		isUpdateTask = !isNewTask;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_acty_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// same as using a normal menu
		switch (item.getItemId()) {
		case R.id.ok:
			makeToast("OK");
			Globals g = Globals.getInstance();   // HACK
			addUpdateActy(isNewTask, g, item.getActionView());
			break;
		case R.id.browse:
			makeToast("browse");
			g = Globals.getInstance();   // HACK
			doBrowser(g, getWindow().getDecorView().getRootView());
			break;
		case R.id.map:
			makeToast("menu_settings2...");
			g = Globals.getInstance();   // HACK
			doMap(g, getWindow().getDecorView().getRootView());

			break;
		case R.id.foursquare:
			makeToast("\n\nComming soon ... \n\n" +
					"   the Foursquare connection.");
			finish();
			break;
		case R.id.yelp:
			makeToast("\n\nComming soon ... \n\n" +
					"   the Yelp connection.");
			finish();
			break;
		case R.id.cancel:
			makeToast("cancel");
			// hack 	v.invalidate();
			finish();
			break;
		}
		return true;
	}
	public void makeToast(String message) {	
		Toast toast = Toast.makeText( this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);
		toast.show();
	}

	private void addUpdateActy(  Boolean isNewTask,   Globals g, View v) {

		System.out.println("*** addUpdateActy isNewTask=" + isNewTask);

		EditText etaskName = (EditText) findViewById(R.id.taskName);
		String ui_taskName = etaskName.getText().toString();
		EditText eduration = (EditText) findViewById(R.id.duration);
		String duration = eduration.getText().toString();
		EditText eurls = (EditText) findViewById(R.id.urls);
		String urls = eurls.getText().toString();

		EditText elocation = (EditText) findViewById(R.id.location);
		String location = elocation.getText().toString();

		if (isNewTask) {
			Task t = new Task();
			t.name = ui_taskName;
			t.desc = "Description of " + taskName;
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
		// HACK	v.invalidate();
		finish();
	}

	private void doBrowser(final Globals g, View v) {
		Intent act = new Intent(getBaseContext(),
				WebHelperActivity.class);
		Task ta = g.getTask(taskName);

		act.putExtra("URL", ta.urls);
		startActivity(act);
		v.invalidate();
		finish();
	}

	private void doMap(final Globals g, View v) {
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
}

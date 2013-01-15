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

		setContentView(R.layout.activity_addacty);
		Button btnOK = (Button) findViewById(R.id.ok);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText text1 = (EditText) findViewById(R.id.editText1);
				String et1 = text1.getText().toString();
				Task t = new Task();
				t.name = et1;
				t.desc = "Description of " + et1;
				Globals g = Globals.getInstance();
				t.plan = g.currentPlanName;
				g.addTask(t);
				v.invalidate();
				finish();
			}
		});
	}
}

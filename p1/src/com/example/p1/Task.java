package com.example.p1;

import android.widget.EditText;

public class Task {
	public String name;
	public String plan;		// if associated this is corresponding plan
							// 
	public String desc;
	public int seq;
	public String duration;
	public String urls;
	public String location;

	
	//
	// adding a field
	//
	//    1. add to Task.java Task class
	//			e.g., public String urls;
	//	  2. add to sample JSON in res/raw/tasks.txt
	//			e.g., "urls":"",
	//	  3. update JSON input --  Utilities.readPlansTasks 
	//			e.g., t.urls = tasks.getJSONObject(i).getString("urls").toString();
	//	  4. update JSON output -- Utilities.tasksToJSON
	//			e.g., jo.put("urls", x1.urls);
	//update layout
//	   <LinearLayout
//       xmlns:android="http://schemas.android.com/apk/res/android"
//       android:layout_width="wrap_content"
//       android:layout_height="wrap_content"
//       android:orientation="horizontal" >
//
//       <TextView
//           android:id="@+id/textView1"
//           style="@style/App_EditTextStyle1"
//           android:layout_width="wrap_content"
//           android:layout_height="wrap_content"
//           android:text="URLs:" />
//
//       <EditText
//           android:id="@+id/urls"
//           android:layout_width="wrap_content"
//           android:layout_height="wrap_content"
//           android:ems="10" />
//   </LinearLayout>
	// update screen edit code
//	EditText eurls = (EditText) findViewById(R.id.urls);
//	String urls = eduration.getText().toString();
//	
//	AND
//	
//	EditText urls = (EditText) findViewById(R.id.urls);
// BUG BUG BUG	duration.setText(ta.urls);
//	urls.setText(ta.urls);
//
//
//	
//	AND
//	
//	t.urls = urls;
//AND
//ta.urls = urls;

	//
}

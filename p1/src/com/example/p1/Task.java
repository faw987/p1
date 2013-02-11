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
	//	5. update layout
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
	// 6. update screen edit code
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

	static public void generateFieldCode(){

		// x-y-<name> 
		//				where x in e=edit h=hiden o=outputOnly   
		//				      y in s=string i=int 


		final String flist="e-s-name,e-s-plan,e-s-desc,h-i-seq,e-s-duration,e-s-urls,e-s-location";
		final   String field_action;
		String field_type;
		String[] fields=flist.split(",");

		//   1. add to Task.java Task class

		for (String f : fields){
			//			e.g., public String urls;

			String  type=f.split("-")[1];
			String  n=f.split("-")[2];

			if ("s".equals(type)) System.out.println("public String " + n + ";");
			if ("i".equals(type)) System.out.println("public int " + n + ";");
		}

		//	 2. add to sample JSON in res/raw/tasks.txt public String urls;

		for (String f : fields){
			//			e.g., "urls":"",

			String  action=f.split("-")[0];
			String  n=f.split("-")[2];

			if ("e".equals(action)) System.out.println("\"" + n + "\": \" \"");

		}
		//		  3. update JSON input --  Utilities.readPlansTasks 

		for (String f : fields){
			//			e.g., t.urls = tasks.getJSONObject(i).getString("urls").toString();
			String  n=f.split("-")[2];

			System.out.println("t." + f + " = tasks.getJSONObject(i).getString(\"" + n + "\").toString();");

		}
		//	  4. update JSON output -- Utilities.tasksToJSON
		for (String f : fields){
			//			e.g., jo.put("urls", x1.urls);
			String  n=f.split("-")[2];

			System.out.println("jo.put(\"" + n + "\", x1." + n + ");");

		}

		// 		5. update layout
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
		for (String f : fields){
			String  n=f.split("-")[2];

			System.out.println(
					"<TextView\n"
							+ "     android:id=\"@+id/textView" + n + "\"\n"
							+ "     style=\"@style/App_EditTextStyle1\"\n"
							+ "     android:layout_width=\"wrap_content\"\n"
							+ "     android:layout_height=\"wrap_content\"\n"
							+ "     android:text=\"" + n +":\" />");

			System.out.println(
					"<EditText\n"
							+ "     android:id=\"@+id/" + n + "\"\n"
							+ "     style=\"@style/App_EditTextStyle1\"\n"
							+ "     android:layout_width=\"wrap_content\"\n"
							+ "     android:layout_height=\"wrap_content\"\n"
							+ "     android:ems=\"10\" />");
		}



		// 6. update screen edit code
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

		for (String f : fields){
			//			EditText eurls = (EditText) findViewById(R.id.urls);
			//	String urls = eduration.getText().toString();
			String  n=f.split("-")[2];

			String  action=f.split("-")[0];
			if ("e".equals(action)) {
				System.out.println("EditText e" + n + " = (EditText) findViewById(R.id." + n + ");");
				System.out.println("String " + n + " = e" + f + ".getText().toString();");
			};
		}


	}


}






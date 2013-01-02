package com.example.p1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class PlanActivity extends Activity {
	private final String TAG = "PlanActivity";
	private WebView browser = null;
	int idNumber = 0;
	TableLayout tl2;
	String et1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      Plan Act");
		setContentView(R.layout.activity_plan);
		
		tl2 = (TableLayout) findViewById(R.id.recTable);
		tl2.removeAllViews();
		tl2.setVerticalScrollBarEnabled(true);

		
		browser = (WebView) findViewById(R.id.calculator);
		// set a webview client to override the default functionality
		browser.setWebViewClient(new wvClient());

		// get settings so we can config our WebView instance
		WebSettings settings = browser.getSettings();

		// JavaScript?  Of course!
		settings.setJavaScriptEnabled(true);

		browser.setVerticalScrollBarEnabled(true);

		// clear cache
		browser.clearCache(true);

		// this is necessary for "alert()" to work
		browser.setWebChromeClient(new WebChromeClient());



		Button btnSearch = (Button) findViewById(R.id.search1);
		btnSearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnSearch clicked =====");
				// startVend();
				// http://www.google.com/search?as_q=nintendo
				//				// browser.loadUrl("http://www.clevelandart.org/visit/plan-your-visit/admission-and-hours");
				//browser.loadUrl("http://www.google.com");
				EditText text1 = (EditText) findViewById(R.id.searchInput);
				et1 = text1.getText().toString();
				String searchInput = "http://www.google.com/search?as_q=" + et1;
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
			
				
				Log.i(TAG, "===== ENTER btnSearch clicked ===== search for:" + searchInput);
				browser.loadUrl(searchInput);
			}
		});


		Button btnSelect = (Button) findViewById(R.id.select1);
		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnSearch clicked =====");
				// startVend();
				
					ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				String text1 = clipboard.getText().toString();	
				Log.i(TAG, "===== ENTER clipboard:" + text1);

			// move to table:	EditText stuff = (EditText) findViewById(R.id.stuffToDo);
			
				idNumber += 1;
				
				// move to table:		stuff.append("\n\n" + idNumber + " " + text1);
				
				Toast.makeText(getApplicationContext(),
						"Your selection is:" + text1 + ".", Toast.LENGTH_SHORT)
						.show();
				
				// ========
			
				
				//for (String rec : Globals.recs) {
					//String plainRec = extractPlainRec(rec);
					//System.out.println(" rec = " + rec);
					addRecRow(tl2, idNumber + " " + text1);
				//};
				// ========
				
			}
		});

		

		Button btnAdd = (Button) findViewById(R.id.add1);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "===== ENTER btnAdd clicked =====");
				// startVend();
				
					ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
					
					EditText text1 = (EditText) findViewById(R.id.searchInput);
					et1 = text1.getText().toString();
					
				Log.i(TAG, "===== ENTER input:" + et1);		// simply use et1 for now

			// move to table:	EditText stuff = (EditText) findViewById(R.id.stuffToDo);
			
				idNumber += 1;
				
				// move to table:		stuff.append("\n\n" + idNumber + " " + text1);
				
				Toast.makeText(getApplicationContext(),
						"Your selection is:" + et1 + ".", Toast.LENGTH_SHORT)
						.show();
				
				// ========
			
				
				//for (String rec : Globals.recs) {
					//String plainRec = extractPlainRec(rec);
					//System.out.println(" rec = " + rec);
					addRecRow(tl2, idNumber + " " + et1);
				//};
				// ========
				
			}
		});

		
		
	}

	final class wvClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) {
			// when our web page is loaded, let's call a function that is contained within the page
			// this is functionally equivalent to placing an onload attribute in the <body> tag
			// whenever the loadUrl method is used, we are essentially "injecting" code into the page when it is prefixed with "javascript:"
			// browser.loadUrl("javascript:startup()");
		}
	}

	public void addRecRow(TableLayout tl, String rowData) {
		TableRow tr2 = new TableRow(this);
	
		
	
		Button btnSelect = new Button(this);
		btnSelect.setText("Edit");
		btnSelect.setTextSize(10.0f);
		btnSelect.setTextColor(Color.BLACK);
	//1	Drawable sBackground = getResources().getDrawable(R.drawable.round_button_background);
	//1	btnSelect.setBackgroundDrawable(sBackground);
				
		btnSelect.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
//				
				View p = (View)v.getParent();
				TableRow tr = (TableRow)v.getParent();
				TextView c = (TextView)tr.getChildAt(2);
				
				String s= c.getText().toString();
				
				System.out.println("      v = " + v);
				System.out.println("      p = " + p);
				System.out.println("      tr = " + tr);	
				System.out.println("      c = " + c);	
				System.out.println("      s = " + s);		// so this IS what we select via DCAT 
				
				Toast.makeText(getApplicationContext(),
						"Thanks for the Edit button press. Data: " + s, Toast.LENGTH_SHORT)
						.show();
//				
//				com.faw.proto2.TMWActivity.dcatSelect(Globals.bid, "[" + s + "]");				// so call DCAT to select the rec as per the Buy button pressed
//					
//				String newQos = com.faw.proto2.TMWActivity.extractQosFromRec(s);
//				
//				System.out.println("      newQos = " + newQos);		// so this IS what we select via DCAT 
//	
//				com.faw.proto2.TMWActivity.setSubPlanOperation(Globals.subscriberId, newQos);		// FIX THIS UP to use rec name TO DO
//	
//		 		startTMWVideoPlayer();							// finlly let em wath the flick
//				
//				tl2.removeAllViews();
	
			}
		});
	
		
	//	LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);
	
//		btnSelect.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
	
		btnSelect.setLayoutParams(lp);

		/* Add Button to row. */
		tr2.addView(btnSelect);
	
		Button btnInfo = new Button(this);
		btnInfo.setText("Info");
		btnInfo.setTextSize(10.0f);
		btnInfo.setTextColor(Color.BLACK);
	//1	btnInfo.setBackgroundDrawable(sBackground);
		btnInfo.setLayoutParams(lp);


		btnInfo.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				
				// output.setText("");
				View p = (View)v.getParent();
				TableRow tr = (TableRow)v.getParent();
				TextView c = (TextView)tr.getChildAt(2);
				
				String s= c.getText().toString();
				
				System.out.println("      v = " + v);
				System.out.println("      p = " + p);
				System.out.println("      tr = " + tr);	
				System.out.println("      c = " + c);	
				System.out.println("      s = " + s);		// so this IS what we select via DCAT 
				
				Toast.makeText(getApplicationContext(),
						"Thanks for the Info button press. Data: " + s, Toast.LENGTH_SHORT)
						.show();
//				
//				String fileName = com.faw.proto2.TMWActivity.convertRecommendationToFileName(s);
//				
//				popupPresoNew("file:///sdcard/MyMovies/" + fileName + ".html"); // convertRecommendationToFileName
	
			}
		});
	
//		btnInfo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
		
		tr2.addView(btnInfo);
	
		tl.addView(tr2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
	
		tl.invalidate(); // This line does not work
		
		TextView t1 = new TextView(this);
//		t1.setTextSize(40.0f);
		t1.setTextSize(16.0f);
		t1.setText(rowData);
		t1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Add Button to row. */
		tr2.addView(t1);
	
	}



}

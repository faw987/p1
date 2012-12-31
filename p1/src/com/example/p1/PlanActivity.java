package com.example.p1;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlanActivity extends Activity {
	private final String TAG = "PlanActivity";
	private WebView browser = null;
	int idNumber = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      Plan Act");
		setContentView(R.layout.activity_plan);
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
				//				// browser.loadUrl("http://www.clevelandart.org/visit/plan-your-visit/admission-and-hours");
				browser.loadUrl("http://www.google.com");
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

				EditText stuff = (EditText) findViewById(R.id.stuffToDo);
			
				idNumber += 1;
				
				stuff.append("\n\n" + idNumber + " " + text1);
				
				Toast.makeText(getApplicationContext(),
						"Your selection is:" + text1 + ".", Toast.LENGTH_SHORT)
						.show();
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



}

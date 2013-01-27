package com.example.p1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.TableLayout;
import android.widget.Toast;

public class WebHelperActivity extends Activity {
	private WebView browser = null;
	private final String TAG = "PlanActivity";
	int idNumber = 0;
	TableLayout tl2;
	String et1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "===== ENTER onCreate =====");

		System.out.println("      Plan Act");
		setContentView(R.layout.web_helper);
		browser = (WebView) findViewById(R.id.calculator);
		// set a webview client to override the default functionality
		// ??? how to fix ?? browser.setWebViewClient(new wvClient());

		// get settings so we can config our WebView instance
		WebSettings settings = browser.getSettings();

		// JavaScript? Of course!
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
				// //
				// browser.loadUrl("http://www.clevelandart.org/visit/plan-your-visit/admission-and-hours");
				// browser.loadUrl("http://www.google.com");
				EditText text1 = (EditText) findViewById(R.id.searchInput);
				et1 = text1.getText().toString();
				String searchInput = "http://www.google.com/search?as_q=" + et1;

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				Log.i(TAG, "===== ENTER btnSearch clicked ===== search for:"
						+ searchInput);
				browser.loadUrl(searchInput);
			}});
		
		browser.setWebViewClient(new WebViewClient() {
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// String fragment = "#access_token=";
				// int start = url.indexOf(fragment);
				// if (start > -1) {
				// // You can use the accessToken for api calls now.
				// String accessToken = url.substring(start + fragment.length(),
				// url.length());
				//
				// Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
				//
				// Toast.makeText(getApplicationContext(), "Token: " +
				// accessToken, Toast.LENGTH_SHORT).show();
				// }
				//
				System.out.println(">>>>>>>>>>>>>>>> onPageStarted url=" + url);

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

				idNumber += 1;

				Toast.makeText(getApplicationContext(), "Your selection is:"
						+ text1 + ".", Toast.LENGTH_SHORT);

			}
		});

		final class wvClient extends WebViewClient {
			public void onPageFinished(WebView view, String url) {
				// when our web page is loaded, let's call a function that is
				// contained within the page
				// this is functionally equivalent to placing an onload
				// attribute in the <body> tag
				// whenever the loadUrl method is used, we are essentially
				// "injecting" code into the page when it is prefixed with
				// "javascript:"
				// browser.loadUrl("javascript:startup()");
			}
		}
		;

	};
}

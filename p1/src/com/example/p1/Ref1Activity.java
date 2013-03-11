package com.example.p1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

public class Ref1Activity extends Activity implements OnItemClickListener {
	private final String tag = "PresentForecastsActivity";
	private WebView browser = null;
	private int flipflop = 0;
	public static HashMap<String, String> acronyms = new HashMap<String, String>();
	
	int colorBg[] = new int[] {0xF5F5DC,0xFFEBCD,0xDEB887,0xD2691E};


	AutoCompleteTextView autoCompView;
	String aggregate;
	String s1;
	String s2;
	String s3;
	private static final int RECOGNIZER_EXAMPLE = 1001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ref1);

		final EditText formula = (EditText) this.findViewById(R.id.formula);
		final Button btnSimple = (Button) this.findViewById(R.id.btnSimple);
		final Button btnVoiceIn = (Button) this.findViewById(R.id.voiceIn);

		btnSimple.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "onClick Simple");
				autoCompView.setText("");
			}
		});

		btnVoiceIn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				autoCompView.setText(s2);

//				Toast.makeText(getApplicationContext(),
//						"Thanks for the button press.", Toast.LENGTH_SHORT)
//						.show();
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
						"Please speak...");
				startActivityForResult(intent, RECOGNIZER_EXAMPLE);
			}
		});

		autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
		PlacesAutoCompleteAdapter paca = new PlacesAutoCompleteAdapter(this,
				R.layout.list_item);

		autoCompView.setAdapter(paca);
		autoCompView.setOnItemClickListener(this);

		// connect to our browser so we can manipulate it
		browser = (WebView) findViewById(R.id.calculator);

		// set a webview client to override the default functionality
		browser.setWebViewClient(new wvClient());

		// get settings so we can config our WebView instance
		WebSettings settings = browser.getSettings();

		// JavaScript? Of course!
		settings.setJavaScriptEnabled(true);

		// clear cache
		browser.clearCache(true);

		// this is necessary for "alert()" to work
		browser.setWebChromeClient(new WebChromeClient());

		// add our custom functionality to the javascript environment
		browser.addJavascriptInterface(new CalculatorHandler(), "calc");

		// uncomment this if you want to use the webview as an invisible
		// calculator!
		// browser.setVisibility(View.INVISIBLE);

		String index = "<html>\n";
		index += "<script type=\"text/javascript\" src=\"./jquery-1.9.1.js\"></script>\n";
		index += "<script type=\"text/javascript\" src=\"./jquery.sparkline.js\"></script>\n";
		index += "<script type=\"text/javascript\" src=\"./jquery-ui.js\"></script>\n";
		index += "<script type=\"text/javascript\" src=\"scrollTo-min.js\"></script>\n";

		index += "<link rel=\"stylesheet\" type=\"text/css\" href=\"./jquery-ui.css\">\n";

		index += "<body>\n";
		index += "<script  language=\"JavaScript\">\n";
		index += "$(document).ready(function() { 	$('.sparklines').sparkline('html');    });\n";
		index += "function scrollToId(id){$.scrollTo(id);};\n";

		index += "</script>\n";

		// //////// index += processFile2(); // HACK HAK

		processFile2(); // HACK HAK no work

		processFile3(); // HACK HAK works

		index += generateHtml();

		index += "</body></html>\n";

		browser.loadDataWithBaseURL("file:///android_asset/", index,
				"text/html", "utf-8", null);
		// browser.loadUrl("file:///android_asset/ref1_lte_acro.html");

		// browser.loadData(index, "text/html", "utf-8");

	}

	final class wvClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) {
			// when our web page is loaded, let's call a function that is
			// contained within the page
			// this is functionally equivalent to placing an onload attribute in
			// the <body> tag
			// whenever the loadUrl method is used, we are essentially
			// "injecting" code into the page when it is prefixed with
			// "javascript:"
			browser.loadUrl("javascript:startup()");
		}
	}

	// Javascript handler
	final class CalculatorHandler {
		private int iterations = 0;

		// write to LogCat (Info)
		public void Info(String str) {
			iterations++;
			Log.i("Calc", str);
		}

		// write to LogCat (Error)
		public void Error(String str) {
			iterations++;
			Log.e("Calc", str);
		}

		// sample to retrieve a custom - written function with the details
		// provided by the Android native application code
		public String GetSomeFunction() {
			iterations++;
			return "var q = 6;function dynamicFunc(v) { return v + q; }";
		}

		// Kill the app
		public void EndApp() {
			iterations++;
			finish();
		}

		public void setAnswer(String a) {
			iterations++;
			Log.i(tag, "Answer [" + a + "]");
		}

		public int getIterations() {
			return iterations;
		}

		public void SendHistory(String s) {
			Log.i("Calc", "SendHistory" + s);
			try {
				JSONArray ja = new JSONArray(s);
				for (int i = 0; i < ja.length(); i++) {
					Log.i("Calc",
							"History entry #" + (i + 1) + " is ["
									+ ja.getString(i) + "]");
				}
			} catch (Exception ee) {
				Log.e("Calc", ee.getMessage());
			}
		}

	}

	public String processFile1() {
		try {
			// URL yahoo = new URL("file:///android_asset/ref1_lte_acro.html");
			AssetManager am = getApplicationContext().getAssets();
			InputStream ist = am.open("ref1_lte_acro.html");
			InputStreamReader is = new InputStreamReader(ist);
			BufferedReader in = new BufferedReader(is);

			StringBuffer result = new StringBuffer();

			String inputLine;
			int i = 0;
			while ((inputLine = in.readLine()) != null) {
				i++;
				System.out.println("i=" + i + " in=/" + inputLine + "/ len="
						+ inputLine.length());
				result.append(processNormal(inputLine));

				if (inputLine.indexOf("</p>") > -1) {
					inputLine = in.readLine();
					while (inputLine.trim().length() == 0
							&& (inputLine = in.readLine()) != null) {
						System.out.println("skip");
					}
					;
					System.out.println("SPECIAL:" + inputLine);
					result.append(processSpecial1(inputLine));
				}
				;
			}
			in.close();
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return "?";
	}

	public void processFile2() {
		try {
			AssetManager am = getApplicationContext().getAssets();
			InputStream ist = am.open("abbr_acro_etc.txt");
			InputStreamReader is = new InputStreamReader(ist);
			BufferedReader in = new BufferedReader(is);

			StringBuffer result = new StringBuffer();

			String inputLine;
			int i = 0;
			while ((inputLine = in.readLine()) != null) {
				i++;
				System.out.println("i=" + i + " in=/" + inputLine + "/ len="
						+ inputLine.length());

				processSpecial2(inputLine);

				//				System.out.println(" ----------- ADD acro " + s1 + "=" + s2);

				// acronyms.put(s1, s2 + "--" ); // HACK
				addAcronym(s1, s2); // HACK
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public void processFile3() {
		try {
			AssetManager am = getApplicationContext().getAssets();
			InputStream ist = am.open("ref1_lte_acro.html");
			InputStreamReader is = new InputStreamReader(ist);
			BufferedReader in = new BufferedReader(is);

			String inputLine;

			int i = 0;

			while ((inputLine = in.readLine()) != null
					&& inputLine.trim().length() != 0) {
				i++;
				System.out.println("i=" + i + " NONBLANK in=/" + inputLine
						+ "/ len=" + inputLine.length());
			}

			while (inputLine != null) {

				while ((inputLine = in.readLine()) != null
						&& inputLine.trim().length() == 0) {
					i++;
					System.out.println("i=" + i + " BLANK in=/" + inputLine
							+ "/ len=" + inputLine.length());
				}
				;

				processSpecial3(inputLine);

				while ((inputLine = in.readLine()) != null
						&& inputLine.trim().length() != 0) {
					i++;
					System.out.println("i=" + i + " AGGREGATE in=/" + inputLine
							+ "/ len=" + inputLine.length());
					addToAggregate(inputLine);
				}
				;
				// acronyms.put(s1, s2 + "--" + aggregate);
				addAcronym(s1, aggregate);

				// result.append(processSpecial3wrapup(inputLine).toString());
				// result.append(processSpecial3wrapup(inputLine).toString());
			}
			in.close();
			// return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	private String processSpecial1(String input) {
		Pattern p = Pattern
				.compile("^([A-Za-z0-9]*)[ \t]*([A-Za-z0-9 ,]*[.])(.*)$"); // lte
		Matcher m = p.matcher(input);

		StringBuffer result = new StringBuffer();
		while (m.find()) {
			// System.out.println("1: " + m.group(1));
			// System.out.println("2: " + m.group(2));
			// System.out.println("3: " + m.group(3));
			result.append("<b><p><a name=\"" + m.group(1) + "\">" + m.group(1)
					+ "</a> " + m.group(2) + "</b>" + processNormal(m.group(3)));
			acronyms.put(m.group(1), m.group(2)); // ?????????????????

			Log.i(tag,
					"=== adding acronym=/" + m.group(1) + "/ = /" + m.group(2)
					+ "/");

		}
		// System.out.println(" r:" + result);
		return result.toString();
	}

	private void processSpecial2(String input) {
		Pattern p = Pattern
				.compile("^([A-Za-z0-9-]*)[ \\t]*[-][ \\t]*([A-Za-z0-9 ,-]*)$"); // gfm
		Matcher m = p.matcher(input);
		System.out
		.println(" SPECIAL2 in=/" + input + "/ len=" + input.length());
		StringBuffer result = new StringBuffer();
		while (m.find()) {
			s1 = m.group(1);
			s2 = m.group(2);
			System.out.println(" processSpecial2 s1=/" + s1 + "/ s2=/" + s2
					+ "/");
		}
	}

	private void processSpecial3(String input) {
		Pattern p = Pattern.compile("^([A-Z]*)[ \t]*([A-Za-z0-9 ,]*[.])(.*)$"); // lte

		if (input == null)
			return;

		Matcher m = p.matcher(input);

		System.out
		.println(" SPECIAL3 in=/" + input + "/ len=" + input.length());

		StringBuffer result = new StringBuffer();
		while (m.find()) {
			s1 = m.group(1);
			s2 = m.group(2);
			s3 = m.group(3);
			aggregate = "";
			addToAggregate(s3);
		}
	}

	private void addToAggregate(String input) {
		System.out.println(" addToAggregate in=/" + input + "/ len="
				+ input.length());

		if (input.endsWith("-")) {
			aggregate += input.substring(0, input.length() - 1); // remove -
		} else {
			aggregate += input + " "; // HACK ?
		}
		;
	}

	// private String processSpecial3wrapup(String input) {
	// StringBuffer result = new StringBuffer();
	// // while (m.find()) {
	// result.append("<b><p><h3><a style=\"color:blue\" id=\"" + s1
	// + "\" name=\"" + s1 + "\">" + s1 + "</a></h3> " + s2 + "</b>"
	// + "<p>" + processAggregate(aggregate) + "</p>");
	// acronyms.put(s1, s2 + "--" + aggregate);
	// // }
	// System.out.println(" r:" + result);
	// return result.toString();
	// }
	// private String processSpecial2wrapup(String input) {
	// // Pattern p = Pattern
	// // .compile("^([A-Za-z0-9]*)[ \t]*([A-Za-z0-9 ,]*[.])(.*)$"); // lte
	// // Matcher m = p.matcher(input);
	//
	// StringBuffer result = new StringBuffer();
	// // while (m.find()) {
	// result.append("<b><p><h3><a style=\"color:blue\" id=\"" + s1
	// + "\" name=\"" + s1 + "\">" + s1 + "</a></h3> " + s2 + "</b>"
	// + "<p>" + processAggregate(aggregate) + "</p>");
	// acronyms.put(s1, s2 + "--" + aggregate);
	// // }
	// System.out.println(" r:" + result);
	// return result.toString();
	// }
	// private Boolean isSpecialLine(String input) {
	// Pattern p = Pattern
	// .compile("^([A-Za-z]*)[ \t]*([A-Za-z0-9 ,]*[.])(.*)$"); // lte
	// Matcher m = p.matcher(input);
	//
	// while (m.find()) {
	// return true;
	// }
	// // System.out.println(" r:" + result);
	// return false;
	// }

	private void addAcronym(String a, String input) {

		if (a==null) {
			System.out.println(" addAcronym a == null");
			return;
		}
		if (input==null){
			System.out.println(" addAcronym input == null");
			return;
		}

		if (acronyms.containsKey(a)) {
			String s = acronyms.get(a);
			String v = s + input + "--";
			acronyms.put(a, v); // HACK
			//			System.out.println(" ACRO a=/" + a + "/ updated value=/" + v + "/");

		} else {
			acronyms.put(a, input + "--"); // HACK
			//			System.out.println(" ACRO a=/" + a + "/     1ST value=/" + input
			//					+ "--" + "/");
		}

	}

	private String processOneAcronym(String acronym, String input) {
		Pattern p = Pattern.compile("^(.*)[ \\t]*(" + acronym
				+ ")[ \\t.]*(.*)$");
		Matcher m = p.matcher(input);

		StringBuffer result = new StringBuffer();
		while (m.find()) {
			Log.i(tag, "=== found  acronym=/" + m.group(1) + "/" + m.group(2)
					+ "/" + m.group(3) + "/");

			result.append(m.group(1) + "<A href=\"#" + m.group(2) + "\">"
					+ m.group(2) + "</a> " + m.group(3));

		}
		// System.out.println(" r:" + result);
		return result.toString();
	}

	private String processNormal(String input) {
		StringBuffer result = new StringBuffer();

		for (String s : acronyms.keySet()) {
			result.append(processOneAcronym(s, input));
		}
		return result.toString();
	}

	private String processAggregate(String input) {
		StringBuffer result = new StringBuffer();
		String words[] = input.split(" ");
		for (String w : words) {
			if (acronyms.containsKey(w)) {
				String l = "<A href=\"#" + w + "\">" + w + "</a> ";
				result.append(l);
			} else {
				// System.out.println("========== append: " + w);
				result.append(handleAllCases(w));
			}
		}
		return result.toString();
	}

	private String handleAllCases(String input) {
		Pattern p = Pattern.compile("^(\\W*)([A-Za-z0-9-]*)(\\W*)$"); //
		Matcher m = p.matcher(input);
		//		System.out.println(" handleAllCases in=/" + input + "/ len="
		//				+ input.length());
		StringBuffer result = new StringBuffer();
		while (m.find()) {
			s1 = m.group(1);
			s2 = m.group(2);
			s3 = m.group(3);
			if (input.equals(s2))
				return input + " ";
			//			System.out.println(" handleAllCases s1=/" + s1 + "/ s2=/" + s2
			//					+ "/ s3=/" + s3);
			if (acronyms.containsKey(s2)) {
				String l = "<A href=\"#" + s2 + "\">" + s2 + "</a> ";
				return s1 + l + s2;
			} else {
				return input + " ";
			}
		}
		return input + " ";
	}

	private String generateHtml() {
		System.out
		.println("=========================================================================");
		StringBuffer result = new StringBuffer();

		// SortedSet<String> keys = new TreeSet<String>(acronyms.keySet());
		SortedSet<String> keys = new TreeSet<String>();
		for (String s : acronyms.keySet()) {
			if (s!=null) {

				//				System.out.println(">>>>>>>>>>>   add to tree -- s=/" + s + "/");


				keys.add(s);}};

				System.out.println(">>>>>>>>>>>    acronyms.keySet().size()=/" + acronyms.keySet().size());
				System.out.println(">>>>>>>>>>>    acronyms.keySet().size()=/" + keys.size());




				// List sortedKeys=new ArrayList(acronyms.keySet());
				// Collections.sort(sortedKeys);

				// SortedSet<String> keys = new TreeSet<String>(acronyms.keySet());

				// String[] keys = acronyms.keySet();
				// System.out.println("          ---------------- keys lengh" +
				// keys.length);
				//
				// Arrays.sort(keys);
				// for(String key : keys) {
				// System.out.println(key + " : " + acronyms.get(key));
				// }

				// List<String> tmp = Collections.list( acronyms.keySet());
				// Collections.sort(tmp);
				// Iterator<String> it = tmp.iterator();
				// while(it.hasNext()){
				// String s =it.next();

				dumpAcronyms();

				// for (String s : acronyms.keySet()) {
				for (String s : keys) {
					String v = acronyms.get(s);
					String p[] = v.split("--");

					// int c=0;for (String s1 : p) {
					// System.out.println(" c=" + c++ + " s1=" +s1);
					// }

					System.out.println(">>>>>>>>>>> >>>>>>>>>>>>>>>>>>>>>>>>    -- generate html for ACRONYM = "
							+ s + " = /" + acronyms.get(s) + "/");

					result.append("<b><h3><a style=\"color:blue\" id=\"" + s
							+ "\" name=\"" + s + "\">" + s + "</a></h3> ");

					// Integer color=0xF0F8FF;
					Integer color = -1;
 
					for (String s1 : p) { // (int i=0;i<p.length;i++){
						color++;
						String s4 = "<p style=\"background-color:#"
								+ Integer.toHexString(colorBg[color % colorBg.length]) + "\">"
								+ processAggregate(s1) + "</p>";
						// String s3="<p style=\"background-color:#F0F8FF\">" + p[i] +
						// "</p>";
						//System.out.println(">>>>>>>>>>>    -- s4=/" + s4 + "/");
						result.append(s4);
						System.out.println(">>>>>>>>>>> >>>>>>>>>>>>>>>>>>>>>>>>    --     s4 html for ACRONYM = "
								+ s + " = /" + s4 + "/");
					}
				}

				System.out
				.println("=========================================================================");
				return result.toString();
	}

	// private String generateHtml0( ) {
	// System.out
	// .println("=========================================================================");
	// StringBuffer result = new StringBuffer();
	//
	// for (String s : acronyms.keySet()) {
	// String v = acronyms.get(s);
	// String p[] = v.split("--");
	// System.out.println(">>>>>>>>>>>>>>>>>>    -- acronym=" + s + " = /"
	// + acronyms.get(s) + "/");
	// if(p.length==2){
	// result.append("<b><p><h3><a style=\"color:blue\" id=\"" + s
	// + "\" name=\"" + s + "\">" + s + "</a></h3> " + p[0] + "</b>"
	// + "<p>" + processAggregate(p[1]) + "</p>");
	// }
	// else {
	//
	// result.append("<b><p><h3><a style=\"color:blue\" id=\"" + s
	// + "\" name=\"" + s + "\">" + s + "</a></h3> " + p[0] + "</b>");
	// }
	// }
	//
	// System.out
	// .println("=========================================================================");
	// return result.toString();
	// }
	static public void dumpAcronyms() {
		System.out
		.println("=========================================================================");
		// for (String s : acronyms.keySet()) {
		// System.out.println(">>>>>>>>>>>>>>>>>>    -- acronym=" + s + " = /"
		// + acronyms.get(s) + "/");
		// }
		System.out.println(">>>>>>>>    -- acronym size ="
				+ acronyms.keySet().size());
		System.out
		.println("=========================================================================");
	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);

		// autoCompView.setInputType(0);

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		browser.loadUrl("javascript:scrollToId('#" + str + "')");

	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
	implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = new ArrayList<String>();

		//		System.out.println("=====                    input=" + input);
		//		System.out.println("=====           input.length()=" + input.length());
		//		System.out.println("===== acronyms.keySet().size()="
		//				+ acronyms.keySet().size());

		for (String s : acronyms.keySet()) {

			//			System.out.println("===== s=" + s);

			int ml = Math.min(input.length(), s.length());
			String s1 = s.substring(0, ml);
			if (s1.toLowerCase().equals(input.toLowerCase())) {
				resultList.add(s);
			}
		}

		return resultList;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(getApplicationContext(), "onActivityResult",
				Toast.LENGTH_SHORT).show();
		if (requestCode == RECOGNIZER_EXAMPLE && resultCode == RESULT_OK) {
			ArrayList<String> result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			int len = result.size();
			String s1 = "result size = " + len + "\n";
			String s2 = result.get(0).toString();

			System.out
			.println("===== >>>>>>>>>>>>>>>>>>>>>>>>>> THIS IS WHAT WAS 'UNDERSOOD' "
					+ s2);

			autoCompView.setText(s2);
			browser.loadUrl("javascript:scrollToId('#" + s2 + "')");

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
package com.example.p1;

 
 
import org.json.JSONArray;

import android.app.Activity;
 
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class PresentForecastsActivity extends Activity {
	private final String tag = "PresentForecastsActivity";
	private WebView browser = null;
	private int flipflop = 0;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentforecast);
        
        
        final EditText formula = (EditText) this.findViewById(R.id.formula);
        final Button btnSimple = (Button) this.findViewById(R.id.btnSimple);
        final Button btnComplex = (Button) this.findViewById(R.id.btnComplex);
        final Button btnRed = (Button) this.findViewById(R.id.btnRed);
        
 //1       final StreamSource xmlSource = new StreamSource(this.getResources().openRawResource(R.raw.resourceoffering1));
//1	     final StreamSource xsltSource = new StreamSource(this.getResources().openRawResource(R.raw.xslt_test1));

		btnSimple.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "onClick Simple");
				// Perform action on click
				try {
					String formulaText = formula.getText().toString();
					Log.i(tag, "Formula is [" + formulaText + "]");

					browser.loadUrl("javascript:PerformSimpleCalculation("
							+ formulaText + ");");

				} catch (Exception e) {
					Log.e(tag, "Error ..." + e.getMessage());
				}
			}
		});
        
        btnComplex.setOnClickListener(new OnClickListener()
        {
        	 
        	 public void onClick(View v) {
        		 Log.i(tag,"onClick Complex");
        		 // Perform action on click
        		 try
        		 {
        			 String jsonText = "";
        		 
        			 if (flipflop == 0)
        			 {	 
        				 jsonText = "{ \"operation\" : \"addarray\",\"arguments\" : [1,2,3,4,5,6,7,8,9,10]}";
        				 flipflop = 1;
        			 } else {
        				 jsonText = "{ \"operation\" : \"multarray\",\"arguments\" : [1,2,3,4,5,6,7,8,9,10]}";
        				 flipflop = 0;
        			 }
        			 Log.i(tag,"jsonText is [" + jsonText + "]" );
        			 browser.loadUrl("javascript:PerformComplexCalculation(" + jsonText + ");");
        		 }
        		 catch (Exception e)
        		 {
        			 Log.e(tag,"Error ..." + e.getMessage());
        		 }
        		 
             }
        });
        
        btnRed.setOnClickListener(new OnClickListener()
        {
        	 public void onClick(View v) {
        		 Log.i(tag,"onClick Red");
        		 // Perform action on click
        		 try
        		 {
        			 //
        		  	//  BORROW THIS --
        			 //    TEMP >>  browser.loadUrl("javascript:(function() { document.getElementsByTagName('body')[0].style.color = 'red';})()");
        			 //
        			 
        	//1		 String r = Snippet.callWD("wdx.po()","doEval");
      			//1	r = Snippet.unescape(r);     		

      			//1	System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>    == r:" + r);

      	//1			ByteArrayInputStream rs = new ByteArrayInputStream(r.getBytes());
  				
      		   //1     StreamSource xmlSource = new StreamSource(rs);     
      				
        		     //1    	        String summary = DoXslt.performTransformation (xsltSource, xmlSource);

         			 
        	         //1			browser.loadData(summary, "text/html", "utf-8");
        			 
        			 
        		 }
        		 catch (Exception e)
        		 {
        			 Log.e(tag,"Error ..." + e.getMessage());
        		 }
        		 
             }
        });
        
        
        
        // connect to our browser so we can manipulate it
        browser = (WebView) findViewById(R.id.calculator);

        // set a webview client to override the default functionality
        browser.setWebViewClient(new wvClient());
        
        // get settings so we can config our WebView instance
        WebSettings settings = browser.getSettings();
        
        // JavaScript?  Of course!
        settings.setJavaScriptEnabled(true);
        
        // clear cache
        browser.clearCache(true);
        
        // this is necessary for "alert()" to work
        browser.setWebChromeClient(new WebChromeClient());
        
        // add our custom functionality to the javascript environment
        browser.addJavascriptInterface(new CalculatorHandler(), "calc");
        
        // uncomment this if you want to use the webview as an invisible calculator!
        //browser.setVisibility(View.INVISIBLE);
        
        // load a page to get things started
 //       browser.loadUrl("file:///android_asset/index.html");
        
        String index = "<html>\n";
        index += "<script type=\"text/javascript\" src=\"./jquery-1.9.1.js\"></script>\n";          
        index += "<script type=\"text/javascript\" src=\"./jquery.sparkline.js\"></script>\n";          
        index += "<script type=\"text/javascript\" src=\"./jquery-ui.js\"></script>\n";          
        index += "<link rel=\"stylesheet\" type=\"text/css\" href=\"./jquery-ui.css\">\n";          

        index += "<body>\n";
        index += "<script  language=\"JavaScript\">\n";
        index += "$(document).ready(function() { 	$('.sparklines').sparkline('html');    });\n";
        index += "</script>\n";
       
        // index += "<span  class=\"sparklines\">1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1</span>\n";          
       
        index += WeatherInfo.dumpForecastsToHtml(false);
        index += WeatherInfo.dumpForecastsToHtml(true);

        index += "</body></html>\n";
     
        
		System.out.println(tag + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + index);

        
        browser.loadDataWithBaseURL( "file:///android_asset/", index, "text/html", "utf-8", null );
        
// browser.loadData(index, "text/html", "utf-8");
        
        
        
        
        
        // allows the control to receive focus
        // on some versions of Android the webview doesn't handle input focus properly
        // this seems to make things work with Android 2.1, but not 2.2
       // browser.requestFocusFromTouch();

    }
    
    final class wvClient extends WebViewClient {
    	 public void onPageFinished(WebView view, String url) {
    		 // when our web page is loaded, let's call a function that is contained within the page
    		 // this is functionally equivalent to placing an onload attribute in the <body> tag
    		 // whenever the loadUrl method is used, we are essentially "injecting" code into the page when it is prefixed with "javascript:"
    		 browser.loadUrl("javascript:startup()");
    	 }
    }
    
    // Javascript handler
    final class CalculatorHandler
	{
    	private int iterations = 0;
    	
		// write to LogCat (Info)
		public void Info(String str) {
			iterations++;
			Log.i("Calc",str);
		}
		
		// write to LogCat (Error)		
		public void Error(String str) {
			iterations++;
			Log.e("Calc",str);
		}
			
		// sample to retrieve a custom - written function with the details provided by the Android native application code
		public String GetSomeFunction()
		{
			iterations++;
			return "var q = 6;function dynamicFunc(v) { return v + q; }";
		}
		
		// Kill the app		
		public void EndApp() {
			iterations++;
			finish();
		}
		
		public void setAnswer(String a)
		{
			iterations++;
			Log.i(tag,"Answer [" + a + "]");
		}
		
		
		public int getIterations()
		{
			return iterations;
		}
		
		public void SendHistory(String s)
		{
			Log.i("Calc","SendHistory" + s);
			try {
				JSONArray ja = new JSONArray(s);
				for (int i=0;i<ja.length();i++) {
					Log.i("Calc","History entry #" + (i+1) + " is [" + ja.getString(i) + "]");
				}
			} catch (Exception ee) {
				Log.e("Calc",ee.getMessage());
			}
		}

	}
}
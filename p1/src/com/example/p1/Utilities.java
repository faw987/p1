package com.example.p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utilities {

	static public String donotdisturb(String input) {
		return input;
	}

	public static String jsonToStringFromAssetFolder(int id, Context context)
			throws IOException {
		// AssetManager manager = context.getAssets();
		// InputStream file = manager.open(fileName);
		InputStream file = context.getResources().openRawResource(id);

		byte[] data = new byte[file.available()];
		file.read(data);
		file.close();
		return new String(data);
	}

	static public JSONObject plansToJSON() {
		Globals g = Globals.getInstance();
		int sz = g.plansSize();
		System.out.println("PlanActivity -- plans sz=" + sz);

		g.sortPlans();

		ArrayList<Plan> planz = g.getPlansArray();

		JSONObject jsonObj = null;
		JSONArray list = new JSONArray();
		for (Plan x : planz) {
			try {
				JSONObject jo = new JSONObject();

				jo.put("name", x.name);
				jo.put("desc", x.desc);

				list.put(jo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		jsonObj = new JSONObject();
		try {
			jsonObj.put("plans", list);

			// System.out.println("jsonObj: " + jsonObj.toString(5));

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return jsonObj;
	};

	static public JSONObject tasksToJSON() {

		Globals g = Globals.getInstance();
		int sz = g.tasksSize();

		System.out.println("tasksToJSON -- tasks sz=" + sz);

		g.sortTasks();

		ArrayList<Task> taskz = g.getTasksArray();

		JSONObject jsonObj = null;
		JSONArray list = new JSONArray();
		for (Task x : taskz) {

			System.out.println("tasksToJSON -- get task next -  x.name="
					+ x.name);

			Task x1 = g.getTask(x.name); // HACK HACK HACK

			try {
				JSONObject jo = new JSONObject();

				jo.put("name", x1.name);
				jo.put("desc", x1.desc);
				jo.put("plan", x1.plan);
				
				System.out.println("\n\nMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM   x1.plan=" +  x1.plan + "\n\n");

				jo.put("duration", x1.duration);
				jo.put("urls", x1.urls);
				jo.put("location", x1.location);

				list.put(jo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		jsonObj = new JSONObject();
		try {
			jsonObj.put("tasks", list);

			// System.out.println("tasks jsonObj: " + jsonObj.toString(5));

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return jsonObj;
	};

	static public String getArrayPlanStrings(Context ac, String input) {

		String result = "";

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(ac);

		String plansStr = sharedPrefs.getString("plans1", "{}");
		String tasksStr = sharedPrefs.getString("tasks1", "{}");

		JSONObject jsonObj = null;

		try {
			jsonObj = new JSONObject(plansStr);

			JSONArray plans = jsonObj.getJSONArray("plans");
			for (int i = 0; i < plans.length(); i++) {
				// printing the values to the logcat
				// System.out.println("Utilities: i:" + i);

				String name = plans.getJSONObject(i).getString("name")
						.toString();

				// System.out.println("Utilities: name:" + name);
				// System.out.println("Utilities: desc:"
				// + plans.getJSONObject(i).getString("desc").toString());
				result = result + name + ",";

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	static public void readPlansTasks(Context c) {
		String sampleplans = null, sampletasks = null;

		Globals g = Globals.getInstance();

		try {

			if (true) {
				sampleplans = Utilities.jsonToStringFromAssetFolder(
						R.raw.plans, c);
				sampletasks = Utilities.jsonToStringFromAssetFolder(
						R.raw.tasks, c);
			} else {
				sampleplans = Utilities.jsonToStringFromAssetFolder(
						R.raw.plansmedium, c);
				sampletasks = Utilities.jsonToStringFromAssetFolder(
						R.raw.tasksmedium, c);
			}
			;

			if (Globals.debugVerbose()) {
				System.out.println("PlanActivity -- plans sampleplans="
						+ sampleplans);
				System.out.println("PlanActivity -- tasks sampletasks="
						+ sampletasks);
			}

		} catch (Exception e) {
			System.out.println("PlanActivity -- plans e=" + e);
		}
		;

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		String plansStr = sharedPrefs.getString("plans1", sampleplans);
		String tasksStr = sharedPrefs.getString("tasks1", sampletasks);

		JSONObject jsonObj = null;

		g.plansClear();
		g.tasksClear();

		// plans follow

		try {
			jsonObj = new JSONObject(plansStr);

			JSONArray plans = jsonObj.getJSONArray("plans");
			for (int i = 0; i < plans.length(); i++) {
				Plan p = new Plan();
				p.name = plans.getJSONObject(i).getString("name").toString();
				p.desc = plans.getJSONObject(i).getString("desc").toString();
				p.arrayListOfTasks = new ArrayList<Task>();
				g.addPlan(p);

				PlanActy pa = new PlanActy(p.name);
				pa.printout();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// tasks follow

		try {
			jsonObj = new JSONObject(tasksStr);

			JSONArray tasks = jsonObj.getJSONArray("tasks");
			for (int i = 0; i < tasks.length(); i++) {

				Task t = new Task();
				t.name = tasks.getJSONObject(i).getString("name").toString();
				t.desc = tasks.getJSONObject(i).getString("desc").toString();
				t.plan = tasks.getJSONObject(i).getString("plan").toString();
				t.duration = tasks.getJSONObject(i).getString("duration")
						.toString();
				t.urls = tasks.getJSONObject(i).getString("urls").toString();
				t.location = tasks.getJSONObject(i).getString("location")
						.toString();
				g.addTask(t);

				BasicActy ba = new BasicActy(t.name);
				ba.printout();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void writePlansTasks(Context c) {

		String plansjson = Utilities.plansToJSON().toString();
		String tasksjson = Utilities.tasksToJSON().toString();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		prefs.edit().putString("plans1", plansjson).commit();
		prefs.edit().putString("tasks1", tasksjson).commit();

	}

	static public String callHttp(String url) {

		System.out.println(" == getParam:" + url);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		try {

			HttpResponse response;
			response = httpclient.execute(httpget);

			System.out.println(" == httpget:" + httpget);

			BufferedReader in = null;

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();

			if (Globals.debugPrintHttpResults())
				System.out.println(" == result:" + result);

			return result;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	// go thru ALL tasks and filter those for planName

	static private void populateTasks(String planName, ArrayList<Task> tasks) {

		System.out.println("populateTasks for: " + planName);

		Globals g = Globals.getInstance();

		ArrayList<Task> taskz = g.getTasksArray();

		for (Task x : taskz) {
			if (planName.equals(x.plan)) {
				System.out.println("task IS  for: " + planName + " task name:"
						+ x.name);
				tasks.add(x);
			} else {
				System.out.println("task NOT for: " + planName + " task name:"
						+ x.name);
			}
			;
		}
		;
	}

	static public void createByTaskArray() {

		Globals g = Globals.getInstance();

		int sz = g.plansSize();
		System.out.println("createByTaskArray -- plans sz=" + sz);

		ArrayList<Plan> planz = g.getPlansArray();

		for (Plan x : planz) {
			ArrayList<Task> tasks = new ArrayList<Task>();
			ArrayList<Task> a = (ArrayList<Task>) x.arrayListOfTasks;

			if (a.isEmpty()) {
				populateTasks(x.name, tasks);
				// x.arrayListOfTasks = (Object) tasks;
				x.arrayListOfTasks = tasks;
				System.out.println("createByTaskArray -- create list for plan");

			} else {
				tasks = (ArrayList<Task>) x.arrayListOfTasks;
				int szt = tasks.size();
				System.out
				.println("createByTaskArray -- have   list for plan, size:"
						+ szt);
			}
			;
		}
		;
		for (Plan x : planz) {
			ArrayList<Task> tasks = new ArrayList<Task>();
			System.out
			.println("createByTaskArray -- recap for plan: " + x.name);
			ArrayList<Task> a = (ArrayList<Task>) x.arrayListOfTasks;

			if (a.isEmpty()) {

				System.out
				.println("createByTaskArray -- create list for plan = null");

			} else {
				tasks = (ArrayList<Task>) x.arrayListOfTasks;
				int szt = tasks.size();
				System.out.println("createByTaskArray -- have list, size:"
						+ szt);
			}
			;
		}
		;

	}

	static public void printFile(File f) {
		// File sourceFile = new File (f);
		String f_name = f.getName();
		String f_path = f.getPath();

		System.out.printf("name= %s  path=%s \n", f_name, f_path);

		FileReader fr = null;
		try {
			fr = new FileReader(f);
			int inChar;

			while ((inChar = fr.read()) != -1) {
				System.out.printf("%c", inChar);
			}
		} catch (IOException e) {
			System.err.printf("Failure while reading %s: %s\n", f,
					e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				System.err.printf("Error closing file reader: %s\n",
						e.getMessage());
				e.printStackTrace();
			}
		}
	}

	static public JSONObject callDirections(String from, String to) {
		String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
				+ from + "&destination=" + to + "&sensor=false";

		String result = Utilities.callHttp(url);

		JSONObject r = null;
		try {
			r = new JSONObject(result);
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		return r;

	}

	static public JSONObject callWeather(String state, String zip) {

		String url="http://api.wunderground.com/api/9b834783bd345d99/conditions/q/" + state + "/" + zip + ".json";

		String result = Utilities.callHttp(url);

		JSONObject r = null;
		try {
			r = new JSONObject(result);
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		return r;

	}

	static public JSONObject callWeather( String zip) {

		String url="http://api.wunderground.com/api/9b834783bd345d99/conditions/q/"  + zip + ".json";

	//	http://api.wunderground.com/api/9b834783bd345d99/hourly/q/08854.json
		
		String result = Utilities.callHttp(url);

		JSONObject r = null;
		try {
			r = new JSONObject(result);
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		return r;

	}

	static public JSONObject callWeatherHourly( String zip) {

		String url="http://api.wunderground.com/api/9b834783bd345d99/hourly/q/"  + zip + ".json";
		
		String result = Utilities.callHttp(url);

		JSONObject r = null;
		try {
			r = new JSONObject(result);
		} catch (Exception e) {
			System.err.printf("Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}
		return r;

	}


	static public String callCityData(String city, String state) {

		String url="http://www.city-data.com/city/" + city + "-" + state + ".html";
		String result = Utilities.callHttp(url);
		return result;

	}



	static public int directionsGetDistance(JSONObject directions) {

		JSONObject jsonObj = null;

		try {
			JSONArray routes = directions.getJSONArray("routes");
			for (int i = 0; i < routes.length(); i++) {
				JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");
				for (int j = 0; j < legs.length(); j++) {
					String d = legs.getJSONObject(j).getJSONObject("distance")
							.getString("value").toString();
					int meter = Integer.parseInt(d);
					return meter; 					// HACK happens to work but not general
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0; 									// HACK s/b unreachable

	}

	static public int directionsGetDuration(JSONObject directions) {

		// TOTAL HACK HACK just a cut and paste and edit

		JSONObject jsonObj = null;

		try {

			JSONArray routes = directions.getJSONArray("routes");
			for (int i = 0; i < routes.length(); i++) {
				JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");
				for (int j = 0; j < legs.length(); j++) {
					String l = legs.getJSONObject(j).getJSONObject("duration")
							.getString("value").toString();
					int duration = Integer.parseInt(l);

					return duration; 	// HACK for some reason 1st route 1st leg
					// works BUT IS NOT geneal
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0; 						// HACK
	}



	static public double weatherGetTempF(JSONObject weather) {

		// TOTAL HACK HACK just a cut and paste and edit

		try {

			JSONObject co = weather.getJSONObject("current_observation");
			String t= co.getString("temp_f").toString();
			double t_double = Double.parseDouble(t);
			return t_double;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0; // HACK
	}
	
	
	// IS THIS STILL USED ??????????????????????????????????????????????????????????????????????
	
	static public String hourlyWeatherGetFeelsLike(JSONObject weatherHourly) {

			String r="";
		// TOTAL HACK HACK just a cut and paste and edit 
//		"year": "2013","mon": "2","mon_padded": "02","mon_abbrev": "Feb","mday": "17","mday_padded": "17","yday": "47"

		try {

			JSONArray hourlyf = weatherHourly.getJSONArray("hourly_forecast");
 			for (int i = 0; i < hourlyf.length(); i++) {
 				String t=  hourlyf.getJSONObject(i).getJSONObject("feelslike").getString("english").toString();
 				String pop=  hourlyf.getJSONObject(i).getString("pop").toString();

 				String h=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("hour").toString();
 				String mon_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mon_padded").toString();
 				String mday_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mday_padded").toString();
 				System.out.println("hourlyWeatherGetFeelsLike --  h=" + h + "  t=" + t + " mon_padded=" + mon_padded + " mday_padded=" + mday_padded);
 				String reading = mon_padded + "/" + mday_padded + "@" + h + "=" + t + "," + pop + "\n" ;
 				r += reading;
 			}
			return r ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}
	
	static public String cityDataGetCrime(String crimeHtml) {

		Document doc = Jsoup.parse(crimeHtml);
		Element body = doc.body();

		Elements ct = body.getElementsByClass("crime");
		System.out.println("ct: " + ct.text());

		//	Element table = body.select("table[class=crime]").first();
		//	Element table = body.select("table[class=\"crime\"]").first();

		Element table = body.select("tr[class=norm2]").first();
		
		if (table  == null) return "9999";				// hack 

		System.out.println("table: " + table);

		//			 Iterator<Element> ite = table.select("td[class=norm2]").iterator();
		//  Elements tableRows = table.select("tr");
		//	Elements tableRows = table.getElementsByClass("tr[class=norm2]");
		//	Elements tableRows = table.getElementsByClass("td");

		String hack_stat="";

		Elements tableRows = table.select("td");
		
		if (tableRows == null) return "9999";				// hack 
		
		for (Element tableRow : tableRows){
			if (tableRow.hasText()){
				String rowData = tableRow.text();
				System.out.println("rowData: " + rowData);
				hack_stat = rowData;						// HACK upon hack - most recent murder number
				//             			if(rowData.contains(testString)){
				//	               		tableRowStrings.add(rowData);
			}
		}

		return hack_stat;
	}




	static public String cityDataCityFromLocation(String location) {
 		if ( "08854".equals(location)) return "Society-Hill";
 		if ( "10012".equals(location)) return "New-York";
 		if ( "44256".equals(location)) return "Medina";
 		if ( "44119".equals(location)) return "Cleveland";
 		return "Camden";
	}


	static public String cityDataStateFromLocation(String location) {
 		if ( "08854".equals(location)) return "New-Jersey";
 		if ( "10012".equals(location)) return "New-York";
 		if ( "44256".equals(location)) return "Ohio";
 		if ( "44119".equals(location)) return "Ohio";
 		return "New-Jersey";
	}

}

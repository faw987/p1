package com.example.p1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Spinner;

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

	// static public void getSamplePlans(Context myContext) throws IOException {
	// AssetManager mngr = myContext.getResources().getAssets();
	// InputStream is = mngr.open("/res/raw/plans.txt");
	// InputStreamReader isr = new InputStreamReader(is);
	// BufferedReader br = new BufferedReader(isr);
	// String read = br.readLine();
	//
	// while (read != null) {
	// System.out.println(read);
	// // sb.append(read);
	// read = br.readLine();
	//
	// }

	// }

	static public JSONObject plansToJSON() {
		Globals g = Globals.getInstance();
		int sz = g.plansSize();
		System.out.println("PlanActivity -- plans sz=" + sz);

		// Utilities.addHardCodedPlan("plana");
		// Utilities.addHardCodedPlan("planb");

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
				// JSONArray plans = jsonObj.getJSONArray("plans"); }
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		// try {
		// System.out.println("list: " + list.toString(5));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ;
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

		System.out.println("PlanActivity -- tasks sz=" + sz);

		g.sortTasks();

		ArrayList<Task> taskz = g.getTasksArray();
		JSONObject jsonObj = null;
		JSONArray list = new JSONArray();
		for (Task x : taskz) {

			try {
				JSONObject jo = new JSONObject();

				jo.put("name", x.name);
				jo.put("desc", x.desc);
				jo.put("plan", x.plan);
				list.put(jo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		// try {
		// System.out.println("tasks list: " + list.toString(5));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// ;
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

	static public void addHardCodedPlan(String s) {
		Plan p = new Plan();
		p.name = s;
		p.desc = "Description of " + s;
		Globals g = Globals.getInstance();
		g.addPlan(p);

	}

	// obj.put("messages", list);

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

			sampleplans = Utilities.jsonToStringFromAssetFolder(R.raw.plans, c);
			// sampleplans = Utilities.jsonToStringFromAssetFolder(
			// R.raw.plansmedium, c);
			// System.out.println("PlanActivity -- plans sampleplans="
			// + sampleplans);
			sampletasks = Utilities.jsonToStringFromAssetFolder(R.raw.tasks, c);
			// sampletasks = Utilities.jsonToStringFromAssetFolder(
			// R.raw.tasksmedium, c);
			// System.out.println("PlanActivity -- tasks sampletasks="
			// + sampletasks);

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
				// printing the values to the logcat
				// System.out.println("ShowSettingsActivity: i:" + i);
				//
				// System.out.println("ShowSettingsActivity: name:"
				// + plans.getJSONObject(i).getString("name").toString());
				// System.out.println("ShowSettingsActivity: name:"
				// + plans.getJSONObject(i).getString("desc").toString());

				// static ArrayList<Task> tasks = new ArrayList<Task>();

				Plan p = new Plan();
				p.name = plans.getJSONObject(i).getString("name").toString();
				p.desc = plans.getJSONObject(i).getString("desc").toString();
				p.arrayListOfTasks = new ArrayList<Task>();
				g.addPlan(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// tasks follow

		try {
			jsonObj = new JSONObject(tasksStr);

			// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tasksStr: "
			// + tasksStr);

			JSONArray tasks = jsonObj.getJSONArray("tasks");
			for (int i = 0; i < tasks.length(); i++) {
				// printing the values to the logcat
				// System.out.println("ShowSettingsActivity: i:" + i);
				//
				// System.out.println("ShowSettingsActivity: name:"
				// + tasks.getJSONObject(i).getString("name").toString());
				// System.out.println("ShowSettingsActivity: name:"
				// + tasks.getJSONObject(i).getString("desc").toString());

				Task t = new Task();
				t.name = tasks.getJSONObject(i).getString("name").toString();
				t.desc = tasks.getJSONObject(i).getString("desc").toString();
				t.plan = tasks.getJSONObject(i).getString("plan").toString();
				g.addTask(t);

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

		HttpClient httpclient = new DefaultHttpClient();

		System.out.println(" == postParam:" + url);

		HttpGet httpget = new HttpGet(url);

		try {

			HttpResponse response;

			response = httpclient.execute(httpget);

			System.out.println(" == httppost:" + httpget);
			System.out.println(" == response:" + response);

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

			System.out.println(" == result:" + result);

			return result;

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}
	static private void populateTasks(String planName, ArrayList<Task> tasks) {
		//	for (Task x : (ArrayList<Task>) arrayListOfTasks) {
		System.out.println("populateTasks for: " + planName);
		Globals g = Globals.getInstance();

		ArrayList<Task> taskz = g.getTasksArray();

		for (Task x : taskz) {
			if (planName.equals(x.plan)) {
				System.out.println("found task for: " + planName + " task name:" + x.name);
				tasks.add(x);
			} else {
				System.out.println("task NOT for: " + planName);

			}
			;
		}
		;
		//}
	}
	static public void createByTaskArray() {
		
		Globals g = Globals.getInstance();
		List<String> list = new ArrayList<String>();

		int sz = g.plansSize();
		System.out.println("PlanActivity -- plans sz=" + sz);

		ArrayList<Plan> planz = g.getPlansArray();

		for (Plan x : planz) {
			ArrayList<Task> tasks = new ArrayList<Task>();
			ArrayList<Task> a = (ArrayList<Task>)x.arrayListOfTasks;

			if (a.isEmpty()) {
				populateTasks(x.name, tasks);
			//	x.arrayListOfTasks = (Object) tasks;
				x.arrayListOfTasks =   tasks;
				System.out.println("PlanActivity -- create list for plan");

			} else {
				tasks = (ArrayList<Task>) x.arrayListOfTasks;
				int szt=tasks.size();
				System.out.println("PlanActivity -- have list, size:" + szt);
			}
			;
		};
		for (Plan x : planz) {
			ArrayList<Task> tasks = new ArrayList<Task>();
			System.out.println("PlanActivity -- recap for plan: " + x.name);
			ArrayList<Task> a = (ArrayList<Task>)x.arrayListOfTasks;

			if (a.isEmpty()) {
				
				System.out.println("PlanActivity -- create list for plan = null");

			} else {
				tasks = (ArrayList<Task>) x.arrayListOfTasks;
				int szt=tasks.size();
				System.out.println("PlanActivity -- have list, size:" + szt);
			}
			;
		};

	}
}

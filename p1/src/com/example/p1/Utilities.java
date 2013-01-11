package com.example.p1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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

	static public void getSamplePlans(Context myContext) throws IOException {
		AssetManager mngr = myContext.getResources().getAssets();
		InputStream is = mngr.open("/res/raw/plans.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String read = br.readLine();

		while (read != null) {
			System.out.println(read);
			// sb.append(read);
			read = br.readLine();

		}

	}

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
				System.out.println("ShowSettingsActivity: i:" + i);

				String name = plans.getJSONObject(i).getString("name")
						.toString();

				System.out.println("ShowSettingsActivity: name:" + name);
				System.out.println("ShowSettingsActivity: name:"
						+ plans.getJSONObject(i).getString("desc").toString());
				result = result + name + ",";

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	static public void readPlansTasks(Context c) {
		String sampleplans = null, sampletasks = null;
		try {
			sampleplans = Utilities.jsonToStringFromAssetFolder(R.raw.plans, c);
			System.out.println("PlanActivity -- plans sampleplans="
					+ sampleplans);
			sampletasks = Utilities.jsonToStringFromAssetFolder(R.raw.tasks, c);
			System.out.println("PlanActivity -- tasks sampletasks="
					+ sampletasks);

		} catch (Exception e) {
			System.out.println("PlanActivity -- plans e=" + e);
		}
		;

		JSONObject jsonObj = null;

		// plans follow

		try {
			jsonObj = new JSONObject(sampleplans);

			JSONArray plans = jsonObj.getJSONArray("plans");
			for (int i = 0; i < plans.length(); i++) {
				// printing the values to the logcat
				System.out.println("ShowSettingsActivity: i:" + i);

				System.out.println("ShowSettingsActivity: name:"
						+ plans.getJSONObject(i).getString("name").toString());
				System.out.println("ShowSettingsActivity: name:"
						+ plans.getJSONObject(i).getString("desc").toString());

				Plan p = new Plan();
				p.name = plans.getJSONObject(i).getString("name").toString();
				p.desc = plans.getJSONObject(i).getString("desc").toString();
				Globals g = Globals.getInstance();
				g.addPlan(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// tasks follow

		try {
			jsonObj = new JSONObject(sampletasks);

			JSONArray tasks = jsonObj.getJSONArray("tasks");
			for (int i = 0; i < tasks.length(); i++) {
				// printing the values to the logcat
				System.out.println("ShowSettingsActivity: i:" + i);

				System.out.println("ShowSettingsActivity: name:"
						+ tasks.getJSONObject(i).getString("name").toString());
				System.out.println("ShowSettingsActivity: name:"
						+ tasks.getJSONObject(i).getString("desc").toString());

				Task t = new Task();
				t.name = tasks.getJSONObject(i).getString("name").toString();
				t.desc = tasks.getJSONObject(i).getString("desc").toString();
				Globals g = Globals.getInstance();
				g.addTask(t);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public String callHttp(String url ) {

		HttpClient httpclient = new DefaultHttpClient();


		System.out.println(" == postParam:" + url);

//		HttpPost httppost = new HttpPost(postParam);
		HttpGet httppost = new HttpGet(url);

	//	httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try {

//			String inputString = "<getIn><input1>" + input
//					+ "</input1></getIn>";
//
//			StringEntity myEntity = new StringEntity(inputString, "UTF-8");
//
//			System.out.println("===============================");
//			System.out.println(myEntity.getContentType());
//			System.out.println(myEntity.getContentLength());
//			System.out.println("inputString:" + inputString);
//			System.out.println("myEntity:" + myEntity);
//
//			httppost.setEntity(myEntity);

			HttpResponse response;

			response = httpclient.execute(httppost);

			System.out.println(" == httppost:" + httppost);
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

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return null;

	}

}

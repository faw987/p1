package com.example.p1;

import org.json.JSONArray;
import org.json.JSONObject;



public class WeatherInfo {
	static public String addHourlyFeelsLike(String location, JSONObject weatherHourly) {
		String r="";

		// TOTAL HACK HACK just a cut and paste and edit 
		//		"year": "2013","mon": "2","mon_padded": "02","mon_abbrev": "Feb","mday": "17","mday_padded": "17","yday": "47"
		LocationForcastInfo lfi = Globals.locationForcasts.get(location);
		if (lfi == null) {
			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- put");

	// ??????		(Globals.locationForcasts).put("A", new LocationForcastInfo());
		} else {

			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi=" + lfi);

		}
	//	int index=lfi.cs;	//  current  slot
		try {

			JSONArray hourlyf = weatherHourly.getJSONArray("hourly_forecast");
			for (int i = 0; i < hourlyf.length(); i++) {
				String t=  hourlyf.getJSONObject(i).getJSONObject("feelslike").getString("english").toString();
				String h=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("hour").toString();
				String mon_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mon_padded").toString();
				String mday_padded=  hourlyf.getJSONObject(i).getJSONObject("FCTTIME").getString("mday_padded").toString();
				String mmdd=  mon_padded + mday_padded;
				System.out.println("hourlyWeatherGetFeelsLike --  h=" + h + "  t=" + t + " mon_padded=" + mon_padded + " mday_padded=" + mday_padded);
 				String reading = mon_padded + "/" + mday_padded + "@" + h + "=" + t + "  " ;
 				r += reading;}

			return r ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;

	}

	public void addHourForecast(String mmdd, String f){};

	public void addHourlyForecast(String loc, String forecast) {

		LocationForcastInfo lfi = Globals.locationForcasts.get(loc);
		if (lfi == null) {
			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- put");

			(Globals.locationForcasts).put("A", new LocationForcastInfo());
		} else {

			System.out.println( ">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi=" + lfi);

		}
		;
		//
		//int pi = plans.size() - 1;
		// planMap.put(p.name, plans.get(pi));

	};

	public class HourlyForcastInfo {
		String dateForcastFor;     //  padded    mmdd 
		String[] hourForcastFor = new String[24]; // 24 of these
	}

	public class LocationForcastInfo {
		int cs = 0;
		HourlyForcastInfo[] hourlyForcastInfo = new HourlyForcastInfo[14];
	}

}

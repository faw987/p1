package com.example.p1;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


public class WeatherInfo {
	public class HourlyForcastInfo {
		public String dateForcastFor = ""; // padded mmdd
		public String[] hourForcastFor = new String[24]; // 24 of these
	}

	private class LocationForcastInfo {
		int cs = 0;
		public String location;
		public ArrayList<HourlyForcastInfo> hourlyForcastInfo = new ArrayList<HourlyForcastInfo>(
				14);
	}

	public static HashMap<String, LocationForcastInfo> locationForcasts = new HashMap<String, LocationForcastInfo>();

	static public int findDateSlot(ArrayList<HourlyForcastInfo> hfi, String mmdd) {

		System.out.println(">>>>>>>>>>>>>>>>>> findDateSlot  -- hfi.size="
				+ hfi.size() + " mmdd=" + mmdd );

		for (int i = 0; i < hfi.size(); i++) {

			if (hfi.get(i) != null) {
				String s = hfi.get(i).dateForcastFor;

				if (s.equals(mmdd)){

					return i;
				}
			}
		}
		;
		WeatherInfo w = new WeatherInfo();

		HourlyForcastInfo z = w.new HourlyForcastInfo();
		z.dateForcastFor = mmdd;
		hfi.add(z);
		int i = hfi.size() - 1; // hack

		System.out.println(">>>>>>>>>>>>>>>>>> findDateSlot  -- return i=" + i );
		return i;
	}

	static public void addHourlyForecast(LocationForcastInfo lfi, String mmdd,
			String hh, String t) {
		System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- mmdd="
				+ mmdd + " hh=" + hh + " t=" + t);
		System.out
		.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi.location="
				+ lfi.location);
		WeatherInfo w = new WeatherInfo();

		/*HourlyForcastInfo x = w.new HourlyForcastInfo();
		x.dateForcastFor = mmdd;
		lfi.hourlyForcastInfo.add(x);*/
		int slot = findDateSlot(lfi.hourlyForcastInfo, mmdd);
		System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- slot="
				+ slot);
		HourlyForcastInfo  h  = lfi.hourlyForcastInfo.get(slot);
		int hhi = Integer.parseInt(hh);
		h.hourForcastFor[hhi] += " " + t;
		System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- forecasts=" + h.hourForcastFor[hhi]);


	}
	static public void dumpForecasts(){
		System.out.println("=========================================================================");
		System.out.println("=========================================================================");

		for ( String s : locationForcasts.keySet()){
			System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- a="  + s );
			LocationForcastInfo o = locationForcasts.get(s);
			for (HourlyForcastInfo h : o.hourlyForcastInfo ) {
				System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- dateForcastFor="  + h.dateForcastFor);
				int hour=0;

				for ( String t : h.hourForcastFor) {
					System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- hour=" + hour + " t="  + t );
					hour++;

				}
			}
		}
		System.out.println("=========================================================================");
		System.out.println("=========================================================================");

	}
	static public String dumpForecastsToString(){
		String r="";
		for ( String s : locationForcasts.keySet()){
			System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- a="  + s );
			r += "LOCATION: " + s + "\n";
			LocationForcastInfo o = locationForcasts.get(s);
			for (HourlyForcastInfo h : o.hourlyForcastInfo ) {
				System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- dateForcastFor="  + h.dateForcastFor);
				r += "DATE: " + h.dateForcastFor + "\n";
				int hour=0;

				for ( String t : h.hourForcastFor) {
					System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- t="  + t);
					r += "HOUR: " + hour + "forecast=" + t + "\n";
					hour++;


				}
			}
		}
		return r;
	}        
	static public String dumpForecastsToHtml(boolean outputHourDetails){
		String r="";


		for ( String s : locationForcasts.keySet()){
			System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- a="  + s );
			r += "<h2>LOCATION: " + s + "</h2>\n";
			LocationForcastInfo o = locationForcasts.get(s);
			for (HourlyForcastInfo h : o.hourlyForcastInfo ) {
				System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- dateForcastFor="  + h.dateForcastFor);
				r += "<h2>DATE: " + h.dateForcastFor + "</h2>\n";
				int hour=0;
				String dateSummary="";
				for ( String t : h.hourForcastFor) {
					System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- t="  + t);
					if (t==null || t.equals("null") || t.length()==0){
						System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- t= = NULL NULL NULL");
					} else {
						if (outputHourDetails)r += "<h3>HOUR: " + hour + "forecast=" + t ;
						String[] forecast = t.split(" ");
						String s2="";
						for (int fi=0;fi<forecast.length;fi++){
							String[] metrics = forecast[fi].split(",");

							System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- metrics[0]="  + metrics[0] );

							if (metrics[0]==null || metrics[0].equals("null")){
								System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- SKIP null" );
								s2 += "0,";
							} else {
								s2 += metrics[0] + ",";}
						};
						String s3=s2.substring(0,s2.length()-1);
						if (outputHourDetails) r += "<span class=\"sparklines\">" + s3 + "</span></h3>\n";
						// 					r += "<span  class=\"sparklines\">1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1</span>\n";          
						String lastForecast=forecast[forecast.length-1];
						String[] metrics = lastForecast.split(",");
						dateSummary += metrics[0] +",";

					}
					hour++;
				}

				r += "<pre id=\"lfont\"> end of data for date: " + h.dateForcastFor + "</pre>\n";
				String dateSummaryShort=dateSummary.substring(0,dateSummary.length()-1);

				System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- dateSummary="  + dateSummaryShort );
				r += "<span class=\"sparklines\">" + dateSummaryShort + "</span>";

			}
		}
		System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- r="  + r );

		return r;
	}






	static public String addHourlyFeelsLike(String location,
			JSONObject weatherHourly) {
		String r = "";


		LocationForcastInfo lfi = locationForcasts.get(location);
		System.out.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- lfi="
				+ lfi);

		if (lfi == null) {
			System.out
			.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- put lfi="
					+ lfi);
			WeatherInfo w = new WeatherInfo();
			LocationForcastInfo x = w.new LocationForcastInfo();
			x.location = location;
			// x.hourlyForcastInfo = new HourlyForcastInfo[14];
			// x.hourlyForcastInfo.get(0).dateForcastFor=null;
			// x.hourlyForcastInfo[0].dateForcastFor="";
			locationForcasts.put(location, x);
			lfi = locationForcasts.get(location);
			System.out
			.println(">>>>>>>>>>>>>>>>>> addHourlyForecast  -- post put new lfi="
					+ lfi);

		}

		try {

			JSONArray hourlyf = weatherHourly.getJSONArray("hourly_forecast");
			for (int i = 0; i < hourlyf.length(); i++) {
				String t = hourlyf.getJSONObject(i).getJSONObject("feelslike")
						.getString("english").toString();
				String h = hourlyf.getJSONObject(i).getJSONObject("FCTTIME")
						.getString("hour").toString();
				String pop=  hourlyf.getJSONObject(i).getString("pop").toString();

				String mon_padded = hourlyf.getJSONObject(i)
						.getJSONObject("FCTTIME").getString("mon_padded")
						.toString();
				String mday_padded = hourlyf.getJSONObject(i)
						.getJSONObject("FCTTIME").getString("mday_padded")
						.toString();
				String mmdd = mon_padded + mday_padded;
				System.out.println("hourlyWeatherGetFeelsLike --  h=" + h
						+ "  t=" + t + " mon_padded=" + mon_padded
						+ " mday_padded=" + mday_padded);
				String reading = mon_padded + "/" + mday_padded + "@" + h + "="
						+ t + "," + pop + "  ";
				r += reading;

				addHourlyForecast(lfi, mmdd, h, t + "," + pop);
			}

			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;

	}



};

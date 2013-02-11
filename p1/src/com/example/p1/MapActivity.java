package com.example.p1;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	private final String TAG = "MapActivity";

	private GoogleMap map;

	static final LatLng PWAY = new LatLng(40.5840, -74.522);
	static final LatLng SOHO = new LatLng(40.72467, -73.99422);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LatLng latlng = setLatLon();

		setContentView(R.layout.activity_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));

		map.setMyLocationEnabled(true);

		if (map != null) {
			// Marker hamburg = map.addMarker(new MarkerOptions().position(PWAY)
			Marker hamburg = map.addMarker(new MarkerOptions().position(latlng)
					.title("Hamburg"));
			Marker kiel = map.addMarker(new MarkerOptions()
					.position(SOHO)
					.title("SOHO")
					.snippet("SOHO is cool")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
		}

		Intent intent = new Intent();
		intent.putExtra("ComingFrom", "Hello");
		setResult(RESULT_OK, intent);
		// finish();

	}

	private LatLng setLatLon() {
		LatLng latlng = null;
		float lat = 0;
		float lng = 0;
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return null;
		String slatlng = extras.getString("LATLNG");
		System.out.println(TAG + " -- slatlng=" + slatlng);

		if (slatlng == null)
			return null;
		if (slatlng.matches("[0123456789][0123456789][0123456789][0123456789][0123456789]")) {
			//String zip = Integer.parseInt(slatlng);
		//	System.out.println(TAG + " -- zip=" + zip);
 			latlng = new LatLng(1,1 );
 			latlng = geoCodeIt(latlng, slatlng);
		}
		else if (slatlng.matches("[0123456789+-. ]*")) {
			
		

			// Boolean looksLikeLatLon=true;
			// for (int curs=slatlng.length()-1;curs>=0;curs--){
			// if ( "+-0123467890. ".contains(slatlng.charAt(curs))) {} else
			// {looksLikeLatLon=false;break;}
			// }
			String[] splits = slatlng.split(" ");
			lat = Float.parseFloat(splits[0]);
			lng = Float.parseFloat(splits[1]);
			System.out.println(TAG + " -- lat=" + lat);
			System.out.println(TAG + " -- lng=" + lng);
			latlng = new LatLng(new Double(lat), new Double(lng));
		} else {
			latlng = geoCodeIt(latlng, slatlng);
		}
		;

		System.out.println(TAG + " -- latlng=" + latlng);


		return latlng;
	}

	private LatLng geoCodeIt(LatLng latlng, String slatlng) {
		float lat;
		float lng;
		try {
			Geocoder gc = new Geocoder(this, Locale.US); // create new
															// geocoder
															// instance
			List<Address> foundAdresses = gc.getFromLocationName(
					slatlng, 3); // Search
																	// addresses
			for (Address a : foundAdresses) {
				System.out.println(">>>>>>>>>>>>>>>>  a=" + a);
				System.out
						.println(">>>>>>>>>>>>>>>>  a=" + a.getLatitude());
				System.out.println(">>>>>>>>>>>>>>>>  a="
						+ a.getLongitude());
				lat =  new Float(a.getLatitude()) ;
				lng = new Float(a.getLongitude());
				System.out.println(TAG + " -- lat=" + lat);
				System.out.println(TAG + " -- lng=" + lng);
				latlng = new LatLng(new Double(lat), new Double(lng));
				String adminArea = a.getAdminArea();
				String postalCode = a.getPostalCode();
				System.out.println(">>>>>>>>>>>>>>>>  adminArea=" + adminArea);
				System.out.println(">>>>>>>>>>>>>>>>  postalCode=" + postalCode);

			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latlng;
	}
}
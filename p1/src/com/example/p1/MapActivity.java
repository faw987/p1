package com.example.p1;

import android.app.Activity;
import android.content.Intent;
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



		LatLng latlng = new LatLng(40.72467, -73.99422);		// SOHO
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String slatlng = extras.getString("LATLNG");
			System.out.println(TAG + " -- slatlng=" + slatlng);
			if (slatlng!=null){
				String[] splits = slatlng.split(" ");
				float lat = Float.parseFloat(splits[0]);
				float lng = Float.parseFloat(splits[1]);
				System.out.println(TAG + " -- lat=" + lat);
				System.out.println(TAG + " -- lng=" + lng);

				latlng=new LatLng(new Double(lat),new Double(lng));}
		}

		setContentView(R.layout.activity_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));

		map.setMyLocationEnabled(true);


		if (map != null) {
			//Marker hamburg = map.addMarker(new MarkerOptions().position(PWAY)
			Marker hamburg = map.addMarker(new MarkerOptions().position(latlng)
					.title("Hamburg"));
			Marker kiel = map.addMarker(new MarkerOptions()
			.position(SOHO)
			.title("SOHO")
			.snippet("SOHO is cool")
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher)));
		}

		Intent intent=new Intent();
		intent.putExtra("ComingFrom", "Hello");
		setResult(RESULT_OK, intent);
		//		    finish();

	}
}
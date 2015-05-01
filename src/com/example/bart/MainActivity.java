package com.example.bart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

/*
 * this activity is the starting point of the application. it has 3 clickable layouts
 * 1) station - to select which station you're at and get all the arrival times of all trains at that station
 * 2) trip - enter your origin and destination and get all upcoming trips between the two stations
 * 2) routes - get all stations on a particular route number
 */
public class MainActivity extends Activity {
	/*
	 * clickable layouts
	 */
	LinearLayout station, trip, routes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		station = (LinearLayout) findViewById(R.id.station);
		trip = (LinearLayout) findViewById(R.id.trip);
		routes = (LinearLayout) findViewById(R.id.routes);
		
		station.setOnClickListener(StationClick);
		trip.setOnClickListener(TripClick);
		routes.setOnClickListener(RoutesClick);

	}

	// TODO clickable layouts

	OnClickListener StationClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent go = new Intent(MainActivity.this, Station.class);
			MainActivity.this.startActivity(go);
		}
	};

	OnClickListener TripClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent go = new Intent(MainActivity.this, Trip.class);
			MainActivity.this.startActivity(go);

		}
	};
	
	OnClickListener RoutesClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent go = new Intent(MainActivity.this, Routes.class);
			MainActivity.this.startActivity(go);

		}
	};
}

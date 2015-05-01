package com.example.bart;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * this activity makes api calls to get all the stations connected by the bart service.
 * selecting one option  and clicking the 'get timings' button 
 * will allow you to look up arrival times of various trains on different routes at that station
 */
public class Station extends Activity {

	Spinner stationsList;
	ArrayList<String> res;
	Button getTimings;
	String selectedStation;
	boolean connected = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station);

		stationsList = (Spinner) findViewById(R.id.spinner1);

		getStations(); // get list of all stations

		// populate spinner with list of all stations
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, res);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stationsList.setAdapter(spinnerArrayAdapter);
		stationsList.setOnItemSelectedListener(stationSelect);

		// button to get timings at station
		getTimings = (Button) findViewById(R.id.get_time);
		getTimings.setOnClickListener(getTimeClick);

	}

	// creates a new thread to make calls to api
	public void getStations() {
		res = new ArrayList<String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				Document doc = APICallHelper
						.makeAPICall("http://api.bart.gov/api/stn.aspx?cmd=stns");

				if (doc != null) {
					NodeList n = doc.getElementsByTagName("name");
					NodeList n1 = doc.getElementsByTagName("abbr");
					for (int i = 0; i < n.getLength(); i++) {
						res.add(n.item(i).getTextContent() + "("
								+ n1.item(i).getTextContent() + ")");
					}

					connected = true;
				} else {
					connected = false;
				}
			}
		});
		t.start();
		try {
			Toast.makeText(getApplicationContext(),
					"Getting stations. Please wait.", Toast.LENGTH_LONG)
					.show();
			t.join();
		} catch (InterruptedException e) {
			Log.d("BartApp", "Api call interrupted");
		}

		if (!connected) {
			Toast.makeText(getApplication(), "PLEASE CONNECT TO NETWORK",
					Toast.LENGTH_LONG).show();
			res.add(" ");
		} else {
			Toast.makeText(getApplicationContext(),
					"List of stations available", Toast.LENGTH_SHORT).show();
		}
	}

	OnItemSelectedListener stationSelect = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			selectedStation = parent.getItemAtPosition(arg2).toString();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	OnClickListener getTimeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (selectedStation != " ") {
				Intent go = new Intent(Station.this, Schedules.class);
				String abbr1 = selectedStation.split("\\(")[1];
				String abbr = abbr1.split("\\)")[0];
				go.putExtra("selected", selectedStation.split("\\(")[0]);
				go.putExtra("abbr", abbr);

				Station.this.startActivity(go);
			} else {
				Toast.makeText(getApplicationContext(),
						"Select staion or wait to connect to network",
						Toast.LENGTH_LONG).show();
			}

		}
	};

}

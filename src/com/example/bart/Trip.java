package com.example.bart;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
	/*
	 * this activity allows you to enter an origin and a destination
	 * from a drop down menu of all stations
	 */
public class Trip extends Activity {

	Spinner fromStn, toStn;
	ArrayList<String> stations;
	Button getTime;
	String from, to;
	boolean connected = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_trip);

		fromStn = (Spinner) findViewById(R.id.spinner1);
		toStn = (Spinner) findViewById(R.id.spinner2);

		getStations();

		getTime = (Button) findViewById(R.id.get_time);
		getTime.setOnClickListener(getTimeClick);
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, stations);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		fromStn.setAdapter(spinnerArrayAdapter);
		toStn.setAdapter(spinnerArrayAdapter);
		fromStn.setOnItemSelectedListener(fromStationSelect);
		toStn.setOnItemSelectedListener(toStationSelect);

	}

	// creates a new thread to make calls to api
	public void getStations() {
		stations = new ArrayList<String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				Document doc = APICallHelper.makeAPICall("http://api.bart.gov/api/stn.aspx?cmd=stns");
				if (doc != null) {
					NodeList n = doc.getElementsByTagName("name");
					NodeList n1 = doc.getElementsByTagName("abbr");
					for (int i = 0; i < n.getLength(); i++) {
						stations.add(n.item(i).getTextContent() + "("
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
		if(connected){
			Toast.makeText(getApplicationContext(),
					"List of stations available.", Toast.LENGTH_SHORT).show();
		}else{
			stations.add(" ");
			Toast.makeText(getApplicationContext(), "PLEAST CONNECT TO NETWORK",Toast.LENGTH_SHORT).show();
		}
	}
	
	OnClickListener getTimeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(from != to && from!=" " && to!=" "){
				Intent go = new Intent(Trip.this, YourTrip.class);
				String fromAbbr = from.split("\\(")[1].split("\\)")[0];
				String toAbbr = to.split("\\(")[1].split("\\)")[0];

				go.putExtra("from", from.split("\\(")[0]);
				go.putExtra("fabbr", fromAbbr);
				go.putExtra("to", to.split("\\(")[0]);
				go.putExtra("tabbr", toAbbr);
				Log.d("BartApp", "in trip");
				Trip.this.startActivity(go);
			}else{
				Toast.makeText(getApplicationContext(), "a)Origin and destination cannot be the same\nb)Connect to network",Toast.LENGTH_LONG).show();
			}
			
		}
	};
	
	OnItemSelectedListener fromStationSelect = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			from = parent.getItemAtPosition(arg2).toString();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	OnItemSelectedListener toStationSelect = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			to = parent.getItemAtPosition(arg2).toString();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};

}

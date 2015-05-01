package com.example.bart;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
	/*
	 * this activity displays upto 4 upcoming trip departure and arrival times from origin
	 * to destination as entered by user. 
	 */
public class YourTrip extends Activity {

	String from, fromAbbr, to, toAbbr;
	TextView station;
	ListView listTrips;
	ArrayList<String> trips;
	boolean connected= true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_yourtrip);
		
		Intent i = getIntent();
		from = i.getStringExtra("from");
		fromAbbr = i.getStringExtra("fabbr");
		to = i.getStringExtra("to");
		toAbbr = i.getStringExtra("tabbr");
		
		station = (TextView) findViewById(R.id.station);
		station.setText("Your trip from " + from + " to " + to);
		getTrips();
		
		listTrips = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, trips);
		listTrips.setAdapter(listArrayAdapter);

	}

	// creates a new thread to make calls to api
	public void getTrips() {
		trips = new ArrayList<String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				Document doc = APICallHelper.makeAPICall("http://api.bart.gov/api/sched.aspx?cmd=arrive&orig="
						+ fromAbbr + "&dest=" + toAbbr);
				if (doc != null) {
					NodeList n = doc.getElementsByTagName("trip");
					for (int i = 0; i < n.getLength(); i++) {
						String trip = "";
						NodeList child = n.item(i).getChildNodes();
						for (int j = 0; j < child.getLength(); j++) {
							String a = child.item(j).getAttributes()
									.getNamedItem("origin").getTextContent();
							String b = child.item(j).getAttributes()
									.getNamedItem("origTimeMin").getTextContent();
							String c = child.item(j).getAttributes()
									.getNamedItem("destination").getTextContent();
							String d = child.item(j).getAttributes()
									.getNamedItem("destTimeMin").getTextContent();
							String e = child.item(j).getAttributes()
									.getNamedItem("line").getTextContent();
							trip += "Departs from " + a + " at " + b + " - Arrives at " + c + " at " + d
									+" on "+e+ "\n";
						}
						trips.add(trip);
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
					"Getting trip details. Please wait.", Toast.LENGTH_LONG)
					.show();
			t.join();
		} catch (InterruptedException e) {
			Log.d("BartApp", "Api call interrupted");
		} 
		if(connected) {
			Toast.makeText(getApplicationContext(),
					"List of routes and trips available.", Toast.LENGTH_SHORT).show();
		}else{
			trips.add(" ");
			Toast.makeText(getApplicationContext(),
					"PLEASE CONNECT TO NETWORK", Toast.LENGTH_SHORT).show();
		}
	}
}

package com.example.bart;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Entity.NamedContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * this activity displays all the train arrival times through different routes
 * at a particular station selected in the 'station' activity
 * all times and routes are displayed in a ListView
 */
public class Schedules extends Activity {

	String stnAbbr;
	String stnSelected;
	TextView stnName;
	ListView timingsList;
	ArrayList<String> time;// , route;
	boolean connected = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_schedules);
		Intent i = getIntent();
		stnAbbr = i.getStringExtra("abbr");
		stnSelected = i.getStringExtra("selected").toUpperCase();
		stnName = (TextView) findViewById(R.id.station);
		stnName.setText(stnSelected + " - ARRIVAL TIMES");

		getTimeAndRoute();

		// populate list with timings and routes from that station
		timingsList = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , time);
		timingsList.setAdapter(listArrayAdapter);

	}

	public void getTimeAndRoute() {
		time = new ArrayList<String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				Document doc = APICallHelper
						.makeAPICall("http://api.bart.gov/api/sched.aspx?cmd=stnsched&orig="
								+ stnAbbr);
				if (doc != null) {
					NodeList n = doc.getElementsByTagName("item");
					for (int i = 0; i < n.getLength(); i++) {
						time.add(n.item(i).getAttributes()
								.getNamedItem("origTime").getNodeValue()
								+ " - "
								+ n.item(i).getAttributes()
										.getNamedItem("line").getNodeValue());
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
					"Getting timings. Please wait.", Toast.LENGTH_LONG).show();
			t.join();
		} catch (InterruptedException e) {
			Log.d("BartApp", "Api call interrupted");
		}

		if (connected) {
			Toast.makeText(getApplicationContext(),
					"List of routes and times available", Toast.LENGTH_SHORT).show();
		} else {
			time.add(" ");
			Toast.makeText(getApplicationContext(),
					"PLEAST CONNECT TO NETWORK", Toast.LENGTH_SHORT).show();
		}
	}

}

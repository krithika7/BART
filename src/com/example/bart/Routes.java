package com.example.bart;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * This activity displays all routes and all the stations in each route in order
 */
public class Routes extends Activity {

	Spinner listRoutes;
	ListView listStations;
	ArrayList<String> routes, stations;
	String routeSelected = "1";
	HashMap<String, String> stationNames;
	ArrayAdapter<String> stationAdapter;
	boolean connected = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_routes);

		getRoutesStations();
		getStations(routeSelected);

		listRoutes = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, routes);
		routeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listRoutes.setAdapter(routeAdapter);
		listRoutes.setOnItemSelectedListener(routeSelect);

		listStations = (ListView) findViewById(R.id.listStations);
		stationAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, stations);
		listStations.setAdapter(stationAdapter);

	}

	void getRoutesStations() {
		routes = new ArrayList<String>();
		stationNames = new HashMap<String, String>();
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Get route names
				try {
					Document doc = APICallHelper
							.makeAPICall("http://api.bart.gov/api/route.aspx?cmd=routes&key=MW9S-E7SL-26DU-VV8V");
					if (doc != null) {
						NodeList n = doc.getElementsByTagName("route");
						for (int i = 0; i < n.getLength(); i++) {
							routes.add(n.item(i).getChildNodes().item(2)
									.getTextContent()
									+ " - "
									+ n.item(i).getChildNodes().item(0)
											.getTextContent());
						}
						connected = true;
					} else {
						connected = false;
					}
					
					//TODO get station names
					Document doc1 = APICallHelper
							.makeAPICall("http://api.bart.gov/api/stn.aspx?cmd=stns");

					if (doc1 != null) {
						NodeList n = doc1.getElementsByTagName("name");
						NodeList n1 = doc1.getElementsByTagName("abbr");
						for (int i = 0; i < n.getLength(); i++) {
							stationNames.put(n1.item(i).getTextContent(), n
									.item(i).getTextContent());
						}
						connected = true;
					} else {
						connected = false;
					}

				} catch (Exception e) {
					connected = false;
					Log.d("BartApp", e.toString());
				}

			}
		});
		t.start();

		
		try {
			t.join();
		} catch (InterruptedException e) {
			Log.d("BartApp", "Api call interrupted");
		}
		

		if (!connected) {
			Toast.makeText(getApplicationContext(),
					"PLEASE CONNECT TO NETWORK", Toast.LENGTH_SHORT).show();
		}

	}

	void getStations(final String rte) {
		stations = new ArrayList<String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Document doc = APICallHelper
							.makeAPICall("http://api.bart.gov/api/route.aspx?cmd=routeinfo&route="
									+ rte + "&key=MW9S-E7SL-26DU-VV8V");

					if (doc != null) {
						NodeList n = doc.getElementsByTagName("station");
						for (int i = 0; i < n.getLength(); i++) {
							stations.add(stationNames.get(n.item(i)
									.getTextContent()));
						}
						connected = true;
					} else {
						connected = false;
					}

				} catch (Exception e) {
					connected = false;
					Log.d("BartApp", e.toString());
				}

			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			Log.d("BartApp", "Api call interrupted");
		}

		if (!connected) {
			Toast.makeText(getApplicationContext(),
					"PLEASE CONNECT TO NETWORK", Toast.LENGTH_SHORT).show();
		}
	}

	OnItemSelectedListener routeSelect = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {

			routeSelected = parent.getItemAtPosition(arg2).toString()
					.split(" ")[1].split(" - ")[0];
			Log.d("BartApp", routeSelected);
			getStations(routeSelected);
			stationAdapter.clear();
			stationAdapter.addAll(stations);
			stationAdapter.notifyDataSetChanged();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO do nothing

		}
	};
}

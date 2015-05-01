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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.util.Log;

public class APICallHelper {

	static Document makeAPICall(String url) {
		Document doc = null;
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
			InputStream is = new BufferedInputStream(
					connection.getInputStream());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);
		} catch (UnknownHostException e) {
			Log.d("BartApp", e.toString());
			return doc;
		} catch (IOException e) {
			Log.d("BartApp", e.toString());
		} catch (Exception e) {
			Log.d("BartApp", e.toString());
		}

		return doc;
	}

}

package com.example.louis.customerinfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GetCustomerInfo extends AsyncTask<String[], Void, String[]> {

    private MainActivity activity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;
    private ProgressDialog pDialog;

    static final String id = "id";
    static final String firstname = "firstname";
    static final String lastname = "lastname";
    static final String street = "street";
    static final String city = "city";

    public GetCustomerInfo(MainActivity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setTitle("Get Customer Information from XML");
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    @Override
    protected String[] doInBackground(String[]... params) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            String[] result = parseXML(myParser);
            stream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
            return null;
        }
    }

    public String[] parseXML(XmlPullParser myParser) {

        int event;
        String[] result = new String[5];

        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase(id)) {
                            result[0] = myParser.nextText();
                        } else if (name.equalsIgnoreCase(firstname)) {
                            result[1] = myParser.nextText();
                        } else if (name.equalsIgnoreCase(lastname)) {
                            result[2] = myParser.nextText();
                        } else if (name.equalsIgnoreCase(street)) {
                            result[3] = myParser.nextText();
                        }else if (name.equalsIgnoreCase(city)) {
                            result[4] = myParser.nextText();
                        }
                        break;
                }
                event = myParser.next();
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        //call back data to main thread
        pDialog.dismiss();
        activity.displayData(result);

    }
}
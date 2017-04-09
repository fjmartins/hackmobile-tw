package com.tw.hackmob.saferide.async;

import android.os.AsyncTask;

import com.tw.hackmob.saferide.listener.DirectionListener;
import com.tw.hackmob.saferide.model.Route;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by phgm on 08/04/2017.
 */

public class DirectionAsync extends AsyncTask<Route, Void, Document> {

    private DirectionListener mListener;

    public DirectionAsync(DirectionListener listener) {
        mListener = listener;
    }

    @Override
    protected Document doInBackground(Route... routes) {
        String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + routes[0].getFrom().getLatitude() + "," + routes[0].getFrom().getLongitude()
                + "&destination=" + routes[0].getTo().getLatitude() + "," + routes[0].getTo().getLongitude()
                + "&sensor=false&units=metric&mode=driving";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Document document) {
        super.onPostExecute(document);

        mListener.getDocument(document);
    }
}

package com.tw.hackmob.saferide.async;

import android.app.Activity;
import android.os.AsyncTask;

import com.tw.hackmob.saferide.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by phgm on 09/04/2017.
 */

public class NotificationAsync extends AsyncTask<String[], Void, Void> {
    private Activity mActivity;

    public NotificationAsync(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String[]... strings) {
        String url = "https://fcm.googleapis.com/fcm/send";

        try {
            JSONObject notification = new JSONObject();
            notification.put("title", strings[0][1]);
            notification.put("body", strings[0][2]);
            notification.put("tag", strings[0][3]);

            JSONObject json = new JSONObject();
            json.put("to", strings[0][0]);
            json.put("notification", notification);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json.toString()));
            httpPost.setHeader("Authorization", mActivity.getString(R.string.firebase_server));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpPost, localContext);
            /*InputStream in = response.getEntity().getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            String result = sb.toString();*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

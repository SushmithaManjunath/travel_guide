package com.example.deepika.travelguide.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceAsynTask extends AsyncTask<String, Integer, String> {

    private String params[];
    private AsyncResponse delegate = null;

    /**
     *
     * @param params : index=0 is URL, index=1 is method type GET/POST, and rest are key/value parameters
     * @param delegate
     * @param activity
     */
    public WebServiceAsynTask(String params[], AsyncResponse delegate, Context activity) {
        this.params = params;
        this.delegate=delegate;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //executed on the background thread and not main thread

    /**
     *
     * @param strings
     * @return output as string from web service
     */
    @Override
    protected String doInBackground(String... strings) {
        return callService();
    }

    private String callService() {
        HttpURLConnection client;
        String response = "";
        try {

            URL reurl = new URL(params[0]);
            client = (HttpURLConnection) reurl.openConnection();
            client.setRequestMethod(params[1]);
            client.setDoOutput(true);
            StringBuilder urlparamters = new StringBuilder();

            int numKeyValue = (params.length - 1);

            for (int i = 2; i < numKeyValue; i = i + 2) {
                Log.d("urlparamters ", "inside loop " + i + " " + params.length);
                if (i == params.length - 2) {
                    urlparamters.append(params[i] + "=" + params[i + 1]);
                } else {
                    urlparamters.append(params[i] + "=" + params[i + 1] + "&");
                }
            }
            Log.d("URL called ", params[0] + "URL parameters" + urlparamters);
            OutputStream outputStream = new BufferedOutputStream(client.getOutputStream());
            outputStream.write(urlparamters.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            StringBuilder sb = new StringBuilder();
            Log.d("Web Service", "message : " + sb.toString());
            int responseCode = client.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Log.d("Web Service", "output " + sb.toString());
            delegate.processFinish(sb.toString());
            return sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}







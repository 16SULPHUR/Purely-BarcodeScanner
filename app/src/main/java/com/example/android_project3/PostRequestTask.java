package com.example.android_project3;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequestTask extends AsyncTask<Void, Void, String> {

    private String jsonBody;

    public PostRequestTask(String jsonData) {
        this.jsonBody = jsonData;
    }
    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://product-barcode-lookup-api.vercel.app/add/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set the necessary headers
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Create the JSON body



            Log.e("body", jsonBody);

            // Write the JSON body to the request
            OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
            os.write(jsonBody.getBytes());
            os.flush();
            os.close();

            // Get the response code
            int responseCode = urlConnection.getResponseCode();

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully sent the request
                // You can handle the response if needed
                String response = readStream(urlConnection.getInputStream());
                Log.d("PostRequest", "Request successful. Response: " + response);
            } else {
                // Handle the error
                Log.e("PostRequest", "Error: " + responseCode);
            }

            // Disconnect the HttpURLConnection
            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readStream(InputStream in) {
        try {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            return "";
        }
    }

}

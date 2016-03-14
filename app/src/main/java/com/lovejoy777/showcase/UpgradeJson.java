package com.lovejoy777.showcase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.lovejoy777.showcase.Helpers.getLayersJsonFile;

public class UpgradeJson extends AsyncTask<Void, String, Void> {

    private Context context;
    private boolean force;
    private Callback callback;
    private ProgressDialog progressShowcase;
    //private final String jsonData = "https://api.github.com/repos/BitSyko/layers_showcase_json/releases/latest";

    private String jsonFile, jsonVersion, version;
    private final String mysqlSwitch = "http://layersshowcase.x10.mx/app_switch.json";


    public UpgradeJson(Context context, boolean force, Callback callback) {
        this.context = context;
        this.force = force;
        this.callback = callback;
    }

    public UpgradeJson(Context context, boolean force) {
        this(context, force, null);
    }

    @Override
    protected void onPreExecute() {
        progressShowcase = ProgressDialog.show(context, "Downloading",
                "Updating database...", true);
        progressShowcase.setCancelable(false);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        String dlMysqlSwitch = downloadFile(mysqlSwitch);

        JsonNode actualObj;

        if (dlMysqlSwitch == null) {
            publishProgress("Download failed");
            return null;
        }

        try {
            actualObj = new ObjectMapper().readTree(dlMysqlSwitch);
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Download failed");
            return null;
        }

        String mysqlSwitchValue = actualObj.get("mysql_switch").asText();

        if(mysqlSwitchValue == "false"){
            jsonVersion = "https://api.github.com/repos/BitSyko/layers_showcase_json/releases/latest";
            String jsonInfo = downloadFile(jsonVersion);
            if (jsonInfo == null) {
                publishProgress("Download failed");
                return null;
            }
            try {
                actualObj = new ObjectMapper().readTree(jsonInfo);
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress("Download failed");
                return null;
            }
            version = actualObj.get("tag_name").asText();
            jsonFile = "https://github.com/BitSyko/layers_showcase_json/releases/download/" + version + "/showcase.json";
        }else if (mysqlSwitchValue == "true"){
            jsonFile = "http://layersshowcase.x10.mx/v1/layers";
            jsonVersion = "http://layersshowcase.x10.mx/v1/getversion";
            String jsonInfo = downloadFile(jsonVersion);
            if (jsonInfo == null) {
                publishProgress("Download failed");
                return null;
            }
            try {
                actualObj = new ObjectMapper().readTree(jsonInfo);
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress("Download failed");
                return null;
            }
            version = actualObj.get("version_id").asText();
        }

        //Compare version with one in preferences
        if (!force && context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("version_id", "0").equals(version)) {
            return null;
        }

        String layersJson = downloadFile(jsonFile);

        if (layersJson == null) {
            publishProgress("Download failed");
            return null;
        }

        //Just for testing
        try {
            new ObjectMapper().readTree(layersJson);
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Download failed");
            return null;
        }

        //We update settings only after successful download
        context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("version_id", version).apply();

        try {
            Files.write(layersJson, getLayersJsonFile(context), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if ((progressShowcase != null) && progressShowcase.isShowing()) {
            progressShowcase.dismiss();
        }

        if (callback != null) {
            callback.callback();
        }

    }


    private String downloadFile(String url) {

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String theString;

        try {

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // download showcase json file
            inputStream = connection.getInputStream();

            theString = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));

        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {

            }

            if (connection != null)
                connection.disconnect();
        }


        return theString;

    }


}
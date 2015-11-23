package com.lovejoy777.showcase.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lovejoy777.showcase.Helpers;
import com.lovejoy777.showcase.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

//TODO
//Add image uploading to database
public class SubmitActivity extends AppCompatActivity {
    public final int[] EditText = {R.id.title, R.id.description, R.id.author, R.id.link,
            R.id.backup_link, R.id.icon, R.id.promo, R.id.screenshot_1, R.id.screenshot_2, R.id.screenshot_3,
            R.id.googleplus, R.id.version, R.id.donate_link, R.id.donate_version, R.id.wallpaper, R.id.plugin_version,
            R.id.toolbar_background_color};
    public final int[] CheckBox = {R.id.for_L, R.id.for_M, R.id.basic, R.id.basic_m,
            R.id.type2, R.id.type3, R.id.type3_m, R.id.touchwiz, R.id.lg, R.id.sense,
            R.id.xperia, R.id.zenui, R.id.hdpi, R.id.mdpi, R.id.xhdpi, R.id.xxhdpi, R.id.xxxhdpi, R.id.free,
            R.id.donate, R.id.paid, R.id.needs_update, R.id.will_update, R.id.bootani, R.id.font, R.id.iconpack};
    Button generate;
    String[] strings = new String[]{"title", "description", "author", "link", "backup_link", "icon",
            "promo", "screenshot_1", "screenshot_2", "screenshot_3", "googleplus", "version", "donate_link",
            "donate_version", "wallpaper", "plugin_version", "toolbar_background_color", "for_L", "for_M",
            "basic", "basic_m", "type2", "type3", "type3_m", "touchwiz", "lg", "sense", "xperia", "zenui",
            "hdpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi", "free", "donate", "paid", "needs_update", "will_update",
            "bootani", "font", "iconpack"};
    ArrayList<String> values = new ArrayList<String>(Arrays.asList(strings));
    ArrayList<String> values2 = new ArrayList<String>();

    ProgressDialog prgDialog;
    EditText nameText, emailText, passwordText;
    ViewFlipper viewFlipper;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("1")) {
            toolbar.setTitle(R.string.Register);
            viewFlipper.setDisplayedChild(0);
        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("2")) {
            toolbar.setTitle(R.string.Login);
            viewFlipper.setDisplayedChild(1);
        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("3")) {
            viewFlipper.setDisplayedChild(2);
        }

        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        generate = (Button) findViewById(R.id.generate);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.PleaseWait));
        prgDialog.setCancelable(false);
    }

    public void invokeWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("apiKey", "null"));
        client.post(getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("apiURL", "http://showcaseapi.x10.mx/v1/register"), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    String response = new String(responseBody);
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("1")) {
                            toolbar.setTitle(R.string.login);
                            viewFlipper.setInAnimation(SubmitActivity.this, R.anim.anni1);
                            viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.anni2);
                            viewFlipper.showNext();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "2").apply();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://showcaseapi.x10.mx/v1/login").apply();
                        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("2")) {
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiKey", obj.getString("apiKey")).apply();
                            toolbar.setTitle(R.string.Submit);
                            viewFlipper.setInAnimation(SubmitActivity.this, R.anim.anni1);
                            viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.anni2);
                            viewFlipper.showNext();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "3").apply();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://showcaseapi.x10.mx/v1/layers").apply();
                        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("3")) {
                            View coordinatorLayoutView = findViewById(R.id.snackbar);
                            Snackbar snack = Snackbar.make(coordinatorLayoutView, R.string.SucessfullySubmited, Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            view.setBackgroundColor(Color.parseColor("#F44336"));
                            snack.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.ErrorInvalidResponsePossible, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), R.string.RessourceNotFound, Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), R.string.ServerProblem, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.UnexpectedError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }

    public void registerUser(View view) {
        nameText = (EditText) findViewById(R.id.input_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        RequestParams params = new RequestParams();
        if (Helpers.isNotNull(name) && Helpers.isNotNull(email) && Helpers.isNotNull(password)) {
            if (Helpers.validate(email)) {
                params.put(getString(R.string.Name), name);
                params.put(getString(R.string.Email), email);
                params.put(getString(R.string.Password), password);
                invokeWS(params);
            } else {
                Toast.makeText(getApplicationContext(), R.string.EnterValidEmail, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoBlankFieldsPlease, Toast.LENGTH_LONG).show();
        }

    }

    public void loginUser(View view) {
        emailText = (EditText) findViewById(R.id.input_email2);
        passwordText = (EditText) findViewById(R.id.input_password2);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        RequestParams params = new RequestParams();
        if (Helpers.isNotNull(email) && Helpers.isNotNull(password)) {
            if (Helpers.validate(email)) {
                params.put("email", email);
                params.put("password", password);
                invokeWS(params);
            } else {
                Toast.makeText(getApplicationContext(), R.string.EnterValidEmail, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoBlankFieldsPlease, Toast.LENGTH_LONG).show();
        }
    }

    public void submitLayer(View view) {
        values2.clear();
        for (int id : EditText) {
            EditText t = (EditText) findViewById(id);
            values2.add((t.getText().toString().trim().length() > 0) ? t.getText().toString() : "false");
        }
        for (int id2 : CheckBox) {
            CheckBox c = (CheckBox) findViewById(id2);
            values2.add(String.valueOf(c.isChecked()));
        }
        RequestParams params = new RequestParams();
        for (int i = 0; i < values.size(); i++) {
            String json1 = values.get(i);
            String json2 = values2.get(i);
            //writer.name(json1).value(json2);
            params.put(json1, json2);
        }
        invokeWS(params);
    }

    public void nextView(View view) {
        viewFlipper.setInAnimation(SubmitActivity.this, R.anim.anni1);
        viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.anni2);
        viewFlipper.showNext();
        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("1")) {
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "2").apply();
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://showcaseapi.x10.mx/v1/login").apply();
            toolbar.setTitle(R.string.Login);
        }
    }

    public void prevView(View view) {
        viewFlipper.setInAnimation(SubmitActivity.this, R.anim.back1);
        viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.back2);
        viewFlipper.showPrevious();
        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("2")) {
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "1").apply();
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://showcaseapi.x10.mx/v1/register").apply();
            toolbar.setTitle(R.string.Register);
        }
    }
}
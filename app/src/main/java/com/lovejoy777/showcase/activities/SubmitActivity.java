package com.lovejoy777.showcase.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.nononsenseapps.filepicker.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class SubmitActivity extends AppCompatActivity {
    public final int[] EditText = {R.id.title, R.id.description, R.id.author, R.id.link,
            R.id.backup_link,
            R.id.googleplus, R.id.version, R.id.donate_link, R.id.donate_version, R.id.plugin_version,
            R.id.toolbar_background_color};
    public final int[] CheckBox = {R.id.for_L, R.id.for_M, R.id.basic, R.id.basic_m,
            R.id.type2, R.id.type3, R.id.type3_m, R.id.touchwiz, R.id.lg, R.id.sense,
            R.id.xperia, R.id.zenui, R.id.hdpi, R.id.mdpi, R.id.xhdpi, R.id.xxhdpi, R.id.xxxhdpi, R.id.free,
            R.id.donate, R.id.paid, R.id.bootani, R.id.font};

    String[] strings = new String[]{"title", "description", "author", "link", "backup_link", "googleplus", "version", "donate_link",
            "donate_version", "plugin_version", "toolbar_background_color", "for_L", "for_M",
            "basic", "basic_m", "type2", "type3", "type3_m", "touchwiz", "lg", "sense", "xperia", "zenui",
            "hdpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi", "free", "donate", "paid", "bootani", "font"};
    ArrayList<String> values = new ArrayList<String>(Arrays.asList(strings));
    ArrayList<String> values2 = new ArrayList<String>();
    ArrayList<String> valuesImages = new ArrayList<String>();
    ArrayList<String> imageStrings = new ArrayList<String>();

    ProgressDialog prgDialog;
    EditText nameText, emailText, passwordText;
    Button b_icon, b_promo, b_s1, b_s2, b_s3, b_apk;
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

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.PleaseWait));
        prgDialog.setCancelable(false);
    }

    public void invokeWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("apiKey", "null"));
        client.post(getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("apiURL", "http://layersshowcase.x10.mx/v1/register"), params, new AsyncHttpResponseHandler() {
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
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://layersshowcase.x10.mx/v1/login").apply();
                        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("2")) {
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiKey", obj.getString("apiKey")).apply();
                            toolbar.setTitle(R.string.Submit);
                            viewFlipper.setInAnimation(SubmitActivity.this, R.anim.anni1);
                            viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.anni2);
                            viewFlipper.showNext();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "3").apply();
                            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://layersshowcase.x10.mx/v1/layers").apply();
                        } else if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("3")) {
                            View coordinatorLayoutView = findViewById(R.id.snackbar);
                            Snackbar snack = Snackbar.make(coordinatorLayoutView, R.string.SucessfullySubmited, Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            view.setBackgroundColor(Color.parseColor("#F44336"));
                            snack.show();
                            valuesImages.clear();
                            imageStrings.clear();
                            File sd = Environment.getExternalStorageDirectory();
                            File folder = new File(sd + "/Showcase");
                            if (folder.isDirectory()) {
                                File[] children = folder.listFiles();
                                for (File child : children) {
                                    child.delete();
                                }

                                folder.delete();
                            }
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
            params.put(json1, json2);
        }
        for (int i = 0; i < valuesImages.size(); i++) {
            String json3 = valuesImages.get(i);
            String json4 = imageStrings.get(i);
            File myFile = new File(json3);
            try {
                params.put(json4, myFile);
            } catch (FileNotFoundException e) {
            }
        }
        invokeWS(params);
    }

    public void nextView(View view) {
        viewFlipper.setInAnimation(SubmitActivity.this, R.anim.anni1);
        viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.anni2);
        viewFlipper.showNext();
        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("1")) {
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "2").apply();
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://layersshowcase.x10.mx/v1/login").apply();
            toolbar.setTitle(R.string.Login);
        }
    }

    public void prevView(View view) {
        viewFlipper.setInAnimation(SubmitActivity.this, R.anim.back1);
        viewFlipper.setOutAnimation(SubmitActivity.this, R.anim.back2);
        viewFlipper.showPrevious();
        if (getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("view", "1").equals("2")) {
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("view", "1").apply();
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("apiURL", "http://layersshowcase.x10.mx/v1/register").apply();
            toolbar.setTitle(R.string.Register);
        }
    }

    public void filePick(View view) {
        Intent i = new Intent(this, FilePickerActivity.class);
        Intent ic = new Intent(this, Chooser.class);
        switch (view.getId()) {
            case R.id.icon:
                startActivityForResult(i, 1);
                break;
            case R.id.promo:
                startActivityForResult(i, 2);
                break;
            case R.id.screenshot_1:
                startActivityForResult(i, 3);
                break;
            case R.id.screenshot_2:
                startActivityForResult(i, 4);
                break;
            case R.id.screenshot_3:
                startActivityForResult(i, 5);
                break;
            case R.id.wallpaper:
                startActivityForResult(i, 6);
                break;
            case R.id.apk:
                startActivityForResult(ic, 7);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File myFile = new File(uri.getPath());
            String path = myFile.getAbsolutePath();
            valuesImages.add(path);
            if (requestCode == 1) {
                imageStrings.add("icon");
            } else if (requestCode == 2) {
                imageStrings.add("promo");
            } else if (requestCode == 3) {
                imageStrings.add("screenshot_1");
            } else if (requestCode == 4) {
                imageStrings.add("screenshot_2");
            } else if (requestCode == 5) {
                imageStrings.add("screenshot_3");
            } else if (requestCode == 6) {
                imageStrings.add("wallpaper");
            }
        } else if (requestCode == 7) {
            try {
                String pn = getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("pn", "null");
                PackageManager pm = getPackageManager();
                Resources resources = pm.getResourcesForApplication(pn);
                b_icon = (Button) findViewById(R.id.icon);
                b_promo = (Button) findViewById(R.id.promo);
                b_s1 = (Button) findViewById(R.id.screenshot_1);
                b_s2 = (Button) findViewById(R.id.screenshot_3);
                b_s3 = (Button) findViewById(R.id.screenshot_2);
                b_apk = (Button) findViewById(R.id.apk);
                int[] images = {(resources.getIdentifier("icon", "drawable", pn)), (resources.getIdentifier("heroimage", "drawable", pn)), (resources.getIdentifier("screenshot1", "drawable", pn)), (resources.getIdentifier("screenshot2", "drawable", pn)), (resources.getIdentifier("screenshot3", "drawable", pn))};
                for(int i=0; i < images.length; i++){
                    Drawable d_image = resources.getDrawable(images[i], null);
                    Bitmap b_image = ((BitmapDrawable) d_image).getBitmap();
                    String name = "image_"+ String.valueOf(i)+".png" ;

                    File sd = Environment.getExternalStorageDirectory();
                    File folder = new File(sd + "/Showcase");
                    folder.mkdir();

                    File dest = new File(folder, name);
                    String d_path = dest.getAbsolutePath();
                    try {
                        FileOutputStream out;
                        out = new FileOutputStream(dest);
                        b_image.compress(Bitmap.CompressFormat.PNG, 100, out);
                        valuesImages.add(d_path);
                        b_icon.setEnabled(false);
                        b_promo.setEnabled(false);
                        b_s1.setEnabled(false);
                        b_s2.setEnabled(false);
                        b_s3.setEnabled(false);
                        b_apk.setEnabled(false);
                        out.flush();
                        out.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                imageStrings.add("icon");
                imageStrings.add("promo");
                imageStrings.add("screenshot_1");
                imageStrings.add("screenshot_2");
                imageStrings.add("screenshot_3");
                ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(pn, PackageManager.GET_META_DATA);
                Bundle bundle = applicationInfo.metaData;
                String name = bundle.getString("Layers_Name");
                String dev = bundle.getString("Layers_Developer");
                String desc = bundle.getString("Layers_Description");
                EditText e_name = (EditText)findViewById(R.id.title);
                EditText e_dev = (EditText)findViewById(R.id.author);
                EditText e_desc = (EditText)findViewById(R.id.description);
                EditText e_pversion = (EditText)findViewById(R.id.plugin_version);
                e_name.setText(name, TextView.BufferType.EDITABLE);
                e_dev.setText(dev, TextView.BufferType.EDITABLE);
                e_desc.setText(desc, TextView.BufferType.EDITABLE);
                if (bundle.containsKey("Layers_PluginVersion")) {
                    String pversion = bundle.getString("Layers_PluginVersion");
                    e_pversion.setText(pversion, TextView.BufferType.EDITABLE);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

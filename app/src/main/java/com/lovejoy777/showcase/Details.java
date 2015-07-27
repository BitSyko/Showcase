package com.lovejoy777.showcase;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Details extends AppCompatActivity {

    private Activity activity;
    final ImageView ScreenshotimageView[] = new ImageView[3];
    Bitmap bitmap[] = new Bitmap[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        activity = this;

        // Handle ToolBar
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(screenSize);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get String SZP
        final Intent extras = getIntent();

        final Theme theme = (Theme) extras.getSerializableExtra("theme");

        // Asign Views
        ImageView promoimg = (ImageView) findViewById(R.id.promo);
        TextView txt2 = (TextView) findViewById(R.id.tvdescription);
        TextView developertv = (TextView) findViewById(R.id.tvDeveloper);

        // Set text & image Views
        collapsingToolbar.setTitle(theme.getTitle());

        Glide.with(this).load(theme.getPromo()).asBitmap().placeholder(R.drawable.loadingpromo).into(promoimg);

        txt2.setText(theme.getDescription());
        developertv.setText(theme.getAuthor());

        // Scroll view with screenshots
        LinearLayout screenshotLayout = (LinearLayout) findViewById(R.id.LinearLayoutScreenshots);

        //   screenshotLayout.getLayoutParams().height = screenSize.y / 2;
        //   screenshotLayout.requestLayout();

        final String[] screenshotsUrls = {
                theme.getScreenshot_1(),
                theme.getScreenshot_2(),
                theme.getScreenshot_3()};


        float height = screenSize.y / 2;

        Log.d("Height: ", String.valueOf(height));

        for (int i = 0; i < 3; i++) {
            ScreenshotimageView[i] = new ImageView(this);

            screenshotLayout.addView(ScreenshotimageView[i]);

            final int finalI = i;
            ScreenshotimageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FullScreenActivity.launch(activity, (ImageView) view, screenshotsUrls[finalI], "img");
                }
            });

            Glide.with(this)
                    .load(screenshotsUrls[finalI])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (height * 0.66), (int) height)
                    .into(ScreenshotimageView[finalI]);

        }

        // Install button
        Button installbutton;
        installbutton = (Button) findViewById(R.id.button);

        installbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = theme.isDonate() ? theme.getDonate_link() : theme.getLink();

                Intent installtheme = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(installtheme, bndlanimation);

            }
        }); // End install button

        // Google Plus button
        Button googleplusbutton;
        googleplusbutton = (Button) findViewById(R.id.button2);

        googleplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent googleplustheme = new Intent(Intent.ACTION_VIEW, Uri.parse(theme.getGoogleplus()));

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(googleplustheme, bndlanimation);

            }
        }); // End Google Plus button


        //Properties table

        LinearLayout propertiesHolder = (LinearLayout) findViewById(R.id.properties);


        propertiesHolder.addView(createRow("mdpi", theme.isMdpi()));
        propertiesHolder.addView(createRow("hdpi", theme.isHdpi()));
        propertiesHolder.addView(createRow("xhdpi", theme.isXhdpi()));
        propertiesHolder.addView(createRow("xxhdpi", theme.isXxhdpi()));
        propertiesHolder.addView(createRow("xxxhdpi", theme.isXxxhdpi()));


    } // End onCreate

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


    public View createRow(String text, boolean isChecked) {

        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(text);
        checkBox.setChecked(isChecked);
        checkBox.setClickable(false);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        row.addView(checkBox);

        return row;

    }

}


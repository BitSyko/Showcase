package com.lovejoy777.showcase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends Activity {

    //#BlameAndrew
    private static Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen);

        int bgColor = ((BitmapDrawable) drawable).getBitmap().getPixel(0, 0);

        final ImageView image = (ImageView) findViewById(R.id.image);
        image.setBackgroundColor(bgColor);

        Picasso.with(this)
                .load(getIntent().getStringExtra("url"))
                .fit()
                .centerInside()
                .placeholder(drawable)
                .into(image);

    }

    public static void launch(Activity activity, ImageView transitionView, String url, String id) {

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, id);

        drawable = transitionView.getDrawable();
        transitionView.setTransitionName(id);
        Intent intent = new Intent(activity, FullScreenActivity.class);
        intent.putExtra("url", url);

        ActivityCompat.startActivity(activity, intent, options.toBundle());

    }

    public void click(View view) {
        this.finishAfterTransition();
    }

}
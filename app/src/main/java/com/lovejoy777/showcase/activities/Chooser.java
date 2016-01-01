package com.lovejoy777.showcase.activities;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lovejoy777.showcase.R;

import java.util.ArrayList;
import java.util.List;

public class Chooser extends ListActivity {
    AppAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_layout);
        PackageManager pm = getPackageManager();
        Intent main = new Intent("com.layers.plugins.PICK_OVERLAYS");
        ArrayList<ResolveInfo> list = (ArrayList<ResolveInfo>) pm.queryIntentServices(main, PackageManager.GET_RESOLVED_FILTER);
        adapter = new AppAdapter(pm, list);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        ResolveInfo ri = adapter.getItem(position);
        ServiceInfo activity = ri.serviceInfo;
        ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

        String pack_name = name.getPackageName();
        getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putString("pn", pack_name).apply();
        setResult(7);
        finish();
    }

    class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm = null;

        AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
            super(Chooser.this, R.layout.row, apps);
            this.pm = pm;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = newView(parent);
            }

            bindView(position, convertView);

            return (convertView);
        }

        private View newView(ViewGroup parent) {
            return (getLayoutInflater().inflate(R.layout.row, parent, false));
        }

        private void bindView(int position, View row) {
            TextView label = (TextView) row.findViewById(R.id.label);

            label.setText(getItem(position).loadLabel(pm));

            ImageView icon = (ImageView) row.findViewById(R.id.icon);

            icon.setImageDrawable(getItem(position).loadIcon(pm));
        }
    }


}

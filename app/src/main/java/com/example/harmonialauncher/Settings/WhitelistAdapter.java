package com.example.harmonialauncher.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harmonialauncher.Adapters.AppListAdapter;
import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.R;

import java.util.ArrayList;

public class WhitelistAdapter extends AppListAdapter {

    private final static String TAG = "Whitelist Adapter";

    private static ArrayList<AppObject> whitelistedApps = new ArrayList<AppObject>();

    //Resource should be R.layout.list_menu_item
    public WhitelistAdapter(Context c, int resource) {
        super(c, resource);
    }

    public WhitelistAdapter(Context c, int resource, ArrayList<AppObject> apps) {
        super(c, resource);
        super.apps = apps;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // View v = convertView;

        LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null, true);

        AppObject app = apps.get(position);

        //Set app name
        TextView text = v.findViewById(R.id.whitelist_text);
        text.setText(app.getName());

        //Set app icon
        ImageView icon = v.findViewById(R.id.whitelist_icon);
        Drawable image = app.getImage();
        if (image == null) {
            if (Build.VERSION.SDK_INT > 21)
                image = CONTEXT.getResources().getDrawable(app.getImageId(), null);
            else
                image = CONTEXT.getResources().getDrawable(app.getImageId());
        }
        icon.setImageDrawable(image);

        //Set app whitelisted checkbox to unchecked
        CheckBox check = v.findViewById(R.id.whitelist_checkbox);
        check.setChecked(WhitelistManager.isWhitelisted(app.getPackageName()));

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                    v.callOnClick();
            }
        });


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = v.findViewById(R.id.whitelist_checkbox);
                String name = ((TextView) v.findViewById(R.id.whitelist_text)).getText().toString();
                AppObject app = findAppByName(name);
                if (c.isChecked())
                    WhitelistManager.whitelist(app.getPackageName());
                else
                    WhitelistManager.unWhitelist(app.getPackageName());
            }
        });

        return v;
    }
}

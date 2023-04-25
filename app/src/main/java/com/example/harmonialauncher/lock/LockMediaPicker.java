package com.example.harmonialauncher.lock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.R;

import java.util.List;

public class LockMediaPicker extends HarmoniaFragment {
    private final static String TAG = LockMediaPicker.class.getSimpleName();

    private final String[] media = new String[] {"Apps", "Websites"};

    public LockMediaPicker(int resource) {
        super(resource);
    }

    public LockMediaPicker() {
        super(R.layout.lock_media_picker);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.lock_media_picker, container, false);

        GridView gv = v.findViewById(R.id.lock_media_grid_view);
        LockMediaAdapter adapter = new LockMediaAdapter(requireContext(), R.id.lock_media_grid_view, media);

        gv.setAdapter(adapter);
        gv.setNumColumns(2);

        v.setBackgroundColor(getResources().getColor(R.color.settings_activity_background));

        return v;
    }

    public View createCardViewButton(Drawable icon, @NonNull String text)
    {
        View c = getLayoutInflater().inflate(R.layout.cardview_button, null, false);

        if (icon != null) {
            ImageView image = c.findViewById(R.id.card_image_view);
            image.setImageDrawable(icon);
        }

        TextView title = c.findViewById(R.id.card_button_text);
        title.setText(text);

        return c;
    }

    public class LockMediaAdapter extends ArrayAdapter<String>
    {

        public LockMediaAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public LockMediaAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public LockMediaAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        public LockMediaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public LockMediaAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        public LockMediaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            View c = createCardViewButton(null, media[position]);

            c.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //TODO: implement on touch listener for media selector card view
                    ((LockActivity)requireActivity()).moveToAppPicker();
                    return false;
                }
            });

            return c;
        }

    }
}

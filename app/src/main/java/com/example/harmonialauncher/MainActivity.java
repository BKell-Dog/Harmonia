package com.example.harmonialauncher;

import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.lockManager.HarmoniaActivity;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */

public class MainActivity extends HarmoniaActivity {
    private static final String TAG = "Main Activity";
    public final Context CONTEXT = MainActivity.this;
    public static Application instance;
    public ViewPager2 vp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager2) findViewById(R.id.ViewPager);

        PageAdapter pa = new PageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(true);
        vp.canScrollVertically(0);

        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        instance = this.getApplication();

        /*final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try{
            wallpaperManager.setResource(R.drawable.gradient_background);
            Log.d(TAG, "WALLPAPER RUNNING");
        }catch(IOException ioe){Log.d(TAG, "EXCEPTION");ioe.printStackTrace();}*/
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    //Handler for swipe events
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }

            /**
             * left to right swipe
             */
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                vp.setCurrentItem(0, true);


                /**
                 * right to left
                 */
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                //prev image
                vp.setCurrentItem(1, true);

            }

        } catch (Exception e) {

        }
        return false;
    }

    public class DetectSwipeDirectionActivity extends HarmoniaActivity {

        // This textview is used to display swipe or tap status info.
        private TextView textView = null;

        // This is the gesture detector compat instance.
        private GestureDetectorCompat gestureDetectorCompat = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Create a common gesture listener object.
            DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();

            // Set activity in the listener.
            gestureListener.setActivity(this);

            // Create the gesture detector with the gesture listener.
            gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Pass activity on touch event to the gesture detector.
            gestureDetectorCompat.onTouchEvent(event);
            // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
            return true;
        }

        public void displayMessage(String message)
        {
            if(textView!=null)
            {
                // Display text in the text view.
                textView.setText(message);
            }
        }
    }


    public class DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        // Minimal x and y axis swipe distance.
        private static int MIN_SWIPE_DISTANCE_X = 100;
        private static int MIN_SWIPE_DISTANCE_Y = 100;

        // Maximal x and y axis swipe distance.
        private static int MAX_SWIPE_DISTANCE_X = 1000;
        private static int MAX_SWIPE_DISTANCE_Y = 1000;

        // Source activity that display message in text view.
        private DetectSwipeDirectionActivity activity = null;

        public DetectSwipeDirectionActivity getActivity() {
            return activity;
        }

        public void setActivity(DetectSwipeDirectionActivity activity) {
            this.activity = activity;
        }

        /* This method is invoked when a swipe gesture happened. */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Get swipe delta value in x axis.
            float deltaX = e1.getX() - e2.getX();

            // Get swipe delta value in y axis.
            float deltaY = e1.getY() - e2.getY();

            // Get absolute value.
            float deltaXAbs = Math.abs(deltaX);
            float deltaYAbs = Math.abs(deltaY);

            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
            if((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X))
            {
                if(deltaX > 0)
                {
                    this.activity.displayMessage("Swipe to left");
                }else
                {
                    this.activity.displayMessage("Swipe to right");
                }
            }

            if((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y))
            {
                if(deltaY > 0)
                {
                    this.activity.displayMessage("Swipe to up");
                }else
                {
                    this.activity.displayMessage("Swipe to down");
                }
            }


            return true;
        }

        // Invoked when single tap screen.
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            this.activity.displayMessage("Single tap occurred.");
            return true;
        }

        // Invoked when double tap screen.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            this.activity.displayMessage("Double tap occurred.");
            return true;
        }
    }



}
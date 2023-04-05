package com.example.harmonialauncher.lock.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.appgrid.views.AppView;

public class CardViewButton extends CardView {

    public CardViewButton(@NonNull Context context) {
        super(context);
        setupAttributes();
    }

    public CardViewButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupAttributes();
    }

    public CardViewButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes();
    }

    public void setupAttributes()
    {
        setId(R.id.card_view);
        setTag("card_view");
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setBackgroundColor(getResources().getColor(R.color.light_blue));
        setPadding(10, 10, 10, 10);
    }

    public TextView getTextView()
    {
        // CardView -> ConstraintLayout -> TextView (at index 0)
        ConstraintLayout cl = (ConstraintLayout) getChildAt(0);
        return (TextView) cl.getChildAt(0);
    }

    public ImageView getImageView()
    {
        // CardView -> ConstraintLayout -> Imageview (at index 0 or 1)
        ConstraintLayout cl = (ConstraintLayout) getChildAt(0);

        if (cl.getChildAt(0) instanceof ImageView)
            return (ImageView) cl.getChildAt(0);
        else if (cl.getChildAt(1) instanceof ImageView)
            return (ImageView) cl.getChildAt(1);

        return null;
    }

    public void setText(String text)
    {
        getTextView().setText(text);
    }

    public String getText()
    {
        return getTextView().getText().toString();
    }

    public void setIcon(@NonNull Drawable icon)
    {
        getImageView().setImageDrawable(icon);
    }

    public static class CardViewButtonFactory {

        @SuppressLint("UseCompatLoadingForDrawables")
        public static CardViewButton createAppView(@NonNull Context context, String text, Drawable icon) {
            CardViewButton card = new CardViewButton(context);
            initialize(context, card, text, icon);
            return card;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public static CardViewButton createAppView(Context context, @Nullable AttributeSet attrs, String text, Drawable icon) {
            CardViewButton card = new CardViewButton(context);
            initialize(context, card, text, icon);
            return card;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public static CardViewButton createAppView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, String text, Drawable icon) {
            CardViewButton card = new CardViewButton(context);
            initialize(context, card, text, icon);
            return card;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public static CardViewButton createAppView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, String text, Drawable icon) {
            CardViewButton card = new CardViewButton(context);
            initialize(context, card, text, icon);
            return card;
        }

        private static void initialize(@NonNull Context context, @NonNull CardViewButton card, String text, Drawable icon)
        {
            // Add constraint layout to card view
            ConstraintLayout cl = new ConstraintLayout(context);
            cl.setId(R.id.card_constraint);
            card.addView(cl);

            //Add image view and text view to constraint layout
            if (text != null && icon != null) {
                TextView textView = new TextView(context);
                textView.setText(text);
                textView.setId(R.id.card_button_text);
                textView.setText(text);
                cl.addView(textView);

                // Set constraints for text view
                ConstraintSet set = new ConstraintSet();
                set.clone(cl);
                set.connect(R.id.card_button_text, ConstraintSet.START, R.id.card_constraint, ConstraintSet.START);
                set.connect(R.id.card_button_text, ConstraintSet.TOP, R.id.card_constraint, ConstraintSet.TOP);
                set.connect(R.id.card_button_text, ConstraintSet.END, R.id.card_constraint, ConstraintSet.END);
                set.applyTo(cl);


                ImageView image = new ImageView(context);
                image.setImageDrawable(icon);
                image.setId(R.id.card_image_view);
                image.setImageDrawable(icon);
                cl.addView(image);

                // Set constraints for text view
                set = new ConstraintSet();
                set.clone(cl);
                set.connect(R.id.card_image_view, ConstraintSet.START, R.id.card_constraint, ConstraintSet.START);
                set.connect(R.id.card_image_view, ConstraintSet.TOP, R.id.card_button_text, ConstraintSet.BOTTOM);
                set.connect(R.id.card_image_view, ConstraintSet.END, R.id.card_constraint, ConstraintSet.END);
                set.connect(R.id.card_image_view, ConstraintSet.BOTTOM, R.id.card_constraint, ConstraintSet.BOTTOM);
                set.applyTo(cl);
            }
            else if (text != null)
            {
                TextView textView = new TextView(context);
                textView.setText(text);
                textView.setId(R.id.card_button_text);
                textView.setText(text);
                cl.addView(textView);

                // Set constraints for text view
                ConstraintSet set = new ConstraintSet();
                set.clone(cl);
                set.connect(R.id.card_button_text, ConstraintSet.START, R.id.card_constraint, ConstraintSet.START);
                set.connect(R.id.card_button_text, ConstraintSet.TOP, R.id.card_constraint, ConstraintSet.TOP);
                set.connect(R.id.card_button_text, ConstraintSet.END, R.id.card_constraint, ConstraintSet.END);
                set.applyTo(cl);
            }
            else if (icon != null)
            {
                ImageView image = new ImageView(context);
                image.setImageDrawable(icon);
                image.setId(R.id.card_image_view);
                image.setImageDrawable(icon);
                cl.addView(image);

                // Set constraints for text view
                ConstraintSet set = new ConstraintSet();
                set.clone(cl);
                set.connect(R.id.card_image_view, ConstraintSet.START, R.id.card_constraint, ConstraintSet.START);
                set.connect(R.id.card_image_view, ConstraintSet.TOP, R.id.card_button_text, ConstraintSet.BOTTOM);
                set.connect(R.id.card_image_view, ConstraintSet.END, R.id.card_constraint, ConstraintSet.END);
                set.connect(R.id.card_image_view, ConstraintSet.BOTTOM, R.id.card_constraint, ConstraintSet.BOTTOM);
                set.applyTo(cl);
            }
        }
    }
}

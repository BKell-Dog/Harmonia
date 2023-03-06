package com.example.harmonialauncher.error;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.harmonialauncher.R;

public class ErrorMessageDialog {


    public static void showDialog(Context c, Exception e)
    {
        showDialog(c, e, null);
    }
    public static void showDialog(Context c, Exception e, @Nullable String message)
    {
        final Dialog d = new Dialog(c);

        //Disable default title generated by android
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Allow user to cancel dialog by clicking anywhere outside dialog box
        d.setCancelable(true);
        d.setContentView(R.layout.error_detail_screen);
        d.show();

        StringBuilder builder = new StringBuilder();

        if (e.getMessage() != null)
            builder.append("ERROR: ").append(e.getMessage()).append("\n");

        if (e.getCause() != null)
            builder.append("CAUSED BY: ").append(e.getCause()).append("\n");

        if (e.getStackTrace().length > 0) {
            builder.append("AT: ");
            for (StackTraceElement elem : e.getStackTrace())
                builder.append(elem.toString()).append("\n");
        }

        if (message != null)
            builder.append(message);

        TextView text = d.findViewById(R.id.errorText);
        text.setText(builder.toString());
    }
}
package com.example.harmonialauncher.Helpers;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeHelper {

    /**
     * Timestamp represents the specified time in absolute terms: with reference to 1/1/1970 00:00:00 GMT
     */
    private Timestamp time;
    public static final String MMDDYYYY = "MMDDYYYY",
            HHMM = "HHMM";
    /**
     * INPUT_ABSOLUTE indicates that the number of milliseconds passed to the constructor is in
     * reference to the Java Epoch of 1/1/1970 00:00:00 GMT. INPUT_RELATIVE indicates that the milli
     * value being passed represents a length of time beginning now and stretching into the future.
     * This is done because the LockManager class works in absolute time, but the LockActivity UI
     * class works in relative time.
     */
    public static final String INPUT_ABSOLUTE = "ABSOLUTE",
                                INPUT_RELATIVE = "RELATIVE";


    public TimeHelper(long millis, String inputMode) {
        if (inputMode.equalsIgnoreCase(INPUT_RELATIVE))
            time = new Timestamp(millis + now());
        else
            time = new Timestamp(millis);
    }

    public TimeHelper(int hours, int minutes, String inputMode) {
        if (inputMode.equalsIgnoreCase(INPUT_RELATIVE))
            time = new Timestamp(hoursToMillis(hours) + minutesToMillis(minutes) + now());
        else
            time = new Timestamp(hoursToMillis(hours) + minutesToMillis(minutes));
    }

    public TimeHelper(Timestamp t)
    {time = t;}

    /**
     * @param millis
     * @return True if the time specified by this object comes after the time specified by the
     * provided Date object, false otherwise.
     */
    public boolean after(long millis)
    {
        return time.after(new Timestamp(millis));
    }

    public boolean before(long millis)
    {
        return time.before(new Timestamp(millis));
    }

    /**
     * @return initially specified time with reference to 1/1/1970 00:00:00 GMT
     */
    public long getAbsoluteTime() {
        return time.getTime();
    }

    /**
     * @return Time between the time specified at instantiation and now. Timestamp by default return
     * time elapsed from 1/1/1970 00:00:00 GMT, so we must subtract the current time at moment of
     * query.
     */
    public long getTimeRemaining() {
        return time.getTime() - now();
    }

    public int getHoursRemaining() {
        return millisToHours(getTimeRemaining());
    }

    /**
     * @return total time remaining converted to minutes
     */
    public int getMinutesRemaining() {
        return millisToMinutes(getTimeRemaining());
    }

    /**
     * @return time remaining in minutes once time remaining in hours has been subtracted.
     */
    public int getMinutesRemainingWithoutHours() {
        // Total Time Remaining [ms] - Hours Remaining [ms] = Minutes Remaining [ms]
        long remaining = getTimeRemaining();
        int hours = millisToHours(remaining);
        long hoursInMillis = hoursToMillis(hours);
        remaining -= hoursInMillis;
        return millisToMinutes(remaining);
    }

    /**
     * @param format
     * @return Time remaining in the specified format. Does not return absolute time.
     */
    public String getTimeFormatted(String format) {
        switch (format) {
            case MMDDYYYY:
                return time.getMonth() + "/" + time.getDay() + "/" + time.getYear();
            case HHMM:
                String h = "", m = "";
                int hour = getHoursRemaining(), minute = getMinutesRemainingWithoutHours();
                if (hour < 10)
                    h = "0" + hour;
                else
                    h = "" + hour;

                if (minute < 10)
                    m = "0" + minute;
                else
                    m = "" + minute;
                return h + ":" + m;
            default:
                return null;
        }
    }

    /**
     * @return Time in milliseconds since the Android Epoch of January 1st, 1970 at 00:00:00 GMT.
     */
    public static long now() {
        return Calendar.getInstance().getTime().getTime();
    }

    public static long hoursToMillis(int hours) {
        return hours * 3600000L;
    }

    public static int millisToHours(long millis) {
        return (int) millis / 3600000;
    }

    public static long minutesToMillis(int minutes) {
        return minutes * 60000L;
    }

    public static int millisToMinutes(long millis) {
        return (int) millis / 60000;
    }
}

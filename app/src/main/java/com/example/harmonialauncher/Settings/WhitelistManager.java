package com.example.harmonialauncher.Settings;

import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class WhitelistManager {

    //An AppObject is primarily defined by its unique package, so it is useless to make a hashmap of
    //AppObjects when we can deal directly with the packages they wrap around.
    private static HashMap<String, Boolean> whitelisted = new HashMap<String, Boolean>();

    public static void whitelist(String packageName)
    {whitelisted.put(packageName, true);}

    public static void whitelist(AppObject app)
    {whitelisted.put(app.getPackageName(), true);}

    public static void unWhitelist(String packageName)
    {whitelisted.put(packageName, false);}

    public static void unWhitelist(AppObject app)
    {whitelisted.put(app.getPackageName(), false);}

    public static boolean isWhitelisted(String packageName)
    {
        if (whitelisted.containsKey(packageName) && whitelisted.get(packageName))
            return true;
        return false;
    }

    public static ArrayList<String> getWhitelist()
    {
        ArrayList<String> wl = new ArrayList<String>();
        for (String key : whitelisted.keySet())
        {
            if (whitelisted.get(key))
                wl.add(key);
        }
        return wl;
    }

    private static boolean inMap(String packageName)
    {
        return whitelisted.containsKey(packageName);
    }
}

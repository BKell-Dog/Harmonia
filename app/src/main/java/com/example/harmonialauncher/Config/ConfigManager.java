package com.example.harmonialauncher.Config;

import android.util.Log;

import com.example.harmonialauncher.AppObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConfigManager {

    private static final String TAG = "Config Manager";

    private static Properties p = new Properties();
    private static String filePath = "src/main/java/com/example/harmonialauncher/app_config";
    private static HashMap<String, String> properties = new HashMap<String, String>();

    //List of required properties


    //Mode Constants
    public static final String NORMAL = "Normal", ELDERLY = "Elderly", CHILD = "Child";
    private static String MODE = NORMAL;


    //This class is a helper to the Harmonia-specific settings app which will be a part of the release.
    //It will search for a config file within the local folder, and import the config settings if found.
    //If not found, it will provide a list of default settings through the Properties class.
    //THe user will be able to add preferences through the external app, which will be added to config here.

    //TODO: finish this class

    public static HashMap<String, String> loadProperties() {
        if (checkForConfig()) {
            //Iterate through loaded hash table of key-value pairs and put them into a hash map. Return hash map.
            for (String s : p.stringPropertyNames()) {
                properties.put(s, p.getProperty(s));
            }
            return properties;
        }
        return null;
    }

    public static void writeHomeScreenApps(AppObject[] apps)
    {

    }

    public static void setProperty(String key, String value) {
        p.put(key, value);
    }

    //Method checks if there is a config file, and if so, automatically sends the data to the
    // Properties object p. We can read the data from p elsewhere.
    //If this method returns false, it means that this is the first time the app is being opened.
    public static boolean checkForConfig() {
        File config = new File(filePath);

        try {
            FileReader reader = new FileReader(config);
            p.load(reader);
            return true;
        } catch (FileNotFoundException fnfe) {
            Log.d(TAG, "File not found");
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            Log.d(TAG, "IOException");
            ioe.printStackTrace();
            return false;
        }
    }

    //Write preferences to file
    public void writeToFile() {
        //Check that Properties p contains all the key-value pairs that are necessary for the system
        //TODO: Check properties list and compile a basic list of config keys

        //Write to file
        File configFile = new File(filePath);
        try {
            OutputStream outputStream = new FileOutputStream(configFile);
            p.storeToXML(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setConfigFilePath(String filePath) {
        filePath = filePath;
    }

    public static String getConfigFilePath() {
        return filePath;
    }

    public static String getMode() {
        checkForConfig();
        return p.getProperty(MODE);
    }


}

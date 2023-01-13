package com.example.harmonialauncher.Config;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.HomeScreen.HomeScreenGridAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ConfigManager {
    private static final String TAG = "Config Manager";

    //Variables for writing to file
    private static final String fileName = "app_config";

    //Variables for App Screen organization and ordering of app objects
    private AppObject[] homeScreenApps = new AppObject[HomeScreenGridAdapter.HOMESCREENAPPNUM];
    private ArrayList<AppObject> drawerApps = new ArrayList<>();

    /**
     * This method will write the drawer page app order to a file.
     * @param apps Arraylist of all drawer pages
     */
    public static void writeAppOrderToFile(@NonNull Context context, @NonNull ArrayList<AppObject> apps)
    {
        Log.d(TAG, "REACHED WRITE TO FILE");
        if (apps.size() == 0) {
            Log.d(TAG, "ArrayList is Empty!");
            return;
        }

        String data = "";
        for (AppObject a : apps)
        {
            data += a.getPackageName() + ";";
        }

        Log.d(TAG, "WRITE DATA: " + data);


        if ((new File(fileName)).exists() && false)
        {
            //File exists, extract data first and insert this data in correct position.
        }
        else
        {
            Log.d(TAG, "REACHED INNER TRY BLOCK");
            //File does not exist, create new file from scratch
            try {
                FileOutputStream fileOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
                outputWriter.write(data);
                outputWriter.close();
                Log.d(TAG, "FILE WRITTEN SUCCESSFULLY");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thi smethod will write the home screen page app order to a file.
     * @param apps Array of all homescreen apps.
     */
    public static void writeAppOrderToFile(AppObject[] apps)
    {

    }

    /**
     * This method will read drawer page app order from a file and return an arraylist of app
     * objects in that order.
     * @return Arraylist of AppObjects in order of appearance in file.
     */
    public static ArrayList<AppObject> readAppOrderFromFile()
    {
        return null;
    }

    /**
     * This method will read home screen page app order from a file and return an array of app objects
     * in that order.
     * @return Array of AppObjects in order
     */
    public static AppObject[] readAppOrderFromFiles()
    {
        return null;
    }

}





/*

import android.content.Context;
import android.util.Log;

import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.HomeScreen.HomeScreenGridAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

/**
 * This class will read and write configuration data to and from a config file.
 * As of now, this file stores data relating to: app ordering on homescreen and drawerscreen;
 */
/*
public class ConfigManager {

    private static Properties p = new Properties();
    private static String filePath = "src/main/java/com/example/harmonialauncher/app_config";
    public final String fileName = "app_config";
    private static HashMap<String, String> properties = new HashMap<String, String>();

    //Variables for App Screen organization and ordering of app objects
    private AppObject[] homeScreenApps = new AppObject[HomeScreenGridAdapter.HOMESCREENAPPNUM];
    private ArrayList<AppObject> drawerApps = new ArrayList<>();

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

    public void readPropertiesFromConfig(Context context)
    {
        try {
            //Find config file and access its contents as String
            File config = new File(context.getFilesDir(), fileName);
            Log.d(TAG, "file output: " + config.toString());
            Scanner scan = new Scanner(config);
            scan.useDelimiter("$");
            while(scan.hasNext())
            {
                String scanned = scan.next();
                Scanner scan2 = new Scanner(scanned);
                scan2.useDelimiter("#");
                while (scan2.hasNext())
                {

                }
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FILE NOT FOUND");
            e.printStackTrace();
        }

        //Parse String using Scanner. For each data segment add data to local variables


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
    private void writeToFile(Context context, String contents) {
        //Check that Properties p contains all the key-value pairs that are necessary for the system
        //TODO: Check properties list and compile a basic list of config keys

        File file = new File(context.getFilesDir(), fileName);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(contents.getBytes());
        }
        catch (IOException ioe) {ioe.printStackTrace();}




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

    public class ConfigObject
    {
        private String categoryName, contents;

        public ConfigObject(String categoryName, String contents)
        {
            this.categoryName = categoryName;
            this.contents = contents;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }
    }


}
*/
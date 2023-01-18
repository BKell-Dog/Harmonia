package com.example.harmonialauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

public class PackageLoader {
    private final static String TAG = "package Loader";
    //Hard-coded preset packages for common android environments
    private final static ArrayList<String> presetDialer = new ArrayList<String>();
    private final static ArrayList<String> presetSMS = new ArrayList<String>();
    private final static ArrayList<String> presetCamera = new ArrayList<String>();
    private final static ArrayList<String> presetGallery = new ArrayList<String>();
    private final static ArrayList<String> presetEmail = new ArrayList<String>();
    private final static ArrayList<String> presetContacts = new ArrayList<String>();
    private final static ArrayList<String> presetSettings = new ArrayList<String>();
    private final static ArrayList<String> presetFiles = new ArrayList<String>();
    private final static ArrayList<String> presetCalendar = new ArrayList<String>();
    //TODO: Fix or generalize filepath to not involve my personal username
    private final String filePath = "C:/Users/nothi/AndroidStudioProjects/HarmoniaLauncher/app/src/main/java/com/example/harmonialauncher/package_config";
    private Context CONTEXT;

    public PackageLoader(Context context) {
        CONTEXT = context;
        setPackagePresets();
    }

    public ArrayList<String> getPresetDialerPackage() {
        if (presetDialer.size() <= 0) {
            setPackagePresets();
        }
        return presetDialer;
    }

    public ArrayList<String> getPresetSMSPackage() {
        if (presetSMS.size() <= 0) {
            setPackagePresets();
        }
        return presetSMS;
    }

    public ArrayList<String> getPresetCameraPackage() {
        if (presetCamera.size() <= 0) {
            setPackagePresets();
        }
        return presetCamera;
    }

    public ArrayList<String> getPresetGalleryPackage() {
        if (presetGallery.size() <= 0) {
            setPackagePresets();
        }
        return presetGallery;
    }

    public ArrayList<String> getPresetEmailPackage() {
        if (presetEmail.size() <= 0) {
            setPackagePresets();
        }
        return presetEmail;
    }

    public ArrayList<String> getPresetContactsPackage() {
        if (presetContacts.size() <= 0) {
            setPackagePresets();
        }
        return presetContacts;
    }

    public ArrayList<String> getPresetFilesPackage() {
        if (presetFiles.size() <= 0)
            setPackagePresets();
        return presetFiles;
    }

    public ArrayList<String> getPresetCalendarPackage() {
        if (presetCalendar.size() <= 0)
            setPackagePresets();
        return presetCalendar;
    }

    public String getDefaultDialerPackage() {
        //NOTE: In the android docs, the phone or calling app is called a DIALER.
        PackageManager packman = CONTEXT.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        return intent.resolveActivity(packman).getPackageName();
    }

    public String getDefaultSMSPackage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            return Telephony.Sms.getDefaultSmsPackage(CONTEXT);
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            PackageManager packman = CONTEXT.getPackageManager();
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("vnd.android-dir/mms-sms");
            return intent.resolveActivity(packman).getPackageName();
        }
    }

    public String getDefaultCameraPackage() {
        PackageManager packman = CONTEXT.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent.resolveActivity(packman).getPackageName();
    }

    public String getDefaultGalleryPackage() {
        PackageManager packman = CONTEXT.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        String g = intent.resolveActivity(packman).getPackageName();
        if (g.equalsIgnoreCase("android"))
            return "";
        return g;
    }

    //TODO: Fix this method. It doesn't work
    public String getDefaultEmailPackage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        PackageManager packman = CONTEXT.getPackageManager();
        List<ResolveInfo> activities = packman.queryIntentActivities(intent, 0);
        return "com.google.android.gm";
        //return intent.resolveActivity(packman).getPackageName();
    }

    public String getDefaultContactsPackage() {
        return "com.android.contacts";
    }

    public String getDefaultSettingsPackage() {
        PackageManager packman = CONTEXT.getPackageManager();
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        return intent.resolveActivity(packman).getPackageName();
    }

    public String getDefaultFilesPackage() {
        PackageManager packman = CONTEXT.getPackageManager();
        return "com.google.android.documentsui";
    }

    public String getDefaultCalendarPackage() {
        return "com.google.android.calendar";
    }

    private void setPackagePresets() {
        presetDialer.add("com.samsung.android.dialer");
        presetDialer.add("com..google.android.dialer");

        presetSMS.add("ort.thoughtcrime.securesms");
        presetSMS.add("com.google.android.apps.messaging");

        presetCamera.add("com.sec.android.app.camera");
        presetCamera.add("com.android.camera2");

        presetGallery.add("com.sec.android.gallery3d");
        presetGallery.add("com.google.android.apps.photos");

        //TODO: get package names for other popular email apps.
        presetEmail.add("com.samsung.android.email.provider");
        presetEmail.add("com.google.android.gm");
        presetEmail.add("ch.protonmail.android");

        presetContacts.add("com.samsung.android.app.contacts");
        presetContacts.add("com.android.contacts");

        presetSettings.add("com.android.settings");

        presetFiles.add("com.google.android.documentsui");
        presetFiles.add("com.sec.android.app.myfiles");

        presetCalendar.add("com.google.android.calendar");
    }

    public ArrayList<String> getInstalledPacks() {
        List<PackageInfo> packages = CONTEXT.getPackageManager().getInstalledPackages(0);
        ArrayList<String> packs = new ArrayList<String>();
        for (PackageInfo p : packages)
            packs.add(p.packageName);
        return packs;
    }
}

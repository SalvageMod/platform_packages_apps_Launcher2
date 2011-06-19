/*
 * Copyright (C) 2011 The SalvageMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher2;

import java.util.List;

import com.android.launcher2.utils.PackageUtils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.IPackageDataObserver;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.app.Activity;
import java.lang.Boolean;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceCategory;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

import android.widget.TextView;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActivityManager;

import com.android.launcher.R;

public class LauncherPreferenceActivity extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LAUNCHER_HIDE_LABELS = "pref_key_launcher_hide_labels";
    public static final String LAUNCHER_SHOW_SHORTCUTS_LABEL = "pref_key_launcher_show_shortcuts_label";
    public static final String LAUNCHER_FULLSCREEN = "pref_key_launcher_fullscreen";
    public static final String LAUNCHER_WALLPAPER_SCROLLING = "pref_key_launcher_wallpaper_scrolling";
    public static final String LAUNCHER_ORIENTATION = "pref_key_launcher_orientation";
    
    private static final String TAG = "Launcher2";
    private boolean DBG = true;

	private ListPreference mScreenPreference;

	private static boolean mIsScreenChangerOn = false;

	CheckBoxPreference mScreenCheckBox;

    private ActivityManager activityManager;

    private String[] mAppNames;

    private static final String THREE = "Three";
    private static final String FIVE = "Five";
    private static final String SEVEN = "Seven";
    private static final String SCREENSETTINGS = "NUM_SCREENS";

    private static final String LAUNCHER = "com.android.launcher";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setPreferences();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

   public void setPreferences(){
	mScreenCheckBox = (CheckBoxPreference) findPreference("screen_changer");
	mScreenCheckBox.setChecked(mIsScreenChangerOn);
	mScreenPreference = (ListPreference) findPreference("num_screens");
	activityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);

	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	    boolean value;

	    if (preference == mScreenCheckBox && mIsScreenChangerOn == false) {
			// Ask the user if they are sure they whant to proceed/
	    	// If they do let them know that the homescreen widgets/apps 
	    	// will be reset
	    	alertbox("Warning", "Selecting a new homescreen configuration will remove the current configuration. " +
	    			"If you do not wish to set up your homescreen again, don't change the configuration.");

	    	mIsScreenChangerOn = true;

		    }
	    else if (preference == mScreenCheckBox && mIsScreenChangerOn == true){

	    	mIsScreenChangerOn = false;

	    }

	    return true;
	}

	//brough to you buy http://www.androidsnippets.com/display-an-alert-box

	protected void alertbox(String title, String mymessage)
	   {
	   new AlertDialog.Builder(this)
	      .setMessage(mymessage)
	      .setTitle(title)
	      .setCancelable(true)
	      .setNeutralButton("OK",
	         new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton){}
	         })
	      .show();
	   }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        	if(DBG)Log.v(TAG, "shared preference changed");    	
        	
	        if(key == mScreenPreference.getKey()){
	        if(DBG)Log.v(TAG, "on shared screen preference change in Launcher2");
	        	registerScreenChange(mScreenPreference.getEntry().toString());
	        	PackageUtils packageUtility = new PackageUtils(this , LAUNCHER);
	        	packageUtility.initiateClearUserData();
	        }
        }
        

        
         void registerScreenChange(String st){
         	if(compareStrings(st.compareTo(SEVEN))) {
         		st.compareTo(SEVEN);
         	  Settings.System.putInt(getContentResolver(), SCREENSETTINGS, 7);
         	  Log.i(TAG, "The number of screens to register is " + st);
         	}
         	if (compareStrings(st.compareTo(FIVE))) {
         	  Settings.System.putInt(getContentResolver(), SCREENSETTINGS, 5);
           	  Log.i(TAG, "The number of screens to register is " + st);
         	}
         	if (compareStrings(st.compareTo(THREE))) {
               Settings.System.putInt(getContentResolver(), SCREENSETTINGS, 3);
           	  Log.i(TAG, "The number of screens to register is " + st);
         	}
         }

         @Override
         protected void onResume() {
             super.onResume();
             // Set up a listener whenever a key changes
             getPreferenceScreen().getSharedPreferences()
                     .registerOnSharedPreferenceChangeListener(this);
         }

         @Override
         protected void onPause() {
             super.onPause();
             // Unregister the listener whenever a key changes
             getPreferenceScreen().getSharedPreferences()
                     .unregisterOnSharedPreferenceChangeListener(this);
         }
         
         boolean compareStrings(int i){
        		if (i == 0)
        			return true;
        		else return false;
        	}

         void toastMsg(String msg){
         	Context context = getApplicationContext();
          	int duration = Toast.LENGTH_SHORT;
         	Toast toast = Toast.makeText(context, msg, duration);
         	toast.show();
         }
         
         public void restartLauncher2(ActivityManager activity) {
     		if(DBG)
     			Log.d(TAG, "About to kill the launcher application");
     	 	activity.killBackgroundProcesses(LAUNCHER);	
         }
}

package com.android.launcher2;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.launcher.R;

public final class SalvageLauncherHelper {
	private static final String SALVAGEMOD_PREFERENCES = "launcher.salvagemod.preferences";
	private static final String[] restart_keys={"systemPersistent"};
	public static boolean needsRestart(String key){
		for(int i=0;i<restart_keys.length;i++){
			if(restart_keys[i].equals(key))
				return true;
		}
		return false;
	}
	public static boolean getSystemPersistent(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SALVAGEMOD_PREFERENCES, Context.MODE_PRIVATE);
		boolean newD = sp.getBoolean("systemPersistent", context.getResources().getBoolean(R.bool.config_system_persistent));
		return newD;
	}
	
}


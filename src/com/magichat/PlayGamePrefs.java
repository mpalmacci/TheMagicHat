package com.magichat;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PlayGamePrefs extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.play_game_prefs);
	}
}
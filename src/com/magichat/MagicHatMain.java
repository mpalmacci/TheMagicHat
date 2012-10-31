package com.magichat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MagicHatMain extends Activity implements View.OnClickListener {

	Button bPlayGame, bViewGameStats, bAddDeck, bUpdateDeck, bChangeActive, bDeleteDeck,
			bDisplayAllDecks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.magic_hat_main);

		initialize();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bEnterGame:
			Intent openGameActivity = new Intent("com.magichat.PLAYGAME");
			startActivity(openGameActivity);
			break;
		case R.id.bViewGameStats:
			Intent openGameStatsActivity = new Intent("com.magichat.GAMESTATS");
			startActivity(openGameStatsActivity);
			break;
		case R.id.bAddDeck:
			Intent openAddDeckActivity = new Intent("com.magichat.ADDDECK");
			startActivity(openAddDeckActivity);
			break;
		case R.id.bUpdateDeck:
			Intent openUpdateDeckActivity = new Intent(
					"com.magichat.UPDATEDECK");
			startActivity(openUpdateDeckActivity);
			break;
		case R.id.bChangeActive:
			Intent openChangeActiveActivity = new Intent(
					"com.magichat.CHANGEACTIVE");
			startActivity(openChangeActiveActivity);
			break;
		case R.id.bDeleteDeck:
			Intent openDeleteDeckActivity = new Intent(
					"com.magichat.DELETEDECK");
			startActivity(openDeleteDeckActivity);
			break;
		case R.id.bDisplayAllDecks:
			Intent openDisplayAllDecksActivity = new Intent(
					"com.magichat.DISPLAYALLDECKS");
			startActivity(openDisplayAllDecksActivity);
			break;
		}
	}

	private void initialize() {
		bPlayGame = (Button) findViewById(R.id.bEnterGame);
		bViewGameStats = (Button) findViewById(R.id.bViewGameStats);
		bAddDeck = (Button) findViewById(R.id.bAddDeck);
		bUpdateDeck = (Button) findViewById(R.id.bUpdateDeck);
		bDeleteDeck = (Button) findViewById(R.id.bDeleteDeck);
		bChangeActive = (Button) findViewById(R.id.bChangeActive);
		bDisplayAllDecks = (Button) findViewById(R.id.bDisplayAllDecks);

		bPlayGame.setOnClickListener(this);
		bViewGameStats.setOnClickListener(this);
		bAddDeck.setOnClickListener(this);
		bUpdateDeck.setOnClickListener(this);
		bChangeActive.setOnClickListener(this);
		bDeleteDeck.setOnClickListener(this);
		bDisplayAllDecks.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.playGamePrefs:
			Intent playGamePrefs = new Intent("com.magichat.PLAYGAMEPREFS");
			startActivity(playGamePrefs);
			break;
		case R.id.gameStatsPrefs:
			Intent gameStatsPrefs = new Intent("com.magichat.GAMESTATSPREFS");
			startActivity(gameStatsPrefs);
			break;
		}
		return false;
	}
}
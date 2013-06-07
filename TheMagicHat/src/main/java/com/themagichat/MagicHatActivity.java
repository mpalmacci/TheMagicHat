package com.themagichat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MagicHatActivity extends Activity implements View.OnClickListener {
	protected Button bAdd, bCheck, bEdit, bBack, bDelete, bPrefs, bPlayers, bCardSearch;

	protected TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_view);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title_bar);

		initialize();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bBack:
			this.finish();
			break;
		case R.id.bPrefs:
			Intent playGamePrefs = new Intent("com.themagichat.MAGICHATPREFS");
			startActivity(playGamePrefs);
			break;
		case R.id.bPlayers:
			Intent openPlayersActivity = new Intent(
					"com.themagichat.players.PLAYERSMAIN");
			startActivity(openPlayersActivity);
			break;
		case R.id.bCardSearch:
			Intent openCardSearchActivity = new Intent(
					"com.themagichat.cards.CARDSEARCH");
			startActivity(openCardSearchActivity);
			break;
		default:
			break;
		}
	}

	private void initialize() {
		bAdd = (Button) findViewById(R.id.bAdd);
		bCheck = (Button) findViewById(R.id.bCheck);
		bEdit = (Button) findViewById(R.id.bEdit);
		bBack = (Button) findViewById(R.id.bBack);
		bDelete = (Button) findViewById(R.id.bDelete);
		bPrefs = (Button) findViewById(R.id.bPrefs);
		bPlayers = (Button) findViewById(R.id.bPlayers);
		bCardSearch = (Button) findViewById(R.id.bCardSearch);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		bBack.setOnClickListener(this);
		bPrefs.setOnClickListener(this);
		bPlayers.setOnClickListener(this);
		bCardSearch.setOnClickListener(this);
	}
}

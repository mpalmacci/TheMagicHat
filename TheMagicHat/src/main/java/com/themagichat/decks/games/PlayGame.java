package com.themagichat.decks.games;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.themagichat.MagicHatActivity;
import com.themagichat.R;
import com.themagichat.decks.Deck;
import com.themagichat.decks.db.MagicHatDb;
import com.themagichat.players.Player;

import java.util.*;

public class PlayGame extends MagicHatActivity implements
		OnItemSelectedListener {
	private List<Deck> allActiveDecks = new ArrayList<Deck>();
	private List<Player> players = new ArrayList<Player>();
	private Map<Player, Deck> playersAndDecks = new HashMap<Player, Deck>();
    private TextView tvPlayer1, tvPlayer2;

	private int p1GameCount = 0, p2GameCount = 0;

	private Button bPlayGame, bPlayer1, bPlayer2;
	private LinearLayout llWinnerSection, llMatchupView;

	private boolean ownDecks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_game);

		initialize();

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ownDecks = getPrefs.getBoolean("ownersDecksOnly", false);

		// TODO Break this out into two separate code paths
		new getAllInfo().execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean ownDecksNew = getPrefs.getBoolean("ownersDecksOnly", false);

		if (ownDecksNew != ownDecks) {
			ownDecks = ownDecksNew;

			// TODO Break this out into two separate code paths
			new getAllInfo().execute();
		}
	}

	private class getAllInfo extends AsyncTask<Boolean, Integer, String> {

		@Override
		protected String doInBackground(Boolean... prefs) {
			MagicHatDb getAllInfoDB = new MagicHatDb(PlayGame.this);
			getAllInfoDB.openReadableDB();
			players = getAllInfoDB.getActivePlayers();

			if (ownDecks) {
				// Here every player will play with their own decks
				for (Player p : players) {
					List<Deck> playersDecks = getAllInfoDB.getDeckList(p, true);
					if (playersDecks.isEmpty()) {
						System.out.println("PlayGame.getAllInfo: "
								+ p.toString() + "'s deckList is empty!");
					}
					p.setDeckList(playersDecks);
				}
			} else {
				allActiveDecks = getAllInfoDB.getAllDecks(true);
			}

			getAllInfoDB.closeDB();

			return null;
		}

		@Override
		protected void onPostExecute(String results) {
			super.onPostExecute(results);
			populateDeckSpinners();
			displayNewGame();
		}
	}

	private void populateDeckSpinners() {
		// This line removes the previously created views in case this is
		// called after onResume
		llMatchupView.removeAllViews();

		for (Player p : players) {
			List<Deck> deckList;

			TextView tvPlayer = new TextView(PlayGame.this);
			Spinner sPlayersDecks = new Spinner(PlayGame.this);

			tvPlayer.setText("\n" + p.toString() + " will play:");

			if (allActiveDecks.isEmpty()) {
				deckList = p.getDeckList();
			} else {
				deckList = allActiveDecks;
			}

			ArrayAdapter<Deck> deckAdapter = new ArrayAdapter<Deck>(
					PlayGame.this, R.layout.mh_spinner, deckList);

			deckAdapter.setDropDownViewResource(R.layout.mh_spinner_dropdown);

			sPlayersDecks.setAdapter(deckAdapter);

			tvPlayer.setTextSize(25);
			sPlayersDecks.setId(p.getId());
			sPlayersDecks.setPrompt("Select a Deck to Play With");
			sPlayersDecks.setOnItemSelectedListener(PlayGame.this);

			llMatchupView.addView(tvPlayer);
			llMatchupView.addView(sPlayersDecks);
		}
	}

	private void displayNewGame() {
		// TODO Add in handling in case the player doesn't have any active decks
		if (players.size() == 0) {
			bPlayGame.setEnabled(false);
			TextView tvErrorMessage = new TextView(PlayGame.this);
			tvErrorMessage.setText("\n\nNo active Players were found!\n\n");
			llMatchupView.addView(tvErrorMessage);
			return;
		}

		getNewRandomGame();
		bPlayGame.setEnabled(true);

		if (playersAndDecks.size() != players.size()) {
			bPlayGame.setEnabled(false);
			TextView tvErrorMessage = new TextView(PlayGame.this);
			tvErrorMessage
					.setText("Players and Decks are not of equal size\n\nPlayers size is "
							+ players.size()
							+ " and the Decks size is "
							+ playersAndDecks.size());
			llMatchupView.addView(tvErrorMessage);
			return;
		}

		if (players.size() == 2) {
			llWinnerSection.setVisibility(LinearLayout.VISIBLE);
			bPlayer1.setText(players.get(0).getName());
			bPlayer2.setText(players.get(1).getName());
		}

		if (allActiveDecks.isEmpty()) {
			for (Player p : players) {
				int pId = p.getId();
				Spinner sPlayersDeck = (Spinner) findViewById(pId);

				sPlayersDeck.setSelection(p.getDeckList().indexOf(
						playersAndDecks.get(p)));
			}
		} else {
			for (Player p : players) {
				int pId = p.getId();
				Spinner sPlayersDeck = (Spinner) findViewById(pId);

				sPlayersDeck.setSelection(allActiveDecks
						.indexOf(playersAndDecks.get(p)));
			}
		}
	}

	private void getNewRandomGame() {
		// Clear the cache of gameDecks, and start anew
		playersAndDecks = new HashMap<Player, Deck>();
		p1GameCount = 0;
		p2GameCount = 0;

		Random ran = new Random();

		int maxVal = 0;
		int r = 0;

		// TODO Allow user to choose who they are - always set them as Player 1
		if (allActiveDecks.isEmpty()) {
			for (Player p : players) {
				maxVal = p.getDeckList().size();
				r = ran.nextInt(maxVal);
				playersAndDecks.put(p, p.getDeckList().get(r));
			}
		} else {
			List<Integer> randomInts = new ArrayList<Integer>();
			maxVal = allActiveDecks.size();
			for (Player p : players) {
				r = ran.nextInt(maxVal);
				while (randomInts.contains(r)) {
					r = ran.nextInt(maxVal);
				}
				randomInts.add(r);
				playersAndDecks.put(p, allActiveDecks.get(r));
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bPlayGame:
			displayNewGame();
			break;
		case R.id.bPlayer1:
			++p1GameCount;
            tvPlayer1.setText(Integer.toString(p1GameCount));
			new addGameResult().execute(0);
			break;
		case R.id.bPlayer2:
			++p2GameCount;
            tvPlayer2.setText(Integer.toString(p2GameCount));
			new addGameResult().execute(1);
			break;
		default:
			break;
		}
	}

	private class addGameResult extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... pNums) {
			Game g = new Game(playersAndDecks, new Date());
			g.setWinner(players.get(pNums[0]));

			MagicHatDb mhAddGameResult = new MagicHatDb(PlayGame.this);
			mhAddGameResult.openWritableDB();
			mhAddGameResult.writeNewGame(g);
			mhAddGameResult.closeDB();

			return pNums[0];
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 0) {
				if (p1GameCount + p2GameCount > 1) {
					if (p1GameCount == 2) {
						showCompletedDialog();
					}
				} else {
					showWinnerDialog();
				}
			} else if (result == 1) {
				if (p1GameCount + p2GameCount > 1) {
					if (p2GameCount == 2) {
						showCompletedDialog();
					}
				} else {
					showWinnerDialog();
				}
			}
		}
	}

	private void showWinnerDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage(
				"Congrats to the Winner!\nWould you like to play for best of 3?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
                        tvPlayer1.setText("0");
                        tvPlayer2.setText("0");
						displayNewGame();
						dialog.dismiss();
					}
				});
		AlertDialog ad = adb.create();
		ad.show();
	}

	private void showCompletedDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setMessage(
				"Congrats to the Winner!\nWould you like to play more games?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
                                tvPlayer1.setText("0");
                                tvPlayer2.setText("0");
								displayNewGame();
								dialog.dismiss();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		AlertDialog ad = adb.create();
		ad.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		playersAndDecks.clear();

		Spinner sPlayersDeck;

		for (Player p : players) {
			sPlayersDeck = (Spinner) findViewById(p.getId());
			Deck d = (Deck) sPlayersDeck.getSelectedItem();
			playersAndDecks.put(p, d);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	private void initialize() {
		bPlayGame = (Button) findViewById(R.id.bPlayGame);
		bPlayer1 = (Button) findViewById(R.id.bPlayer1);
		bPlayer2 = (Button) findViewById(R.id.bPlayer2);
		llMatchupView = (LinearLayout) findViewById(R.id.llMatchupView);
		llWinnerSection = (LinearLayout) findViewById(R.id.llWinnerSection);
        tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView) findViewById(R.id.tvPlayer2);

		this.bCardSearch.setVisibility(LinearLayout.VISIBLE);
		this.bPrefs.setVisibility(LinearLayout.VISIBLE);

		this.tvTitle.setText("Let's Play Some Magic!");

		bPlayGame.setOnClickListener(this);
		bPlayer1.setOnClickListener(this);
		bPlayer2.setOnClickListener(this);
	}
}

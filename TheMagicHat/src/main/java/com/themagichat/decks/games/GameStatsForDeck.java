package com.themagichat.decks.games;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.themagichat.MagicHatActivity;
import com.themagichat.R;
import com.themagichat.decks.Deck;
import com.themagichat.decks.db.MagicHatDb;
import com.themagichat.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameStatsForDeck extends MagicHatActivity implements
		OnItemSelectedListener {

	float totalGames, totalGamesWon, totalGamesLost = 0;

	List<Player> allPlayers = new ArrayList<Player>();
	List<Deck> dOpponents = new ArrayList<Deck>();
	List<Deck> playersDecks = new ArrayList<Deck>();

	LinearLayout llByDecks, llWonLostStats, llNonePlayed;
	Spinner sDeckSelection;
	TextView tvTotalGames, tvGamesWon, tvGamesLost, tvEmptyList;
	ProgressBar pbTotalWonLost;

	Deck currentDeck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_stats_for_deck);

		initialize();

		new populateDeckSpinner().execute();
	}

	private class populateDeckSpinner extends
			AsyncTask<String, Integer, List<Deck>> {

		@Override
		protected List<Deck> doInBackground(String... params) {
			List<Deck> allDecks = new ArrayList<Deck>();
			MagicHatDb mhDB = new MagicHatDb(GameStatsForDeck.this);
			mhDB.openReadableDB();
			allDecks = mhDB.getAllDecks(false);
			mhDB.closeDB();
			return allDecks;
		}

		@Override
		protected void onPostExecute(List<Deck> allDecks) {
			super.onPostExecute(allDecks);

			if (allDecks.isEmpty()) {
				llByDecks.setVisibility(LinearLayout.GONE);
				sDeckSelection.setVisibility(LinearLayout.GONE);
				llNonePlayed.setVisibility(LinearLayout.VISIBLE);
				tvEmptyList.setText("There are No Decks in the Database!");
			} else {
				ArrayAdapter<Deck> deckAdapter = new ArrayAdapter<Deck>(
						GameStatsForDeck.this,
						R.layout.mh_spinner, allDecks);
				deckAdapter.setDropDownViewResource(R.layout.mh_spinner_dropdown);
				sDeckSelection.setAdapter(deckAdapter);

				currentDeck = (Deck) sDeckSelection.getSelectedItem();
			}
		}
	}

	private class populateScreen extends AsyncTask<String, Integer, String> {
		List<Game> games = new ArrayList<Game>();

		@Override
		protected String doInBackground(String... params) {
			MagicHatDb mhDB = new MagicHatDb(GameStatsForDeck.this);
			mhDB.openReadableDB();
			games = mhDB.getGames(currentDeck);
			mhDB.closeDB();

			getStats(games);

			return null;
		}

		@Override
		protected void onPostExecute(String results) {
			super.onPostExecute(results);
			if (games.isEmpty()) {
				llByDecks.setVisibility(LinearLayout.GONE);
				llNonePlayed.setVisibility(LinearLayout.VISIBLE);
			} else {
				tvTotalGames.setText(Integer.toString(games.size()));
				tvGamesWon.setText(Integer.toString(Math.round(totalGamesWon)));
				int iPercentage = Math
						.round(totalGamesWon / games.size() * 100);
				pbTotalWonLost.setProgress(iPercentage);
				tvGamesLost
						.setText(Integer.toString(Math.round(totalGamesLost)));

				// Get a list of games based on your opponents and populate the
				// Game Set Results in a separate method
				for (Deck dOpp : dOpponents) {
					List<Game> gameList = new ArrayList<Game>();
					for (Game g : games) {
						if (g.getDeck(0).equals(dOpp)
								|| g.getDeck(1).equals(dOpp)) {
							gameList.add(g);
						}
					}
					populateGamesForDecks(gameList);

				}

				llByDecks.setVisibility(LinearLayout.VISIBLE);
			}
		}
	}

	private void populateGamesForDecks(List<Game> gameList) {
		List<Game> lGamesWon = new ArrayList<Game>();
		TextView tvTotalGamesAgainstOpp = new TextView(GameStatsForDeck.this);
		ProgressBar pbGames = new ProgressBar(GameStatsForDeck.this, null,
				android.R.attr.progressBarStyleHorizontal);
		TextView tvPercentWonAgainstOpp = new TextView(GameStatsForDeck.this);
		Deck dOpp = new Deck();
		Player p = new Player();
		String sTotalGamesAgainstOpp, sPercentWonAgainstOpp;

		if (gameList.get(0).getDeck(0).equals(currentDeck)) {
			dOpp = gameList.get(0).getDeck(1);
			p = gameList.get(0).getPlayer(0);
		} else {
			dOpp = gameList.get(0).getDeck(0);
			p = gameList.get(0).getPlayer(1);
		}

		sTotalGamesAgainstOpp = dOpp.toString();

		for (Game g : gameList) {
			if (g.isWinner(p)) {
				lGamesWon.add(g);
			}
		}

		float fGamesWon = lGamesWon.size();
		int iPercentage = Math.round(fGamesWon / gameList.size() * 100);

		pbGames.setMax(100);
		pbGames.setProgress(iPercentage);

		sPercentWonAgainstOpp = Integer.toString(iPercentage) + "% - Won "
				+ lGamesWon.size() + " out of " + gameList.size() + " games";

		tvTotalGamesAgainstOpp.setText(sTotalGamesAgainstOpp);
		tvPercentWonAgainstOpp.setText(sPercentWonAgainstOpp);
		llWonLostStats.addView(tvTotalGamesAgainstOpp);
		llWonLostStats.addView(pbGames);
		llWonLostStats.addView(tvPercentWonAgainstOpp);
	}

	private void getStats(List<Game> games) {
		Player p = new Player();
		List<Game> gamesWon = new ArrayList<Game>(), gamesLost = new ArrayList<Game>();

		for (Game g : games) {
			// Only add the Deck if opponents doesn't consist of the Deck yet
			if (g.getDeck(0).equals(currentDeck)) {
				// Deck owner != Player of that deck in all situations
				p = g.getPlayer(0);

				if (!dOpponents.contains(g.getDeck(1))) {
					dOpponents.add(g.getDeck(1));
				}
			} else if (!dOpponents.contains(g.getDeck(0))) {
				// Deck owner != Player of that deck in all situations
				p = g.getPlayer(1);

				dOpponents.add(g.getDeck(0));
			}

			if (g.isWinner(p)) {
				gamesWon.add(g);
			} else {
				gamesLost.add(g);
			}
		}
		Collections.sort(dOpponents);

		totalGamesWon = gamesWon.size();
		totalGamesLost = gamesLost.size();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		llWonLostStats.removeAllViews();
		dOpponents = new ArrayList<Deck>();
		playersDecks = new ArrayList<Deck>();
		llNonePlayed.setVisibility(LinearLayout.GONE);

		currentDeck = (Deck) sDeckSelection.getSelectedItem();
		new populateScreen().execute();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	private void initialize() {
		llNonePlayed = (LinearLayout) findViewById(R.id.llNonePlayed);
		llByDecks = (LinearLayout) findViewById(R.id.llByDecks);
		sDeckSelection = (Spinner) findViewById(R.id.sCurrentSelection);
		tvEmptyList = (TextView) findViewById(R.id.tvEmptyList);

		llWonLostStats = (LinearLayout) findViewById(R.id.llWonLostStatsD);
		tvTotalGames = (TextView) findViewById(R.id.tvTotalGamesD);
		tvGamesWon = (TextView) findViewById(R.id.tvGamesWonD);
		tvGamesLost = (TextView) findViewById(R.id.tvGamesLostD);
		pbTotalWonLost = (ProgressBar) findViewById(R.id.pbTotalWonLostD);

		this.bCardSearch.setVisibility(LinearLayout.VISIBLE);

		this.tvTitle.setText("Game Stats for Decks");

		sDeckSelection.setOnItemSelectedListener(this);
	}
}

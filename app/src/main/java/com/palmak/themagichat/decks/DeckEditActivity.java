package com.palmak.themagichat.decks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.palmak.themagichat.R;
import com.palmak.themagichat.common.activities.TMHActivityBase;
import com.palmak.themagichat.players.Player;

/**
 * Created by Matthew on 12/6/2014.
 */
public class DeckEditActivity extends TMHActivityBase {
    Deck currentDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_edit);

        Bundle bName = getIntent().getExtras();
        Long dId = bName.getLong("deckId");

        initialize();

        if (dId == Long.valueOf(0)) {
            currentDeck = new Deck();
            this.setTitle("Create a New Deck");
        } else {
            currentDeck = new Deck("New Deck", new Player("Matty P"), true);
            // This will only flicker quickly while the background task runs
            //.setText(currentDeck.getName());
            //new PopulateDeck().execute(dId);
            this.setTitle(currentDeck.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deck_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deck_save:
                return true;
            case R.id.action_deck_cancel:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {

    }
}

package com.palmak.themagichat.decks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.palmak.themagichat.R;
import com.palmak.themagichat.common.activities.TMHActivityBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 12/4/2014.
 */
public class DeckMainViewActivity extends TMHActivityBase {
    private boolean isDualPane;
    private int activeDeckId = 0;

    private FrameLayout fragmentContainer;

    private RecyclerView rvDeckList;
    private DeckListRecycleViewAdapter mDeckListRecycleViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Deck> deckList = new ArrayList<Deck>();
    private MenuItem actionEditDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deck_main);

        initialize();

        // set flag here to have onOptionsMenu called for fragments
        if (savedInstanceState != null) {
            return;
        }

        // TODO try this to see if it is only needed during onResume
        //refreshDeckListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_deck:
                Intent deckEditIntent = new Intent("com.palmak.themagichat.decks.DECKEDITACTIVITY");
                Bundle deckId = new Bundle();
                deckId.putLong("deckId", activeDeckId);
                deckEditIntent.putExtras(deckId);
                startActivity(deckEditIntent);
                return true;
            case R.id.action_deck_list_filter:
                return true;
            case R.id.action_deck_list_sort:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Either here or onRestart() I should refresh the view to make sure any Deck changes are reflected

        refreshDeckListView();
    }

    private void refreshDeckListView() {
        if (fragmentContainer != null) {
            DeckListFragment deckListFragment = new DeckListFragment();

            deckListFragment.setArguments(getIntent().getExtras());

            deckListFragment.setHasOptionsMenu(true);

            getFragmentManager().beginTransaction()
                    .add(R.id.deck_main_fragment_container, deckListFragment).commit();
        }
    }

    private void initialize() {
        rvDeckList = (RecyclerView) findViewById(R.id.rvDeckList);
        fragmentContainer = (FrameLayout) findViewById(R.id.deck_main_fragment_container);
    }
}

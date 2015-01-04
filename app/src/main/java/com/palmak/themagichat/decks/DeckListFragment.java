package com.palmak.themagichat.decks;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.palmak.themagichat.R;
import com.palmak.themagichat.common.logger.Log;
import com.palmak.themagichat.common.view.DividerItemDecoration;
import com.palmak.themagichat.players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 12/2/2014.
 */
public class DeckListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DeckListFragment";

    private boolean isDualPane;
    private int activeDeckId = 0;

    private List<Deck> deckList = new ArrayList<Deck>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DeckListRecycleViewAdapter mAdapter;
    private ImageButton actionAddDeck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Player p = new Player("Matty P");
        deckList.add(new Deck(Long.valueOf(23), "Cool Deck", p, true));
        deckList.add(new Deck(Long.valueOf(3), "Bad Deck", p, true));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.deck_list, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View deckMainFrame = getActivity().findViewById(R.id.deck_view_fragment);
        isDualPane = deckMainFrame != null && deckMainFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            activeDeckId = savedInstanceState.getInt("curDeck", 0);
        }

        if (isDualPane) {
            // Make sure our UI is in the correct state.
            showDetails(activeDeckId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curDeck", activeDeckId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck_list, container, false);
        rootView.setTag(TAG);

        return rootView;
        //return inflater.inflate(R.layout.fragment_deck_list, container, false);
    }

    private void initialize() {
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rvDeckList);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DeckListRecycleViewAdapter(deckList);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO This should not start a new Activity.  This should pass the Bundle to the DeckViewFragment
                        Bundle deckId = new Bundle();

                        Deck d = mAdapter.getItem(position);

                        deckId.putLong("deckId", d.getId());
                        //deckEditIntent.putExtras(deckId);
                        Log.i(TAG, "onClick: Deck Id " + d.getId());

                    }
                })
        );

        actionAddDeck = (ImageButton) getActivity().findViewById(R.id.action_add_deck);

        actionAddDeck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_add_deck:
                Intent deckEditIntent = new Intent("com.palmak.themagichat.decks.DECKEDITACTIVITY");
                Bundle deckId = new Bundle();

                deckId.putLong("deckId", Long.valueOf(0));
                deckEditIntent.putExtras(deckId);

                Log.i(TAG, "Creating new Deck.");

                startActivity(deckEditIntent);
                break;
            default:
                break;
        }
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int dId) {

        if (isDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            mAdapter.setSelected(dId);

            // Check what fragment is currently shown, replace if needed.
            DeckViewFragment deckViewFragment = (DeckViewFragment)
                    getFragmentManager().findFragmentById(R.id.deck_view_fragment);
            if (deckViewFragment == null || deckViewFragment.getShownIndex() != dId) {
                // Make new fragment to show this selection.
                deckViewFragment = DeckViewFragment.newInstance(dId);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.deck_view_fragment, deckViewFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DeckViewActivity.class);
            intent.putExtra("deckId", dId);
            startActivity(intent);
        }

        // Setting this after all the logic in case there's an issue along the way
        activeDeckId = dId;
    }
}

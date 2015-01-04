package com.palmak.themagichat.decks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.palmak.themagichat.R;
import com.palmak.themagichat.common.logger.Log;

import java.util.List;

/**
 * Created by Matthew on 12/5/2014.
 */
public class DeckListRecycleViewAdapter extends RecyclerView.Adapter<DeckListRecycleViewAdapter.ViewHolder> {
    private static final String TAG = "RecycleViewAdapter";

    private List<Deck> mDeckList;
    private int selectedId;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeckListRecycleViewAdapter(List<Deck> deckList) {
        mDeckList = deckList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DeckListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_deck_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        Deck d = mDeckList.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mDeckName.setText(d.getName());
        holder.mOwnerName.setText(d.getOwner().getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDeckList.size();
    }

    public Deck getItem(int position) {
        return mDeckList.get(position);
    }

    public void setSelected(int position) {
        if (position != 0) {
            selectedId = position;

            notifyItemChanged(position);
        }
    }

    public int getSelected() {
        return selectedId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mDeckName;
        private TextView mOwnerName;
        public ViewHolder(View v) {
            super(v);
            mDeckName = (TextView) v.findViewById(R.id.tvDeckName);
            mOwnerName = (TextView) v.findViewById(R.id.tvOwnerName);
        }

        public TextView getDeckName() {
            return mDeckName;
        }
    }

}

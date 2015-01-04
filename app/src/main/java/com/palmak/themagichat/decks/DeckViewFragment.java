package com.palmak.themagichat.decks;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package com.example.android.slidingtabsbasic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.graphics.Color;

import com.palmak.themagichat.R;
import com.palmak.themagichat.common.logger.Log;
import com.palmak.themagichat.common.view.SlidingTabLayout;

public class DeckViewFragment extends Fragment {

    static final String LOG_TAG = "DeckViewFragment";

    private LinearLayout deckViewLayout, deckCurveLayout, deckResultsLayout;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'dId'.
     */
    public static DeckViewFragment newInstance(int dId) {
        DeckViewFragment deckViewFragment = new DeckViewFragment();

        // Supply dId input as an argument.
        Bundle args = new Bundle();
        args.putInt("deckId", dId);
        deckViewFragment.setArguments(args);

        return deckViewFragment;
    }

    public int getShownIndex() {
        return getArguments().getInt("deckId", 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.deck_view, menu);
    }

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        return inflater.inflate(R.layout.common_sliding_tabs, container, false);
    }

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link DeckViewPagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new DeckViewPagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDividerColors(Color.TRANSPARENT);
        mSlidingTabLayout.setSelectedIndicatorColors(R.color.app_accent);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class DeckViewPagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link com.palmak.themagichat.common.view.SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Cards";
            } else if (position == 1) {
                return "Curve";
            } else if (position == 2) {
                return "Results";
            }
            return "Unknown";
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link android.support.v4.view.ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                // Inflate a new layout from our resources
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_empty_deck_view,
                        container, false);
            } else if (position == 1) {
                // Inflate a new layout from our resources
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_deck_curve,
                        container, false);
            } else if (position == 2) {
                // Inflate a new layout from our resources
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_deck_results,
                        container, false);
            }

            if (view == null) {
                Log.e(LOG_TAG, "instantiateItem() - position: " + position + " doesn't exist..");
                return null;
            }
            // Add the newly created View to the ViewPager
            container.addView(view);

            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }
}

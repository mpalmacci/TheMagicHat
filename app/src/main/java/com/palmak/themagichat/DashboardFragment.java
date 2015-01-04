package com.palmak.themagichat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Matthew Palmacci on 12/2/2014.
 */
public class DashboardFragment extends Fragment {
    private ImageButton actionPlayGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);
    }

    private void initialize() {
        actionPlayGame = (ImageButton) getActivity().findViewById(R.id.action_play_game);

        actionPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch (arg0.getId()) {
                    case R.id.action_play_game:
                        // TODO Start the Play Game Activity
                        break;
                    default:
                        break;
                }
            }
        });
    }
}

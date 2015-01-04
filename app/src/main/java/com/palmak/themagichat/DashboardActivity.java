package com.palmak.themagichat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.palmak.themagichat.common.activities.TMHActivityBase;

public class DashboardActivity extends TMHActivityBase {
    Fragment dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);

        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_decks:
                Intent deckMainIntent = new Intent("com.palmak.themagichat.decks.DECKMAINACTIVITY");
                startActivity(deckMainIntent);
                return true;
            case R.id.action_players:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        dashboardFragment = getFragmentManager().findFragmentById(R.id.dashboard_fragment);
    }

}

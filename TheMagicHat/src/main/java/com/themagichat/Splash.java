package com.themagichat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import com.themagichat.cards.CardDbUtil;
import com.themagichat.decks.db.MagicHatDb;

import java.io.IOException;

public class Splash extends MagicHatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/*
		 * Toast.makeText(Splash.this, "Checking for updates...",
		 * Toast.LENGTH_SHORT) .show();
		 */

/*		new backupDb().execute();
		Splash.this.finish();*/

		new setupCardDb().execute();
		new setupDeckDb().execute();

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException iE) {
					iE.printStackTrace();
				} finally {
					Intent openMagicHatMain = new Intent(
							"com.themagichat.MAGICHATHOME");
					startActivity(openMagicHatMain);
				}
			}
		};
		timer.start();
	}

	private class backupDb extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			MagicHatDb mhDb = new MagicHatDb(Splash.this);
			mhDb.openWritableDB();
			mhDb.backupDb();
			mhDb.closeDB();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Toast.makeText(Splash.this, "Database is backed up",
					Toast.LENGTH_SHORT).show();
			Splash.this.finish();
		}
	}

	private class setupCardDb extends AsyncTask<String, Integer, String> {
		boolean wasCreated = false, wasUpgraded = false;

		@Override
		protected String doInBackground(String... params) {

			try {
				CardDbUtil.initCardDb(Splash.this);
				wasCreated = CardDbUtil.createDb;
				wasUpgraded = CardDbUtil.isUpgrade;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (wasUpgraded) {
				Toast.makeText(Splash.this,
						"Cards have been updated to latest version",
						Toast.LENGTH_SHORT).show();
			} else if (wasCreated) {
				Toast.makeText(Splash.this, "Cards have been initialized",
						Toast.LENGTH_SHORT).show();
			} else {
				/*
				 * Toast.makeText(Splash.this,
				 * "No changes to Cards were needed",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		}
	}

	private class setupDeckDb extends AsyncTask<String, Integer, String> {
		boolean wasCreated = false, isUpgrade = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (!MagicHatDb.isCreated()) {
				/*
				 * Toast.makeText(Splash.this,
				 * "Initializing Decks... Please wait...",
				 * Toast.LENGTH_SHORT).show();
				 */
				wasCreated = true;
			}
		}

		@Override
		protected String doInBackground(String... params) {
			MagicHatDb mhDb = new MagicHatDb(Splash.this);

			mhDb.openReadableDB();
			isUpgrade = mhDb.isUpgrade();
			mhDb.closeDB();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (wasCreated) {
				Toast.makeText(Splash.this, "Decks have been initialized",
						Toast.LENGTH_SHORT).show();
			} else if (isUpgrade) {
				Toast.makeText(Splash.this,
						"Decks have been updated to latest version",
						Toast.LENGTH_SHORT).show();
			} else {
				/*
				 * Toast.makeText(Splash.this,
				 * "No changes to Decks were needed",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Splash.this.finish();
	}

}

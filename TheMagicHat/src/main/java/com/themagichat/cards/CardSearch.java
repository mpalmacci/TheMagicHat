package com.themagichat.cards;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.themagichat.MagicHatActivity;
import com.themagichat.R;

import java.util.List;

public class CardSearch extends MagicHatActivity implements OnClickListener {

	Button bSearch;
	EditText etRulesText, etCMC;
	AutoCompleteTextView etName, etSubtype;
	Spinner sExpansion, sBlock, sType, sCMCEquality;
	ToggleButton tbWhite, tbBlue, tbBlack, tbRed, tbGreen, tbMythic, tbRare,
			tbUncommon, tbCommon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_search);

		initialize();

		new populateAutoFillCardNames().execute();
		new populateExpansions().execute();
		new populateSubTypes().execute();
	}

	private class populateAutoFillCardNames extends
			AsyncTask<String, Integer, ArrayAdapter<Card>> {

		@Override
		protected ArrayAdapter<Card> doInBackground(String... params) {
			CardDbUtil.getStaticDb();
			List<Card> allCardNames = CardDbUtil.getAllCardNames();
			CardDbUtil.close();

			ArrayAdapter<Card> cardNameAdapter = new ArrayAdapter<Card>(
					CardSearch.this,
					android.R.layout.simple_dropdown_item_1line, allCardNames);

			return cardNameAdapter;
		}

		@Override
		protected void onPostExecute(ArrayAdapter<Card> cardNameAdapter) {
			super.onPostExecute(cardNameAdapter);
			cardNameAdapter.setDropDownViewResource(R.layout.mh_spinner_dropdown);
			// cardNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			etName.setAdapter(cardNameAdapter);
		}
	}

	private class populateExpansions extends
			AsyncTask<String, Integer, ArrayAdapter<Expansion>> {

		@Override
		protected ArrayAdapter<Expansion> doInBackground(String... params) {
			CardDbUtil.getStaticDb();
			List<Expansion> allExpansions = CardDbUtil.getAllExpansions();
			CardDbUtil.close();

			if (allExpansions.isEmpty()) {
				System.out.println("allExpansions is empty!");
				finish();
			}

			allExpansions.add(0, new Expansion("Any", null));

			ArrayAdapter<Expansion> expAdapter = new ArrayAdapter<Expansion>(
					CardSearch.this, R.layout.mh_spinner,
					allExpansions);

			return expAdapter;
		}

		@Override
		protected void onPostExecute(ArrayAdapter<Expansion> expAdapter) {
			super.onPostExecute(expAdapter);
			expAdapter.setDropDownViewResource(R.layout.mh_spinner_dropdown);
			// expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			sExpansion.setAdapter(expAdapter);
		}
	}

	private class populateSubTypes extends
			AsyncTask<String, Integer, ArrayAdapter<String>> {

		@Override
		protected ArrayAdapter<String> doInBackground(String... params) {
			CardDbUtil.getStaticDb();
			String[] stAllSubTypes = CardDbUtil.getAllCardSubTypes();
			CardDbUtil.close();
			ArrayAdapter<String> cardSubTypesAdapter = new ArrayAdapter<String>(
					CardSearch.this,
					android.R.layout.simple_dropdown_item_1line, stAllSubTypes);

			return cardSubTypesAdapter;
		}

		@Override
		protected void onPostExecute(ArrayAdapter<String> cardSubTypesAdapter) {
			super.onPostExecute(cardSubTypesAdapter);
			cardSubTypesAdapter.setDropDownViewResource(R.layout.mh_spinner_dropdown);
			// cardSubTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			etSubtype.setAdapter(cardSubTypesAdapter);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		new performSearch().execute();

		// The next two lines of code hide the keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(bSearch.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private class performSearch extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Temporary code for testing
			Bundle cardName = new Bundle();
			cardName.putString("cardName", etName.getText().toString());

			Intent openCardViewActivity = new Intent("com.themagichat.cards.CARDVIEW");
			openCardViewActivity.putExtras(cardName);
			startActivity(openCardViewActivity);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// TODO - Store search entry items, and clear device memory usage
		
	}

	private void initialize() {
		bSearch = (Button) findViewById(R.id.bSearch);
		etName = (AutoCompleteTextView) findViewById(R.id.etName);
		etRulesText = (EditText) findViewById(R.id.etRulesText);
		etCMC = (EditText) findViewById(R.id.etCMC);
		sCMCEquality = (Spinner) findViewById(R.id.sCMCEquality);
		sExpansion = (Spinner) findViewById(R.id.sExpansion);
		sBlock = (Spinner) findViewById(R.id.sBlock);
		sType = (Spinner) findViewById(R.id.sType);
		etSubtype = (AutoCompleteTextView) findViewById(R.id.etSubtype);
		tbMythic = (ToggleButton) findViewById(R.id.tbMythic);
		tbRare = (ToggleButton) findViewById(R.id.tbRare);
		tbUncommon = (ToggleButton) findViewById(R.id.tbUncommon);
		tbCommon = (ToggleButton) findViewById(R.id.tbCommon);
		tbWhite = (ToggleButton) findViewById(R.id.tbWhite);
		tbBlue = (ToggleButton) findViewById(R.id.tbBlue);
		tbBlack = (ToggleButton) findViewById(R.id.tbBlack);
		tbRed = (ToggleButton) findViewById(R.id.tbRed);
		tbGreen = (ToggleButton) findViewById(R.id.tbGreen);
		
		this.tvTitle.setText("Card Search");
		
		bSearch.setOnClickListener(this);
	}
}
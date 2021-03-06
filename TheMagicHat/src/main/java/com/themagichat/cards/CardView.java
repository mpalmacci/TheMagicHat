package com.themagichat.cards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.themagichat.MagicHatActivity;
import com.themagichat.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class CardView extends MagicHatActivity {

	LinearLayout llCardList;
	TextView tvExpansion;
	ImageView ivCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_view);
		initialize();

		Bundle bName = getIntent().getExtras();
		String cardName = bName.getString("cardName");
		
		this.tvTitle.setText(cardName);

		new getCardImages().execute(cardName);
	}

	private class getCardImages extends
			AsyncTask<String, Integer, Map<Expansion, URL>> {

		@Override
		protected Map<Expansion, URL> doInBackground(
				String... cardName) {

			CardDbUtil.getStaticDb();
			// Temporary code for testing
			int cardId = CardDbUtil.getCardId(cardName[0]);
			Map<Expansion, URL> cardImages = CardDbUtil
					.getExpansionImages(cardId);
			CardDbUtil.close();
			return cardImages;
		}

		@Override
		protected void onPostExecute(
				Map<Expansion, URL> cardImages) {
			super.onPostExecute(cardImages);
			if (cardImages.keySet().iterator().hasNext()) {
				Expansion keyExp = cardImages.keySet().iterator().next();
				tvExpansion.setText(keyExp.toString());

				new showCard().execute(cardImages.get(keyExp));
			}
		}
	}

	private class showCard extends AsyncTask<URL, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(URL... urls) {
			InputStream is = null;
			BufferedInputStream bis = null;
			Bitmap cardImage = null;
			try {
				HttpURLConnection conn = (HttpURLConnection) urls[0]
						.openConnection();
				conn.connect();
				is = conn.getInputStream();
				bis = new BufferedInputStream(is);
				cardImage = BitmapFactory.decodeStream(bis);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return cardImage;
		}

		@Override
		protected void onPostExecute(Bitmap cardImage) {
			super.onPostExecute(cardImage);
			ivCard.setImageBitmap(cardImage);
		}
	}
	
	private void initialize() {
		tvExpansion = (TextView) findViewById(R.id.tvExpansion);
		ivCard = (ImageView) findViewById(R.id.ivCard);
		llCardList = (LinearLayout) findViewById(R.id.llCardList);
	}
}

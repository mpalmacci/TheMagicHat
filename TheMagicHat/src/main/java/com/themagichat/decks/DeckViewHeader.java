package com.themagichat.decks;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.themagichat.R;
import com.themagichat.decks.DeckViewAdapter.RowType;

public class DeckViewHeader implements DeckViewItem {
	private final String title;

	public DeckViewHeader(LayoutInflater inflater, String title) {
		this.title = title;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	private static class ViewHolder {
		TextView header;
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		ViewHolder holder = new ViewHolder();

		if (convertView == null) {
			convertView = (View) inflater.inflate(R.layout.deck_view_header,
					null);

			holder.header = (TextView) convertView.findViewById(R.id.header);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.header.setText(title);

		return convertView;
	}

}

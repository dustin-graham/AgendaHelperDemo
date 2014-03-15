package com.rain.android.agenda.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rain.android.agenda.data.database.table.AgendaTable;

/**
 * Created by dustin on 3/12/14. :)
 */
public class AgendaAdapter extends CursorAdapter {

    private static class Viewholder {
        TextView titleText;
    }

    public AgendaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItem = View.inflate(context,android.R.layout.simple_list_item_1, null);
        TextView titleText = (TextView) listItem.findViewById(android.R.id.text1);
        Viewholder holder = new Viewholder();
        holder.titleText = titleText;
        listItem.setTag(holder);
        return listItem;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Viewholder holder = (Viewholder) view.getTag();
        String agendaName = cursor.getString(cursor.getColumnIndex(AgendaTable.NAME));
        holder.titleText.setText(agendaName);
    }
}

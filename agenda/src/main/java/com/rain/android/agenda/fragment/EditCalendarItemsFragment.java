package com.rain.android.agenda.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.rain.android.agenda.data.database.table.CalendarItemTable;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.util.BusProvider;

/**
 * Created by dustin on 3/13/14. :)
 */
public class EditCalendarItemsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_AGENDA_ID = "EditCalendarItemsFragment:AgendaId";

    private long mAgendaId;
    private CursorAdapter mAdapter;

    public static EditCalendarItemsFragment create(long agendaId) {
        EditCalendarItemsFragment fragment = new EditCalendarItemsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_AGENDA_ID, agendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgendaId = getArguments().getLong(EXTRA_AGENDA_ID);
        mAdapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,new String[]{CalendarItemTable.TITLE},new int[]{android.R.id.text1},0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), AgendaProvider.CALENDAR_ITEM_CONTENT_URI,null,CalendarItemTable.WHERE_AGENDA_ID_EQUALS,new String[] {String.valueOf(mAgendaId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        BusProvider.getInstance().post(new OnCalendarItemClickedEvent(id));
    }

    //Otto events
    public static class OnCalendarItemClickedEvent {
        private long mCalendarId;

        public OnCalendarItemClickedEvent(long calendarId) {
            mCalendarId = calendarId;
        }

        public long getCalendarId() {
            return mCalendarId;
        }
    }
}

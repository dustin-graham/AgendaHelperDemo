package com.rain.android.agenda.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.database.table.CalendarItemTable;
import com.rain.android.agenda.data.model.CalendarItem;
import com.rain.android.agenda.data.provider.AgendaProvider;

/**
 * Created by dustin on 3/12/14. :)
 */
public class CalendarItemListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_AGENDA_ID = "CalendarItemListFragment:AgendaId";

    public static CalendarItemListFragment create(long agendaId) {
        Bundle args = new Bundle();
        args.putLong(EXTRA_AGENDA_ID, agendaId);
        CalendarItemListFragment frag = new CalendarItemListFragment();
        frag.setArguments(args);
        return frag;
    }

    private Long _agendaId;
    private CalendarItemListFragmentListener _listener;
    private CursorAdapter _adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (CalendarItemListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The host activity must implement CalendarItemListFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _agendaId = getArguments().getLong(EXTRA_AGENDA_ID);
        _adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,new String[] {CalendarItemTable.TITLE},new int[]{android.R.id.text1},0);
        setListAdapter(_adapter);
        getLoaderManager().initLoader(0,null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), AgendaProvider.AGENDA_CONTENT_URI, null, CalendarItemTable.AGENDA_ID + " = ?", new String[]{String.valueOf(_agendaId)}, CalendarItemTable.DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        _adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        _adapter.swapCursor(null);
    }

    public interface CalendarItemListFragmentListener {
        void OnCalendarItemSelected(Long calendarItemId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.calendar_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.new_calendar_item) {
            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setTitle("New Calendar Item");
            calendarItem.setAgendaId(_agendaId);
            getActivity().getContentResolver().insert(AgendaProvider.CALENDAR_ITEM_CONTENT_URI,calendarItem.getContentValues());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

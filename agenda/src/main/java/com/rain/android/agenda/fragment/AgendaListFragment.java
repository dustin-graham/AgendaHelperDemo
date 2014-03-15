package com.rain.android.agenda.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rain.android.agenda.adapter.AgendaAdapter;
import com.rain.android.agenda.data.database.table.AgendaTable;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.util.BusProvider;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * interface.
 */
public class AgendaListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CursorAdapter mAdapter;

    public static AgendaListFragment newInstance() {
        return new AgendaListFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AgendaListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AgendaAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        BusProvider.getInstance().post(new AgendaSelectedEvent(id));
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = getListView().getEmptyView();

        if (emptyView != null && emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), AgendaProvider.AGENDA_CONTENT_URI,new String[]{AgendaTable._ID, AgendaTable.NAME},null,null,AgendaTable._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    //Otto events
    public static class AgendaSelectedEvent {
        private long mAgendaId;

        public AgendaSelectedEvent(long agendaId) {
            mAgendaId = agendaId;
        }

        public long getAgendaId() {
            return mAgendaId;
        }
    }

}

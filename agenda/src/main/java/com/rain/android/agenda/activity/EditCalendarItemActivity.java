package com.rain.android.agenda.activity;

import android.os.Bundle;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.database.table.CalendarItemTable;
import com.rain.android.agenda.data.model.CalendarItem;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.fragment.EditCalendarItemFragment;
import com.squareup.otto.Subscribe;

/**
 * Created by dustin on 3/13/14. :)
 */
public class EditCalendarItemActivity extends BaseActivity {

    public static final String EXTRA_CALENDAR_ID = "EditCalendarItemActivity:CalendarId";
    private static final String TAG_EDIT_CALENDAR_FRAG = "EditCalendarItemActivity:EditCalendarFrag";

    private long mCalendarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_fragment_host);

        mCalendarId = getIntent().getLongExtra(EXTRA_CALENDAR_ID, 0);

        getSupportFragmentManager().beginTransaction().add(R.id.container, EditCalendarItemFragment.create(mCalendarId), TAG_EDIT_CALENDAR_FRAG).commit();
        getBus().register(this);
    }

    @Subscribe
    public void OnCalendarItemModified(EditCalendarItemFragment.OnCalendarItemModifiedEvent event) {
        CalendarItem item = event.getCal();
        getContentResolver().update(AgendaProvider.CALENDAR_ITEM_CONTENT_URI,item.getContentValues(), CalendarItemTable.WHERE_ID_EQUALS, new String[]{String.valueOf(item.getRowId())});
    }
}

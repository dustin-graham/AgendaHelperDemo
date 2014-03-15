package com.rain.android.agenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.model.CalendarItem;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.fragment.EditCalendarItemsFragment;
import com.squareup.otto.Subscribe;

/**
 * Created by dustin on 3/13/14. :)
 */
public class EditCalendarItemsActivity extends BaseActivity {

    public static final String EXTRA_AGENDA_ID = "EditCalendarItemsActivity:AgendaId";
    private static final String TAG_CALANDAR_LIST = "EditCalendarItemsActivity:CalendarList";

    private long mAgendaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_fragment_host);

        mAgendaId = getIntent().getLongExtra(EXTRA_AGENDA_ID,0);

        getSupportFragmentManager().beginTransaction().add(R.id.container, EditCalendarItemsFragment.create(mAgendaId), TAG_CALANDAR_LIST).commit();
        getBus().register(this);
    }

    @Subscribe
    public void OnCalendarItemClicked(EditCalendarItemsFragment.OnCalendarItemClickedEvent event) {
        long calendarId = event.getCalendarId();
        //launch intent for calendar item edit
        Intent intent = new Intent(this, EditCalendarItemActivity.class);
        intent.putExtra(EditCalendarItemActivity.EXTRA_CALENDAR_ID, calendarId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_calendar_item) {
            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setTitle("New Event");
            calendarItem.setAgendaId(mAgendaId);
            getContentResolver().insert(AgendaProvider.CALENDAR_ITEM_CONTENT_URI,calendarItem.getContentValues());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

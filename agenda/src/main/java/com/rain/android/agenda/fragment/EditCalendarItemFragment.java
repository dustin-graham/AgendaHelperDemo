package com.rain.android.agenda.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.database.table.CalendarItemTable;
import com.rain.android.agenda.data.model.CalendarItem;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.fragment.dialog.DateTimePickerDialogFragment;
import com.rain.android.agenda.fragment.dialog.TimePickerDialogFragment;
import com.rain.android.agenda.util.BusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dustin on 3/12/14. :)
 */
public class EditCalendarItemFragment extends Fragment {

    public static final String EXTRA_CALENDAR_ID = "EditCalendarItemFragment:CalendarId";

    private CalendarItem mCalendarItem;

    @InjectView(R.id.calendar_item_title)
    EditText mTitle;

    @InjectView(R.id.calendar_item_date_input)
    EditText mDate;

    @InjectView(R.id.calendar_item_time_input)
    EditText mTime;

    @InjectView(R.id.calendar_item_location_input)
    EditText mLocation;

    @InjectView(R.id.calendar_item_notes_input)
    EditText mNotes;

    public static EditCalendarItemFragment create(long calendarId) {
        EditCalendarItemFragment fragment = new EditCalendarItemFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_CALENDAR_ID, calendarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long calendarId = getArguments().getLong(EXTRA_CALENDAR_ID);
        Cursor calendarCursor = getActivity().getContentResolver().query(AgendaProvider.CALENDAR_ITEM_CONTENT_URI,null, CalendarItemTable.WHERE_ID_EQUALS, new String[]{String.valueOf(calendarId)}, null);
        calendarCursor.moveToFirst();
        mCalendarItem = new CalendarItem(calendarCursor);
        calendarCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_calendar_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDateInputSelected();
                }
            }
        });
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateInputSelected();
            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeInputSelected();
            }
        });
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateCalendarItem();
        BusProvider.getInstance().post(new OnCalendarItemModifiedEvent(mCalendarItem));
    }

    private void init() {
        mTitle.setText(mCalendarItem.getTitle());
        mDate.setText(mCalendarItem.getDate());
        mTime.setText(mCalendarItem.getTime());
        mLocation.setText(mCalendarItem.getLocation());
        mNotes.setText(mCalendarItem.getNotes());
    }

    private void updateCalendarItem() {
        mCalendarItem.setTitle(mTitle.getText().toString());
        mCalendarItem.setDate(mDate.getText().toString());
        mCalendarItem.setTime(mTime.getText().toString());
        mCalendarItem.setLocation(mLocation.getText().toString());
        mCalendarItem.setNotes(mNotes.getText().toString());
    }

    public void onDateInputSelected() {
        DialogFragment dateFragment = new DateTimePickerDialogFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    public void onTimeInputSelected() {
        DialogFragment dateFragment = new TimePickerDialogFragment();
        dateFragment.show(getFragmentManager(), "timePicker");
    }

    //Otto events
    public static class OnCalendarItemModifiedEvent {
        private CalendarItem mCal;

        public OnCalendarItemModifiedEvent(CalendarItem cal) {
            mCal = cal;
        }

        public CalendarItem getCal() {
            return mCal;
        }
    }
}

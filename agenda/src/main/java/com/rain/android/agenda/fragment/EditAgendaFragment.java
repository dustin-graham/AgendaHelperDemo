package com.rain.android.agenda.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.database.table.AgendaTable;
import com.rain.android.agenda.data.model.Agenda;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.util.BusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dustin on 3/13/14. :)
 */
public class EditAgendaFragment extends Fragment {

    public static final String EXTRA_AGENDA_ID = "EditAgendaFragment:AgendaId";

    @InjectView(R.id.edit_calendar_button)
    Button mEditCalendarButton;

    @InjectView(R.id.edit_item_groups_button)
    Button mEditItemGroups;

    @InjectView(R.id.agenda_title_input)
    EditText mAgendaTitle;

    @InjectView(R.id.agenda_person_conducting_input)
    EditText mPersonConducting;

    private Long mAgendaId;
    private EditAgendaFragmentListener mFragmentListener;
    private Agenda mAgenda;

    public static EditAgendaFragment create(Long agendaId) {
        EditAgendaFragment frag = new EditAgendaFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_AGENDA_ID, agendaId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgendaId = getArguments().getLong(EXTRA_AGENDA_ID);
        Cursor agendaCursor = getActivity().getContentResolver().query(AgendaProvider.AGENDA_CONTENT_URI, null, AgendaTable._ID + " = ?", new String[]{mAgendaId.toString()}, null);
        agendaCursor.moveToFirst();
        mAgenda = new Agenda(agendaCursor);
        agendaCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_agenda, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        init(mAgenda);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateAgenda();
        BusProvider.getInstance().post(new OnAgendaModifiedEvent(mAgenda));
    }

    private void init(Agenda agenda) {
        mAgendaTitle.setText(agenda.getName());
        mPersonConducting.setText(agenda.getPersonConducting());
    }

    private void updateAgenda() {
        mAgenda.setName(mAgendaTitle.getText().toString());
        mAgenda.setPersonConducting(mPersonConducting.getText().toString());
    }

    @OnClick(R.id.edit_calendar_button)
    void EditCalendarItems() {
        BusProvider.getInstance().post(new CalendarEditRequestEvent());
    }

    @OnClick(R.id.edit_item_groups_button)
    void EditItemGroups() {
        BusProvider.getInstance().post(new AgendaGroupEditRequestEvent());
    }

    public interface EditAgendaFragmentListener {
        void OnEditCalendarItems();

        void OnEditItemGroups();
    }

    //Otto events
    public static class OnAgendaModifiedEvent {
        private Agenda mAgenda;

        public OnAgendaModifiedEvent(Agenda agenda) {
            mAgenda = agenda;
        }

        public Agenda getAgenda() {
            return mAgenda;
        }
    }

    public static class OnAgendaDeletedEvent {
        private Agenda mAgenda;

        public OnAgendaDeletedEvent(Agenda agenda) {
            mAgenda = agenda;
        }

        public Agenda getAgenda() {
            return mAgenda;
        }
    }

    public static class CalendarEditRequestEvent {}
    public static class AgendaGroupEditRequestEvent {}
}

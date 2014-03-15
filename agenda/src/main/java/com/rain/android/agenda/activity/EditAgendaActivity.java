package com.rain.android.agenda.activity;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;

import com.rain.android.agenda.R;
import com.rain.android.agenda.data.database.table.AgendaTable;
import com.rain.android.agenda.data.model.Agenda;
import com.rain.android.agenda.data.provider.AgendaProvider;
import com.rain.android.agenda.fragment.EditAgendaFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dustin on 3/13/14. :)
 */
public class EditAgendaActivity extends BaseActivity {

    public static final String EXTRA_AGENDA_ID = "EditAgendaActivity:AgendaId";
    private static final String TAG_AGENDA_FRAGMENT = "EditAgendaActivity:AgendaFragment";

    private Long mAgendaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_fragment_host);

        mAgendaId = getIntent().getLongExtra(EXTRA_AGENDA_ID, 0);

        getSupportFragmentManager().beginTransaction().add(R.id.container, EditAgendaFragment.create(mAgendaId), TAG_AGENDA_FRAGMENT).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBus().register(this);
    }

    @Subscribe
    public void OnAgendaModified(EditAgendaFragment.OnAgendaModifiedEvent event) {
        Agenda agenda = event.getAgenda();
        getContentResolver().update(AgendaProvider.AGENDA_CONTENT_URI, agenda.getContentValues(), AgendaTable.WHERE_ID_EQUALS, new String[]{agenda.getRowId().toString()});
    }

    public void updateLotsOfAgendasAtOnce(List<Agenda> agendas) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (int i = 0; i < agendas.size(); i++) {
            ContentProviderOperation operation = ContentProviderOperation.newUpdate(AgendaProvider.AGENDA_CONTENT_URI)
                    .withSelection(AgendaTable.WHERE_ID_EQUALS, new String[]{agendas.get(i).getRowId().toString()}).build();
            operations.add(operation);
        }
        //just for fun, let's insert a few new ones while we're at it
        for (int i = 0; i < 10; i++) {
            Agenda agenda = new Agenda();
            agenda.setName("New Agenda " + i);
            ContentProviderOperation operation = ContentProviderOperation.newInsert(AgendaProvider.AGENDA_CONTENT_URI).withValues(agenda.getContentValues()).build();
            operations.add(operation);

        }
        try {
            getContentResolver().applyBatch(AgendaProvider.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void EditCalendarItems(EditAgendaFragment.CalendarEditRequestEvent event) {
        //launch intent
        Intent intent = new Intent(this, EditCalendarItemsActivity.class);
        intent.putExtra(EditCalendarItemsActivity.EXTRA_AGENDA_ID, mAgendaId);
        startActivity(intent);
    }

    @Subscribe
    public void EditAgendaGroups(EditAgendaFragment.AgendaGroupEditRequestEvent event) {
        //launch intent
    }
}

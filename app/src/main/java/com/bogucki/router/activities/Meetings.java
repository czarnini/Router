package com.bogucki.router.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bogucki.router.R;
import com.bogucki.router.utils.ConstantValues;
import com.bogucki.router.dialogs.AddNewOrEditMeetingDialog;
import com.bogucki.router.dialogs.ChooseActionForMeeting;
import com.bogucki.router.models.Meeting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Meetings extends AppCompatActivity {

    private static final String TAG = Meetings.class.getSimpleName();
    private String formattedDate;
    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_meetings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        formattedDate = createDateFromExtras(extras);
        attachFireBaseAdapter();
        handleFloatingButton();

        setTitle(formattedDate.replaceAll("_", "."));
    }

    private void attachFireBaseAdapter() {
        DatabaseReference meetingsReference = FirebaseDatabase.getInstance().getReference()
                .child(ConstantValues.MEETINGS_FIREBASE)
                .child(formattedDate);

        RecyclerView meetingsList = (RecyclerView) findViewById(R.id.meetings_list);
        meetingsList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingHolder>(
                Meeting.class,
                R.layout.meeting_list_item,
                MeetingHolder.class,
                meetingsReference) {
            @Override
            protected void populateViewHolder(MeetingHolder viewHolder, final Meeting model, int position) {
                viewHolder.setClient(model.getClient());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setReason(model.getReason());
                viewHolder.setDate(model.getEarliestTimePossible());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment dialogFragment = new ChooseActionForMeeting();
                        Bundle args = new Bundle();
                        args.putString(ConstantValues.MEETING_ID_BUNDLE_KEY, model.getPushId());
                        args.putString(ConstantValues.CLIENT_NAME_BUNDLE_KEY, model.getClient());
                        args.putString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY, model.getAddress());
                        args.putString(ConstantValues.MEETING_REASON_BUNDLE_KEY, model.getReason());
                        args.putString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY, ConstantValues.MEETINGS_FIREBASE);
                        args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, formattedDate);
                        args.putInt(ConstantValues.MEETING_ORDER, model.getMeetingOrder());
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getSupportFragmentManager(), TAG);
                    }
                });

            }
        };
        meetingsList.setAdapter(mAdapter);
    }

    private String createDateFromExtras(Bundle extras) {
        if (null != extras) {
            return extras.getString("day") + "_" + extras.getString("month") + "_" + extras.getString("year");
        } else {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.todays_route_option: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.clients_option: {
                Intent intent = new Intent(this, Clients.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meetings, menu);
        return true;
    }

    private void handleFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialogFragment = new AddNewOrEditMeetingDialog();
                    Bundle args = new Bundle();
                    args.putString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY, ConstantValues.MEETINGS_FIREBASE);
                    args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, formattedDate);
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getSupportFragmentManager(), TAG);
                }
            });
        }
    }


}

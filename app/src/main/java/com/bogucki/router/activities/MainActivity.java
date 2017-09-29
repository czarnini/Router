package com.bogucki.router.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bogucki.router.R;
import com.bogucki.router.dialogs.AddNewOrEditMeetingDialog;
import com.bogucki.router.dialogs.ChooseActionForMeeting;
import com.bogucki.router.dialogs.DatePickerFragment;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.Utils.ConstantValues;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView todayMeetings;
    private DatabaseReference meetingsReference;
    FirebaseRecyclerAdapter mAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        attachFireBaseAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetings_option: {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), TAG);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void attachFireBaseAdapter() {

        meetingsReference = FirebaseDatabase.getInstance().getReference()
                .child(ConstantValues.MEETINGS_FIREBASE)
                .child(getFormattedDate());

        todayMeetings = (RecyclerView) findViewById(R.id.today_meetings);
        todayMeetings.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingHolder>(
                Meeting.class, R.layout.meeting_list_item, MeetingHolder.class, meetingsReference) {
            @Override
            protected void populateViewHolder(MeetingHolder viewHolder, final Meeting model, int position) {
                viewHolder.setClient(model.getClient());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setReason(model.getReason());
                viewHolder.setDate(model.getEarliestTimeOfDelivery());

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
                        args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, getFormattedDate());
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getSupportFragmentManager(), TAG);
                    }
                });
            }
        };
        todayMeetings.setAdapter(mAdapter);
    }

    @NonNull
    private String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) +1 ); // because January is 0
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return "27_9_2017";
        //return day + "_" + month + "_" + year;
    }



}



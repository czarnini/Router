package com.bogucki.router.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;
import com.bogucki.router.dialogs.AddNewOrEditClientDialog;
import com.bogucki.router.dialogs.AddNewOrEditMeetingDialog;
import com.bogucki.router.models.Meeting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Meetings extends AppCompatActivity {

    private static final String TAG = Meetings.class.getSimpleName();
    private String formattedDate;
    private RecyclerView meetingsList;
    private DatabaseReference meetingsReference;
    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_meetings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        formattedDate = createDateFromExtras(extras);
        setTitle(formattedDate.replaceAll("_", "."));

        attachFireBaseAdapter();
        handleFloatingButton();
    }

    private void attachFireBaseAdapter(){
        meetingsReference = FirebaseDatabase.getInstance().getReference()
                .child(ConstantValues.MEETINGS_FIREBASE)
                .child(formattedDate);

        meetingsList = (RecyclerView) findViewById(R.id.meetings_list);
        meetingsList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingHolder>(Meeting.class, R.layout.meeting_list_item, MeetingHolder.class,meetingsReference){
            @Override
            protected void populateViewHolder(MeetingHolder viewHolder, Meeting model, int position) {
                viewHolder.setClient(model.getClient());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setReason(model.getReason());
                viewHolder.setDate(model.getEarliestTimeOfDelivery());
                viewHolder.setPushId(model.getPushId());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
            }
        };
        meetingsList.setAdapter(mAdapter);
    }

    private String createDateFromExtras(Bundle extras) {
        if(null != extras) {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AddNewOrEditMeetingDialog();
                dialogFragment.show(getSupportFragmentManager(), TAG);
            }
        });
    }

}

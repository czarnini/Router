package com.bogucki.router.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bogucki.router.R;
import com.bogucki.router.adapters.MeetingItemTouchHelperCallback;
import com.bogucki.router.adapters.MeetingsAdapter;
import com.bogucki.router.dialogs.DatePickerFragment;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.utils.ConstantValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Meeting> todayMeetings = new ArrayList<>();
    private DatabaseReference meetingsReference;

    MeetingsAdapter mAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        attachFireBaseAdapter();
        registerListenerForMeetings();
        handleOptimizeButton();

    }

    private void registerListenerForMeetings() {
        meetingsReference.orderByChild(ConstantValues.MEETING_ORDER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todayMeetings.clear();
                for (DataSnapshot meeting : dataSnapshot.getChildren()) {
                    todayMeetings.add(meeting.getValue(Meeting.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 01.04.2018 Handle error
            }
        });
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
        RecyclerView todayMeetingsView = findViewById(R.id.meeting_list);
        todayMeetingsView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MeetingsAdapter(todayMeetings, getSupportFragmentManager());
        ItemTouchHelper.Callback callback = new MeetingItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(todayMeetingsView);
        todayMeetingsView.setAdapter(mAdapter);
    }



    //TODO extract this to Utilis package
    private String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1); // because January is 0
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return "2_1_2018";
        //return day + "_" + month + "_" + year;
    }

    private void handleOptimizeButton() {
        Button optimize = findViewById(R.id.optimize_button);
        optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = meetingsReference.getKey();
                DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("requests").child(key);
                requests.setValue(true);
            }
        });
    }

}



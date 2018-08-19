package com.bogucki.router.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.adapters.MeetingItemTouchHelperCallback;
import com.bogucki.router.adapters.MeetingsAdapter;
import com.bogucki.router.dialogs.AddNewOrEditMeetingDialog;
import com.bogucki.router.dialogs.DatePickerFragment;
import com.bogucki.router.dialogs.DelayedClientsDialog;
import com.bogucki.router.dialogs.OptimizeNewPointsDialog;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.utils.ConstantValues;
import com.bogucki.router.utils.NetworkMonitor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Meeting> todayMeetings = new ArrayList<>();
    private ArrayList<Meeting> meetingsToDelete = new ArrayList<>();
    private DatabaseReference meetingsReference;
    private DatabaseReference optimizeReference;
    private DatabaseReference updateReference;
    MeetingsAdapter mAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isOptimizationStarted = false;
    private FloatingActionButton addMeetingButton;
    private TextView noDataTW;
    private ScheduledThreadPoolExecutor optimizationExecutor = new ScheduledThreadPoolExecutor(2);

    private Snackbar optimizingSnackBar;
    private Snackbar errorDataLoadingSnackBar;
    private Snackbar loadingDataSnackBar;
    private Snackbar recalculatingSnackBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        noDataTW = findViewById(R.id.noDataInfoView);
        addMeetingButton = findViewById(R.id.add_meeting);
        setSupportActionBar(toolbar);
        attachFireBaseAdapter();

        optimizeReference = FirebaseDatabase.getInstance().getReference()
                .child("requests");

        updateReference = FirebaseDatabase.getInstance().getReference()
                .child("update");


        loadingDataSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.loading_data_snackbar, Snackbar.LENGTH_INDEFINITE);
        errorDataLoadingSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.error_getting_data, Snackbar.LENGTH_INDEFINITE);
        optimizingSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.optimizing_started, Snackbar.LENGTH_INDEFINITE);
        recalculatingSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.recalculating, Snackbar.LENGTH_INDEFINITE);
        View sbErrorView = errorDataLoadingSnackBar.getView();
        sbErrorView.setBackgroundColor(getColor(R.color.colorMistake));


        if (addMeetingButton != null) {
            addMeetingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialogFragment = new AddNewOrEditMeetingDialog();
                    Bundle args = new Bundle();
                    args.putString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY, ConstantValues.MEETINGS_FIREBASE);
                    args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, getFormattedDate());
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getSupportFragmentManager(), TAG);
                }
            });

        }
        loadingDataSnackBar.show();

        boolean isOnline = NetworkMonitor.getInstance(this.getApplicationContext()).isOnline();
        if (!isOnline) {
            loadingDataSnackBar.dismiss();
            errorDataLoadingSnackBar.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        todayMeetings.clear();
        mAdapter.notifyDataSetChanged();
        loadingDataSnackBar.show();

        meetingsReference.addValueEventListener(meetingsValueEventListener);
        meetingsReference.orderByChild(ConstantValues.MEETING_ORDER).addChildEventListener(meetingsChildEventListener);
        optimizeReference.addChildEventListener(optimizeEventListener);
        updateReference.addChildEventListener(updateEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        meetingsReference.removeEventListener(meetingsValueEventListener);
        meetingsReference.removeEventListener(meetingsChildEventListener);
        optimizeReference.removeEventListener(optimizeEventListener);
        updateReference.removeEventListener(updateEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetings_option: {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.isCalledFromMainActivity();
                newFragment.show(getSupportFragmentManager(), TAG);
                break;
            }
            case R.id.clients_option: {
                Intent intent = new Intent(this, Clients.class);
                startActivity(intent);
                break;
            }
            case R.id.optimize_option: {
                sendOptimizeRequest();
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


        final RecyclerView todayMeetingsView = findViewById(R.id.meeting_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        todayMeetingsView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(todayMeetingsView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider));
        todayMeetingsView.addItemDecoration(dividerItemDecoration);
        mAdapter = new MeetingsAdapter(todayMeetings, getSupportFragmentManager(), meetingsReference, new MeetingsAdapter.MeetingActionListener() {
            @Override
            public void onItemRemoveRequested(final int position) {
                final Meeting deletedMeeting = todayMeetings.get(position);

                Snackbar undo = Snackbar.make(todayMeetingsView, position == 0 ? "Zadanie zakńczone" : "Usunięto spotkanie", Snackbar.LENGTH_LONG)
                        .setAction("COFNIJ", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                todayMeetings.add(position, deletedMeeting);
                                meetingsToDelete.remove(deletedMeeting);
                                mAdapter.notifyItemInserted(position);
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {

                                if (event != DISMISS_EVENT_ACTION && null != deletedMeeting.getPushId() && null != meetingsReference) {
                                    meetingsReference.child(deletedMeeting.getPushId()).removeValue();
                                }

                                super.onDismissed(transientBottomBar, event);
                            }
                        });
                undo.show();
                meetingsToDelete.add(deletedMeeting);
                todayMeetings.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onNavRequested(String address) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }
        );
        ItemTouchHelper.Callback callback = new MeetingItemTouchHelperCallback(mAdapter, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(todayMeetingsView);
        todayMeetingsView.setAdapter(mAdapter);
    }

    private String getFormattedDate() {
        String day;
        String month;
        String year;

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("data") && !getIntent().getStringExtra("data").equals(19_08_2018) ) {
            LinearLayout notToday = findViewById(R.id.notTodayBar);
            notToday.setVisibility(View.VISIBLE);
            return getIntent().getStringExtra("data");
        } else {
            Calendar calendar = Calendar.getInstance();
            day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
            month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);// because January is 0
            year = String.format("%02d", calendar.get(Calendar.YEAR));
            return day + "_" + month + "_" + year;
        }


    }

    private void sendOptimizeRequest() {
        Map<String, Object> map = new HashMap<>();
        for (Meeting meeting :
                todayMeetings) {
            map.put(meeting.getPushId(), meeting.toMap());
        }

        meetingsReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String key = meetingsReference.getKey();
                DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("requests").child(key);
                requests.setValue(true);
            }
        });

    }


    private ChildEventListener meetingsChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            handleSnackBarsAfterGettingFirebaseData();
            Meeting meeting = dataSnapshot.getValue(Meeting.class);
            todayMeetings.add(meeting);
            mAdapter.notifyItemInserted(meeting.getMeetingOrder());
            noDataTW.setVisibility(todayMeetings.isEmpty() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Meeting meeting = dataSnapshot.getValue(Meeting.class);
            todayMeetings.set(todayMeetings.indexOf(meeting), meeting);
            Collections.sort(todayMeetings);
            mAdapter.notifyDataSetChanged();
            FirebaseDatabase.getInstance().getReference().child("update").child(meetingsReference.getKey()).setValue(true);
            handleSnackBarsAfterGettingFirebaseData();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            handleSnackBarsAfterGettingFirebaseData();
            Meeting meeting = dataSnapshot.getValue(Meeting.class);
            int index = todayMeetings.indexOf(meeting);
            todayMeetings.remove(meeting);
            mAdapter.notifyItemRemoved(index);
            noDataTW.setVisibility(todayMeetings.isEmpty() ? View.VISIBLE : View.GONE);

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            handleSnackBarsAfterGettingFirebaseData();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            errorDataLoadingSnackBar.show();
            loadingDataSnackBar.dismiss();
        }
    };


    private ValueEventListener meetingsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            handleSnackBarsAfterGettingFirebaseData();
            noDataTW.setVisibility(todayMeetings.isEmpty() ? View.VISIBLE : View.GONE);

            boolean shouldShowOptimizationDialog = false;
            int delayedCounter = 0;
            for (DataSnapshot meeting : dataSnapshot.getChildren()) {
                Meeting tmp = meeting.getValue(Meeting.class);
                if (tmp.getLatestTimePossible() < tmp.getPlanedTimeOfVisit()) {
                    delayedCounter++;
                }
                if (tmp.getMeetingOrder() == -1) {
                    shouldShowOptimizationDialog = true;
                    break;
                }
            }
            if (shouldShowOptimizationDialog) {
                OptimizeNewPointsDialog dialog = new OptimizeNewPointsDialog();
                dialog.setRequests(optimizeReference.child(meetingsReference.getKey()));
                dialog.show(getSupportFragmentManager(), "TAG");
            } else if (delayedCounter > 0) {
                DelayedClientsDialog dialog = new DelayedClientsDialog();
                dialog.setCounter(delayedCounter);
                dialog.show(getSupportFragmentManager(), "TAG");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            errorDataLoadingSnackBar.show();
            loadingDataSnackBar.dismiss();
        }
    };


    private ChildEventListener updateEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.getKey().equals(meetingsReference.getKey()))
                recalculatingSnackBar.show();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getKey().equals(meetingsReference.getKey()))
                recalculatingSnackBar.dismiss();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            errorDataLoadingSnackBar.show();
        }
    };


    private ChildEventListener optimizeEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.getKey().equals(meetingsReference.getKey())) optimizingSnackBar.show();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getKey().equals(meetingsReference.getKey()))
                optimizingSnackBar.dismiss();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            errorDataLoadingSnackBar.show();
        }
    };


    private void handleSnackBarsAfterGettingFirebaseData() {
        errorDataLoadingSnackBar.dismiss();
        loadingDataSnackBar.dismiss();
    }

}



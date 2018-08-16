package com.bogucki.router.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bogucki.router.R;
import com.bogucki.router.adapters.MeetingItemTouchHelperCallback;
import com.bogucki.router.adapters.MeetingsAdapter;
import com.bogucki.router.dialogs.DatePickerFragment;
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
    private DatabaseReference meetingsReference;
    MeetingsAdapter mAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isOptimizationStarted = false;
    private FloatingActionButton addMeetingButton;
    private TextView noDataTW;
    private ScheduledThreadPoolExecutor optimizationExecutor = new ScheduledThreadPoolExecutor(2);

    Snackbar optimizingSnackBar;
    Snackbar errorDataLoadingSnackBar;
    Snackbar loadingDataSnackBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        noDataTW = findViewById(R.id.noDataInfoView);
        addMeetingButton = findViewById(R.id.add_meeting);
        setSupportActionBar(toolbar);
        attachFireBaseAdapter();

        meetingsReference.addValueEventListener(meetingsValueEventListener);

        loadingDataSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.loading_data_snackbar, Snackbar.LENGTH_INDEFINITE);
        errorDataLoadingSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.error_getting_data, Snackbar.LENGTH_INDEFINITE);
        optimizingSnackBar = Snackbar.make(findViewById(R.id.mainActivity), R.string.optimizing_started, Snackbar.LENGTH_INDEFINITE);
        View sbErrorView = errorDataLoadingSnackBar.getView();
        sbErrorView.setBackgroundColor(getColor(R.color.colorMistake));

        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadingDataSnackBar.show();

        boolean isOnline = NetworkMonitor.getInstance(this.getApplicationContext()).isOnline();
        if (!isOnline) {
            loadingDataSnackBar.dismiss();
            errorDataLoadingSnackBar.show();
        }
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
        RecyclerView todayMeetingsView = findViewById(R.id.meeting_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        todayMeetingsView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(todayMeetingsView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider));
        todayMeetingsView.addItemDecoration(dividerItemDecoration);
        mAdapter = new MeetingsAdapter(todayMeetings, getSupportFragmentManager(), meetingsReference);
        ItemTouchHelper.Callback callback = new MeetingItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(todayMeetingsView);
        todayMeetingsView.setAdapter(mAdapter);
    }

    private String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);// because January is 0
        String year = String.format("%02d", calendar.get(Calendar.YEAR));

        return day + "_" + month + "_" + year;
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
            mAdapter.notifyDataSetChanged();
            boolean shouldShowOptimizationDialog = false;
            for (DataSnapshot meeting : dataSnapshot.getChildren()) {
                Meeting tmp = meeting.getValue(Meeting.class);
                if (tmp.getMeetingOrder() == -1) {
                    shouldShowOptimizationDialog = true;
                    break;
                }
            }
            meetingsReference.orderByChild(ConstantValues.MEETING_ORDER).addChildEventListener(meetingsChildEventListener);
            meetingsReference.removeEventListener(this);
            if (shouldShowOptimizationDialog) {
                OptimizeNewPointsDialog dialog = new OptimizeNewPointsDialog();
                dialog.show(getSupportFragmentManager(), "TAG");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            errorDataLoadingSnackBar.show();
            loadingDataSnackBar.dismiss();
        }
    };

    private void handleSnackBarsAfterGettingFirebaseData() {
        errorDataLoadingSnackBar.dismiss();
        loadingDataSnackBar.dismiss();
    }


    private void showRouteInGoogleMaps() {
        String base = "https://www.google.com/maps/dir/?api=1&origin=" + todayMeetings.get(0).getAddress().replaceAll(" ", "%20") +
                "&destination=" + todayMeetings.get(todayMeetings.size() - 1).getAddress().replaceAll(" ", "%20") +
                "&waypoints=";

        for (int i = 1; i < todayMeetings.size() - 1; i++) {
            String address = todayMeetings.get(i).getAddress().replaceAll(" ", "%20");
            base += address + "|";
        }

        base += "&travelmode=driving";
        Uri launchMaps = Uri.parse(base);
        Intent intent = new Intent(Intent.ACTION_VIEW, launchMaps);
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, launchMaps);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
}



package com.bogucki.router.adapters;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogucki.router.activities.MeetingHolder;
import com.bogucki.router.dialogs.AddNewOrEditMeetingDialog;
import com.bogucki.router.dialogs.ConfirmRemovalDialog;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.utils.ConstantValues;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bogucki.router.R.layout.route_item;

/**
 * Created by Michal Bogucki
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = MeetingsAdapter.class.getSimpleName();
    private ArrayList<Meeting> meetings;
    private FragmentManager fragmentManager;
    private DatabaseReference meetingsReference;
    private MeetingActionListener listener;

    public MeetingsAdapter(ArrayList<Meeting> meetings, FragmentManager fragmentManager, DatabaseReference meetingsReference, MeetingActionListener listener) {
        this.meetings = meetings;
        this.fragmentManager = fragmentManager;
        this.meetingsReference = meetingsReference;
        this.listener = listener;
    }




    @Override
    public MeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(route_item, parent, false);
        return new MeetingHolder(v);
    }

    @Override
    public void onBindViewHolder(MeetingHolder holder, int position) {
        final Meeting meeting = meetings.get(position);
        holder.setClient(meeting.getClient());
        holder.setAddress(meeting.getAddress());
        holder.setReason(meeting.getReason());
        holder.setTimes(meeting.getEarliestTimePossible(), meeting.getLatestTimePossible(), meeting.getPlanedTimeOfVisit());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AddNewOrEditMeetingDialog();
                Bundle args = new Bundle();
                args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY, ConstantValues.EDIT_MEETING_BUNDLE_VALUE);
                args.putString(ConstantValues.MEETING_ID_BUNDLE_KEY, meeting.getPushId());
                args.putString(ConstantValues.CLIENT_NAME_BUNDLE_KEY, meeting.getClient());
                args.putString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY, meeting.getAddress());
                args.putString(ConstantValues.MEETING_REASON_BUNDLE_KEY, meeting.getReason());
                args.putString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY, ConstantValues.MEETINGS_FIREBASE);
                args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, meetingsReference.getKey());
                args.putInt(ConstantValues.MEETING_ORDER, meeting.getMeetingOrder());
                args.putLong(ConstantValues.LATEST_TIME_BUNDLE_KEY, meeting.getLatestTimePossible());
                args.putLong(ConstantValues.EARLIEST_TIME_BUNDLE_KEY, meeting.getEarliestTimePossible());
                args.putInt(ConstantValues.MEETING_ORDER, meeting.getMeetingOrder());
                dialogFragment.setArguments(args);
                dialogFragment.show(fragmentManager, TAG);
            }
        });


    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                int tmp = meetings.get(i).getMeetingOrder();
                meetings.get(i).setMeetingOrder(meetings.get(i + 1).getMeetingOrder());
                meetings.get(i + 1).setMeetingOrder(tmp);
                Collections.swap(meetings, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                int tmp = meetings.get(i).getMeetingOrder();
                meetings.get(i).setMeetingOrder(meetings.get(i - 1).getMeetingOrder());
                meetings.get(i - 1).setMeetingOrder(tmp);
                Collections.swap(meetings, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
//        ConfirmRemovalDialog dialogFragment = new ConfirmRemovalDialog();
//        dialogFragment.setAdapter(this);
//        Bundle args = new Bundle();
//        args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY, ConstantValues.REMOVE_MEETING_BUNDLE_VALUE);
//        args.putString(ConstantValues.MEETING_ID_BUNDLE_KEY, meetings.get(position).getPushId());
//        args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, meetingsReference.getKey());
//        dialogFragment.setArguments(args);
//        dialogFragment.show(fragmentManager, TAG);
        listener.onItemRemoveRequested(position);
    }

    @Override
    public void onItemClear() {
        Map<String, Object> map = new HashMap<>();
        for (Meeting meeting : meetings) {
            map.put(meeting.getPushId(), meeting.toMap());
        }

        meetingsReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String key = meetingsReference.getKey();
                DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("update").child(key);
                requests.setValue(true);
            }
        });
    }



    public interface MeetingActionListener{
        void onItemRemoveRequested(final int position);
    }
}

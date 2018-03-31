package com.bogucki.router.adapters;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogucki.router.activities.MainActivity;
import com.bogucki.router.activities.MeetingHolder;
import com.bogucki.router.dialogs.ChooseActionForMeeting;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.utils.ConstantValues;

import java.util.ArrayList;
import java.util.Collections;

import static com.bogucki.router.R.layout.meeting_list_item;

/**
 * Created by Michal Bogucki
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = MeetingsAdapter.class.getSimpleName();
    private ArrayList<Meeting> meetings;
    private FragmentManager fragmentManager;

    public MeetingsAdapter(ArrayList<Meeting> meetings, FragmentManager fragmentManager) {
        this.meetings = meetings;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public MeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(meeting_list_item, parent, false);
        return new MeetingHolder(v);
    }

    @Override
    public void onBindViewHolder(MeetingHolder holder, int position) {
        final Meeting meeting = meetings.get(position);
        holder.setClient(meeting.getClient());
        holder.setAddress(meeting.getAddress());
        holder.setReason(meeting.getReason());
        holder.setDate(meeting.getEarliestTimePossible());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new ChooseActionForMeeting();
                Bundle args = new Bundle();
                args.putString(ConstantValues.MEETING_ID_BUNDLE_KEY, meeting.getPushId());
                args.putString(ConstantValues.CLIENT_NAME_BUNDLE_KEY, meeting.getClient());
                args.putString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY, meeting.getAddress());
                args.putString(ConstantValues.MEETING_REASON_BUNDLE_KEY, meeting.getReason());
                args.putString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY, ConstantValues.MEETINGS_FIREBASE);
                args.putString(ConstantValues.MEETING_DATE_BUNDLE_KEY, "2_1_2018");
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
                Collections.swap(meetings, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(meetings, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }
}

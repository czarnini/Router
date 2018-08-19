package com.bogucki.router.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogucki.router.R;

import java.util.Calendar;

/**
 * Created by Michał Bogucki
 */

public class MeetingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView clientTV;
    private final TextView addressTV;
    private final TextView reasonTV;
    private final TextView timeWindowTV;
    private final TextView plannedArrivalTV;
    private final LinearLayout routeItem;
    private final LinearLayout navBar;
    private ItemClickListener itemClickListener;

    public MeetingHolder(View itemView, final Context context) {
        super(itemView);
        clientTV = itemView.findViewById(R.id.client_name);
        addressTV = itemView.findViewById(R.id.client_address);
        reasonTV = itemView.findViewById(R.id.meeting_reason);
        plannedArrivalTV = itemView.findViewById(R.id.planned_arrival);
        routeItem = itemView.findViewById(R.id.routeItem);
        navBar = itemView.findViewById(R.id.navBar);


        timeWindowTV = itemView.findViewById(R.id.time_window);


    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }


    public void setClient(String client) {
        clientTV.setText(client);
    }

    public void setAddress(String address) {
        addressTV.setText(address);
    }

    public void setReason(String reason) {
        reasonTV.setText(reason);
    }


    @SuppressLint("DefaultLocale")
    public void setTimes(long earliestTimePossible, long latestTimePossible, long plannedArrival) {
        Calendar calendarLate = Calendar.getInstance();
        calendarLate.setTimeInMillis(latestTimePossible);
        Calendar calendarEarly = Calendar.getInstance();
        calendarEarly.setTimeInMillis(earliestTimePossible);
        timeWindowTV.setText(String.format("%02d:%02d - %02d:%02d",
                calendarEarly.get(Calendar.HOUR_OF_DAY), calendarEarly.get(Calendar.MINUTE),
                calendarLate.get(Calendar.HOUR_OF_DAY), calendarLate.get(Calendar.MINUTE)
        ));

        Calendar calendarPlanned = Calendar.getInstance();
        calendarPlanned.setTimeInMillis(plannedArrival);

        if (plannedArrival == 0) {
            plannedArrivalTV.setText("--:--");
            routeItem.setBackgroundColor(Color.parseColor("#FFF59D"));
        } else {
            plannedArrivalTV.setText(
                    String.format("%02d:%02d",
                            calendarPlanned.get(Calendar.HOUR_OF_DAY), calendarPlanned.get(Calendar.MINUTE)
                    ));

            if (plannedArrival > latestTimePossible) {
                routeItem.setBackgroundColor(Color.parseColor("#EF9A9A"));
            } else {
                routeItem.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
        }


    }


    public LinearLayout getNavBar() {
        return navBar;
    }
}
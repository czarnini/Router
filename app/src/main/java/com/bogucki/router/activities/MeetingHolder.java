package com.bogucki.router.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bogucki.router.R;

/**
 * Created by Michał Bogucki
 */

public class MeetingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView clientTV;
    private final TextView addressTV;
    private final TextView reasonTV;
    private final TextView dateTV;
    private final TextView idTV;

    private ItemClickListener itemClickListener;

    public MeetingHolder(View itemView) {
        super(itemView);

        clientTV    = (TextView) itemView.findViewById(R.id.meeting_reason);
        dateTV      = (TextView) itemView.findViewById(R.id.client_name);
        reasonTV    = (TextView) itemView.findViewById(R.id.client_address);
        addressTV   = (TextView) itemView.findViewById(R.id.closest_term);
        idTV        = (TextView) itemView.findViewById(R.id.push_id);
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

    public void setDate(String date) {
        dateTV.setText(date);
    }

    public void setPushId(String pushId) {
        idTV.setText(pushId);
    }


}
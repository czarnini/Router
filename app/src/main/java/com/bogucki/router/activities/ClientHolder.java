package com.bogucki.router.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bogucki.router.R;

/**
 * Created by Michal Bogucki
 */

public class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nameTV;
    private final TextView addressTV;
    private final TextView idTv;

    private ItemClickListener itemClickListener;

    public ClientHolder(View itemView) {
        super(itemView);
        nameTV = (TextView) itemView.findViewById(R.id.client_name);
        addressTV = (TextView) itemView.findViewById(R.id.client_address);
        idTv = (TextView) itemView.findViewById(R.id.push_id);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }

    public void setName(String name) {
        nameTV.setText(name);
    }

    public void setAddress(String address) {
        addressTV.setText(address);
    }

    public void setPushId(String pushId) {
        idTv.setText(pushId);
    }
}
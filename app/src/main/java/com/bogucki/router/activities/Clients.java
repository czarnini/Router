package com.bogucki.router.activities;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;
import com.bogucki.router.dialogs.AddNewOrEditClientDialog;
import com.bogucki.router.dialogs.ChooseActionForClientDialog;
import com.bogucki.router.models.Client;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Clients extends AppCompatActivity {

    private RecyclerView clientsList;
    private static final String TAG = Clients.class.getSimpleName();
    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachFireBaseAdapter();
        handleFloatingButton();
    }

    //TODO -> divider 80dp transparent
    private void attachFireBaseAdapter() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        clientsList = (RecyclerView) findViewById(R.id.clientRecycler);
        clientsList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Client, ClientHolder>(Client.class, R.layout.client_list_item, ClientHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(ClientHolder viewHolder, Client model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setPushId(model.getPushID());


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        DialogFragment dialogFragment = new ChooseActionForClientDialog();

                        TextView idTV = (TextView) view.findViewById(R.id.push_id);
                        String pushId = idTV.getText().toString();

                        TextView nameTV = (TextView) view.findViewById(R.id.client_name);
                        String name = nameTV.getText().toString();

                        TextView addressTV = (TextView) view.findViewById(R.id.client_address);
                        String address = addressTV.getText().toString();

                        Bundle args = new Bundle();
                        args.putString(ConstantValues.CLIENT_ID_BUNDLE_KEY, pushId);
                        args.putString(ConstantValues.CLIENT_NAME_BUNDLE_KEY, name);
                        args.putString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY, address);
                        dialogFragment.setArguments(args);

                        dialogFragment.show(getSupportFragmentManager(), TAG);
                    }
                });

            }

        };


        clientsList.setAdapter(mAdapter);
    }


    private void handleFloatingButton() {
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY, ConstantValues.ADD_CLIENT_BUNDLE_VALUE);
                DialogFragment dialogFragment = new AddNewOrEditClientDialog();
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), TAG);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetings_option: {
                Intent intent = new Intent(this, Meetings.class);
                startActivity(intent);
                break;
            }
            case R.id.todays_route_option: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clients, menu);
        return true;
    }


    public static class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTV;
        private final TextView addressTV;
        private final TextView pushTV;

        private ItemClickListener itemClickListener;
        private String pushId;

        public ClientHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.client_name);
            addressTV = (TextView) itemView.findViewById(R.id.client_address);
            pushTV = (TextView) itemView.findViewById(R.id.push_id);
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
            pushTV.setText(pushId);
        }
    }

}

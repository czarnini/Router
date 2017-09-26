package com.bogucki.router.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;
import com.bogucki.router.dialogs.AddNewOrEditClientDialog;
import com.bogucki.router.dialogs.ChooseActionForClientDialog;
import com.bogucki.router.models.Client;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Clients extends AppCompatActivity
{

    private ListView clientsList;
    private static final String TAG = Clients.class.getSimpleName();
    private DatabaseReference databaseReference;
    FirebaseListAdapter<Client> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachFireBaseAdapter();
        attachOnListItemClickListener();
        handleFloatingButton();
    }

    private void attachFireBaseAdapter() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        clientsList = (ListView) findViewById(R.id.clients_list);
        mAdapter = new FirebaseListAdapter<Client>(this, Client.class, R.layout.client_list_item, databaseReference) {
            @Override
            protected void populateView(View view, Client client, int position) {
                TextView name = (TextView) view.findViewById(R.id.client_name);
                TextView address = (TextView) view.findViewById(R.id.client_address);
                TextView pushTV = (TextView) view.findViewById(R.id.push_id);

                name.setText(client.getName());
                address.setText(client.getAddress());
                pushTV.setText(client.getPushID());

            }
        };
        clientsList.setAdapter(mAdapter);
    }

    private void attachOnListItemClickListener() {
        clientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment dialogFragment = new ChooseActionForClientDialog();

                TextView idTV = (TextView)view.findViewById(R.id.push_id);
                String pushId = idTV.getText().toString();

                TextView nameTV = (TextView)view.findViewById(R.id.client_name);
                String name = nameTV.getText().toString();

                TextView addressTV = (TextView)view.findViewById(R.id.client_address);
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

    private void handleFloatingButton() {
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Bundle args = new Bundle();
                args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY, ConstantValues.ADD_CLIENT_BUNDLE_VALUE );
                DialogFragment dialogFragment = new AddNewOrEditClientDialog();
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), TAG);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.meetings_option:
            {
                Intent intent = new Intent(this, Meetings.class);
                startActivity(intent);
                break;
            }
            case R.id.todays_route_option:
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.clients, menu);
        return true;
    }


}

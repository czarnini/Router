package com.bogucki.router.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bogucki.router.R;
import com.bogucki.router.utils.ConstantValues;
import com.bogucki.router.dialogs.AddNewOrEditClientDialog;
import com.bogucki.router.dialogs.ChooseActionForClientDialog;
import com.bogucki.router.models.Client;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Clients extends AppCompatActivity {

    private RecyclerView clientsList;
    private static final String TAG = Clients.class.getSimpleName();
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        attachFireBaseAdapter("");
        handleFloatingButton();
    }


    //TODO -> divider 80dp transparent
    private void attachFireBaseAdapter(String name) {
        Log.d(TAG, "attachFireBaseAdapter: " + name);
        clientsList = (RecyclerView) findViewById(R.id.clientRecycler);
        clientsList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        Query query;
        if("".equals(name))
            query = databaseReference;
        else
            query = databaseReference.orderByChild("name").startAt(name).endAt(name +"\uf8ff");
        mAdapter = new FirebaseRecyclerAdapter<Client, ClientHolder>(
                Client.class, R.layout.client_list_item, ClientHolder.class, query) {
            @Override
            protected void populateViewHolder(ClientHolder viewHolder, final Client model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setAddress(model.getAddress());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        DialogFragment dialogFragment = new ChooseActionForClientDialog();

                        Bundle args = new Bundle();
                        args.putString(ConstantValues.CLIENT_ID_BUNDLE_KEY, model.getPushID());
                        args.putString(ConstantValues.CLIENT_NAME_BUNDLE_KEY, model.getName());
                        args.putString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY, model.getAddress());
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

        MenuItem searchItem = menu.findItem(R.id.search_client);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                attachFireBaseAdapter(newText);
                return false;
            }
        });
        return true;
    }



}

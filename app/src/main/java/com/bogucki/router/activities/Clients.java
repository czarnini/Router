package com.bogucki.router.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        mAdapter = new FirebaseRecyclerAdapter<Client, ClientHolder>(
                Client.class, R.layout.client_list_item, ClientHolder.class, databaseReference) {
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
        return true;
    }


    void insertClients() {
        String[] adresses = {"Janowskiego 13, Warszawa", "Wspólna 73, Warszawa", "Mielczarskiego 10, Warszawa",
                "Konduktorska 2, Warszawa", "Komorska 29/33, Warszawa", "Herbsta 4, Warszawa", "Waszyngtona 12/14, Warszawa",
                "Świętokrzyska 31/33a, Warszawa", "Berensona 12b, Warszawa", "Woronicza 50, Warszawa", "Zawiszy 5, Warszawa",
                "Kredytowa 5, Warszawa", "al. KEN 36, Warszawa", "Rondo ONZ 1, Warszawa", "Czerniakowska 22, Warszawa",
                "Trakt Brzeski 50, Warszawa", "Bruna 20, Warszawa", "Hoża 29/31, Warszawa", "Daniłowskiego 2/4, Warszawa",
                "Anielewicza 2, Warszawa", "Sosnkowskiego 11, Warszawa", "Domaniewska 31, Warszawa", "Ks. Janusza 23, Warszawa",
                "Nugat 9, Warszawa", "Gwiaździsta 15s, Warszawa", "Wojciecha Bogusławskiego 6a, Warszawa",
                "Gen. Zajączka 9, Warszawa", "Jana Pawła II 27, Warszawa", "Staffa 6, Warszawa", "Św. Wincentego 114, Warszawa",
                "Rembielińska 7, Warszawa", "Sierpińskiego 1, Warszawa", "Klaudyny 26, Warszawa", "Góralska 7, Warszawa",
                "Wojciechowskiego 18, Warszawa", "Belgradzka 6, Warszawa", "Powsińska 31, Warszawa", "Modzelewskiego 42/44, Warszawa",
                "Conrada 11, Warszawa", "Światowida 18, Warszawa", "Fieldorfa 41, Warszawa", "Wolska 71/73, Warszawa",
                "Toruńska 92, Warszawa", "Światowida 41, Warszawa", "Szolc-Rogozińskiego 1, Warszawa", "Sosnkowskiego 1c, Warszawa",
                "Patriotów 154, Warszawa", "Nerudy 1, Warszawa", "Młynarska 8/12, Warszawa", "Kwiatkowskiego 1, Warszawa",
                "Kondratowicza 39, Warszawa", "Kleszczowa 18, Warszawa", "Fieldorfa 37, Warszawa", "Czerska 4/6, Warszawa",
                "Stalowa 60/64, Warszawa", "Połczyńska 121-125, Warszawa", "Al. KEN 14, Warszawa", "Górczewska 212/226, Warszawa"};
        int i = 1;
        DatabaseReference clients = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        for (String address : adresses) {
            String pushId = clients.push().getKey();
            clients.child(pushId).setValue(new Client(pushId, String.valueOf(i), address));
            ++i;
        }
    }


}

package com.bogucki.router;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Clients extends AppCompatActivity
{
    private ListView clientsList;
    private static final String TAG = Clients.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String tmp[][] = new String[10][2];
        for (int j = 0; j < 10; ++j)
        {
            tmp[j][0] = "ROMBAPOL POLEX sp. Z.O.O";
            tmp[j][1] = "ul. Polska 14/10, 01-444 Warszawa";
        }
        clientsList = (ListView) findViewById(R.id.clients_list);
        MyListAdapter listAdapter = new MyListAdapter(tmp);


        clientsList.setAdapter(listAdapter);


        clientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: Item clicked " + position);
                DialogFragment dialogFragment = new ChooseActionForClientDialog();
                dialogFragment.show(getSupportFragmentManager(), TAG);
            }
        });


        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DialogFragment dialogFragment = new AddNewClientDialog();
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


    private class MyListAdapter extends ArrayAdapter<String[]>

    {
        private final String[][] values;

        public MyListAdapter(String[][] values)
        {
            super(Clients.this, R.layout.future_list_item, values);
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.client_list_item, parent, false);
            TextView client = (TextView) rowView.findViewById(R.id.client_name);
            TextView address = (TextView) rowView.findViewById(R.id.client_address);
            client.setText(values[position][0]);
            address.setText(values[position][1]);
            return rowView;
        }


    }
}

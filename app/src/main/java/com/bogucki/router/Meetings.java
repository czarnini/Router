package com.bogucki.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Meetings extends AppCompatActivity
{
    private ListView meetingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String tmp[][] = new String[10][4];
        for (int j = 0; j < 10; ++j)
        {
            tmp[j][0] = "Odbiór tony wiórów";
            tmp[j][1] = "ROMBAPOL POLEX sp. Z.O.O";
            tmp[j][2] = "ul. Polska 14/10, 01-444 Warszawa";
            tmp[j][3] = "Najbliższy wolny termin: 09.06.2017 19:00";
        }
        meetingsList = (ListView) findViewById(R.id.meetings_list);
        MyListAdapter listAdapter = new MyListAdapter(this, tmp);
        meetingsList.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.todays_route_option:
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.clients_option:
            {
                Intent intent = new Intent(this, Clients.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.meetings, menu);
        return true;
    }


    private class MyListAdapter extends ArrayAdapter<String[]>

    {
        private final Context context;
        private final String[][] values;

        public MyListAdapter(Context context, String[][] values)
        {
            super(context, R.layout.future_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.meeting_list_item, parent, false);


            TextView reason = (TextView) rowView.findViewById(R.id.meeting_reason);
            TextView client = (TextView) rowView.findViewById(R.id.client_address);
            TextView address = (TextView) rowView.findViewById(R.id.client_address);
            TextView term = (TextView) rowView.findViewById(R.id.closest_term);

            reason.setText(values[position][0]);
            client.setText(values[position][1]);
            address.setText(values[position][2]);
            term.setText(values[position][3]);

            return rowView;
        }
    }

}

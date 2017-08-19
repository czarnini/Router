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

public class Clients extends AppCompatActivity
{
    private ListView clientsList;

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
        MyListAdapter listAdapter = new MyListAdapter(this, tmp);


        clientsList.setAdapter(listAdapter);

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
            View rowView = inflater.inflate(R.layout.client_list_item, parent, false);
            TextView client = (TextView) rowView.findViewById(R.id.client_name);
            TextView address = (TextView) rowView.findViewById(R.id.client_address);
            client.setText(values[position][0]);
            address.setText(values[position][1]);
            return rowView;
        }
    }
}

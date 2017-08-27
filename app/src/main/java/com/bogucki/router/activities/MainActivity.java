package com.bogucki.router.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.bogucki.router.R;
import com.bogucki.router.dialogs.DatePickerFragment;

public class MainActivity extends AppCompatActivity
{
    private ListView futureMeetings;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tmp[][] = new String[10][4];
        for (int j = 0; j < 10; ++j)
        {
            tmp[j][3] = "18:30";
            tmp[j][2] = "Odbiór zamówienia";
            tmp[j][1] = "ul. Krucza 5, Warszawa";
            tmp[j][0] = "Rolex";
        }
        futureMeetings = (ListView) findViewById(R.id.list_view_future_items);
        MyListAdapter listAdapter = new MyListAdapter(this, tmp);


        futureMeetings.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.meetings_option:
            {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), TAG);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.future_list_item, parent, false);
            TextView client = (TextView) rowView.findViewById(R.id.future_client);
            TextView address = (TextView) rowView.findViewById(R.id.future_address);
            TextView reason = (TextView) rowView.findViewById(R.id.future_reason);
            TextView time = (TextView) rowView.findViewById(R.id.future_time);
            client.setText(values[position][0]);
            address.setText(values[position][1]);
            reason.setText(values[position][2]);
            time.setText(values[position][3]);
            return rowView;
        }
    }

}



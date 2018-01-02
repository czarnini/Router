package com.bogucki.router.rest;

import android.util.Log;

import com.bogucki.router.models.Meeting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Michał Bogucki
 */

public class RouterClient {
    Retrofit retrofit;
    RouterAPI routerAPI;

    public RouterClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:9000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        routerAPI = retrofit.create(RouterAPI.class);
    }

    public void optimize(final DatabaseReference todayMeetings, final OptimizationListener listener) {

        final OptimizerRequest request;
        request = new OptimizerRequest();
        final ArrayList<Meeting> meetings = new ArrayList<>();


        todayMeetings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tmp : dataSnapshot.getChildren()) {
                    meetings.add(tmp.getValue(Meeting.class));
                    request.addNewMeeting(tmp.getValue(Meeting.class));
                }
                Call<OptimizerResponse> responseCall;
                responseCall = routerAPI.optimize(request);


                responseCall.enqueue(new Callback<OptimizerResponse>() {
                    @Override
                    public void onResponse(Call<OptimizerResponse> call, Response<OptimizerResponse> response) {
                        Map<String, Object> valuesToSend = new HashMap<>();
                        ArrayList<Integer> result = response.body().getResult();
                        for (int i = 0; i < result.size(); i++) {
                            int newOrder = result.get(i);
                            meetings.get(i).setMeetingOrder(newOrder);
                            valuesToSend.put(meetings.get(i).getPushId(), meetings.get(i).toMap());
                        }
                        todayMeetings.updateChildren(valuesToSend);
                        listener.onOptimizationDone();
                    }

                    @Override
                    public void onFailure(Call<OptimizerResponse> call, Throwable t) {
                        Log.e("Router Client ", "onFailure: ", t);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Router Client", "onCancelled: ", databaseError.toException());
            }
        });

    }


}

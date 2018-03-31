package com.bogucki.router.rest;

import com.bogucki.router.models.Meeting;

import java.util.ArrayList;

/**
 * Created by Michał Bogucki
 */

class OptimizerRequest {

    private ArrayList<MiniMeeting> meetings;

    public OptimizerRequest() {
        meetings = new ArrayList<>();
    }

    public void addNewMeeting(Meeting baseMeeting){
        meetings.add(new MiniMeeting(baseMeeting.getAddress(), baseMeeting.getEarliestTimePossible(), baseMeeting.getLatestTimePossible()));
    }

    private class MiniMeeting {
        private String address;
        private long ETP;
        private long LTP;



        public MiniMeeting(String address, long ETP, long LTP) {
            this.address = address;
            this.ETP = ETP;
            this.LTP = LTP;
        }
    }

}

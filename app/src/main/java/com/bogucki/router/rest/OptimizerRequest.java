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
        meetings.add(new MiniMeeting(baseMeeting.getAddress(), baseMeeting.getEarliestTimeOfDelivery(), baseMeeting.getLatestTimeOfDelivery()));
    }

    private class MiniMeeting {
        private String address;
        private String ETP;
        private String LTP;



        public MiniMeeting(String address, String ETP, String LTP) {
            this.address = address;
            this.ETP = ETP;
            this.LTP = LTP;
        }
    }

}

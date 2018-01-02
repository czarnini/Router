package com.bogucki.router.rest;

import java.util.ArrayList;

/**
 * Created by Michał Bogucki
 */

class OptimizerResponse {
    private ArrayList<Integer> result;

    public OptimizerResponse() {
    }

    public OptimizerResponse(ArrayList<Integer> result) {

        this.result = result;
    }

    public ArrayList<Integer> getResult() {

        return result;
    }

    public void setResult(ArrayList<Integer> result) {
        this.result = result;
    }
}

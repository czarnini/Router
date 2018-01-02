package com.bogucki.router.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Michał Bogucki
 */

public interface RouterAPI {
    @POST("/optimize")
    Call<OptimizerResponse> optimize(@Body OptimizerRequest request);

}

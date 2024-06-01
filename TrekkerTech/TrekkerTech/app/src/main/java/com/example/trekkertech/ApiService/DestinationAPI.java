package com.example.trekkertech.ApiService;

import com.example.trekkertech.Database.Destination;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DestinationAPI {
    @GET("destinations")
    Call<List<Destination>> getDestinations();
    @GET("destinations")
    Call<List<Destination>> getDestinationsByContinent(@Query("continent") String continent);
    @GET("destinations")
    Call<List<Destination>> getDestinationsByName(@Query("name") String name);

}



package com.codevian.campgladiator.rest

import com.codevian.campgladiator.models.PlacesDetails
import com.codevian.campgladiator.utils.Constants.PLACES
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {


    @GET(PLACES)
    fun getPlacesDetails(@Query("lat") lat: String,
                         @Query("lon") long: String,
                         @Query("radius") radius: String): Call<PlacesDetails>

}


package com.chopas.soccerstandings.network

import com.chopas.soccerstandings.model.Games
import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("soccer_game_results.json")
    fun getSoccerGameResults() : Call<Games>
}
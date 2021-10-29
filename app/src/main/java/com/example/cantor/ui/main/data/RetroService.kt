package com.example.cantor.ui.main.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetroService {

    @GET()
    fun getDataFromAPI(@Url query : String) : Call<Data>
}
package com.dicoding.githubuserapp.data.retrofit

import com.dicoding.githubuserapp.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUsers(@Query("q") page: String): Call<GithubResponse>
}
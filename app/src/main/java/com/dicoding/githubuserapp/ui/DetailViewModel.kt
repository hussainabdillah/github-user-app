package com.dicoding.githubuserapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.response.DetailUserResponse
import com.dicoding.githubuserapp.data.response.ItemsItem
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    private fun showLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    fun setUserDetail(username: String) {
        showLoading(true)
        ApiConfig.getApiService()
            .getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        _userDetail.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun fetchFollowers(username: String) {
        showLoading(true)
        ApiConfig.getApiService()
            .getFollowers(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        if (userResponse != null) {
                            _followers.value = response.body()
                        }
                    } else {
                        Log.d("FetchFollowersError", "Error fetching followers: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    showLoading(false)
                    Log.d("FetchFollowersFailure", "Failed to fetch followers: ${t.message}")
                }
            })
    }

    fun fetchFollowing(username: String) {
        showLoading(true)
        ApiConfig.getApiService()
            .getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        if (userResponse != null) {
                            _following.value = response.body()
                        }
                    } else {
                        Log.d("FetchFollowingError", "Error fetching following: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    showLoading(false)
                    Log.d("FetchFollowingFailure", "Failed to fetch following: ${t.message}")
                }
            })
    }
}

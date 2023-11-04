package com.dicoding.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import com.dicoding.githubuserapp.data.response.GithubResponse
import com.dicoding.githubuserapp.data.response.ItemsItem
import com.dicoding.githubuserapp.ui.settings.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingsPreferences): ViewModel() {

    private val _githubUsers = MutableLiveData<List<ItemsItem>>()
    val githubUsers: LiveData<List<ItemsItem>> = _githubUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun showLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    val listUsers = MutableLiveData<ArrayList<ItemsItem>>()

    fun setSearchUsers(query: String){
        showLoading(true)
        ApiConfig.getApiService()
            .getListUsers(query)
            .enqueue(object : Callback<GithubResponse>{
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        listUsers.postValue(response.body()?.items as ArrayList<ItemsItem>?)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun getSearchUsers(): LiveData<ArrayList<ItemsItem>>{
        return listUsers
    }
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSettings().asLiveData()
    }
}

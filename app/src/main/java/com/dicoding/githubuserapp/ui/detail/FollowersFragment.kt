package com.dicoding.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.data.response.ItemsItem
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import com.dicoding.githubuserapp.databinding.FragmentFollowersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: FollowAdapter
    private var username: String? = null
    companion object {
        var ARG_USERNAME = "username"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvItemFollow
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FollowAdapter()
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : FollowAdapter.OnItemClickListener {
            override fun onItemClick(user: ItemsItem) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.ARG_USERNAME, user.login)
                startActivity(intent)
            }
        })

        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
        username?.let { fetchFollowers(it) }

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
                            adapter.submitList(userResponse)
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
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}




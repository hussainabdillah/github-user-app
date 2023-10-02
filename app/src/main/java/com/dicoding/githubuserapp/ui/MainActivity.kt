package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.response.GithubResponse
import com.dicoding.githubuserapp.data.response.ItemsItem
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.ui.favorites.FavoriteUserActivity
import com.dicoding.githubuserapp.ui.settings.ThemeSettingsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val querySearch = searchView.text.toString()
                    viewModel.setSearchUsers(querySearch)
                    searchView.hide()
                    true
                }
            searchBar.inflateMenu(R.menu.main_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.theme_setting -> {
                        // Handle klik pada menu theme_setting di sini
                        val intent = Intent(this@MainActivity, ThemeSettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.favorites_list -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubuser.layoutManager = layoutManager

        adapter = UserAdapter()
        binding.rvGithubuser.adapter = adapter

        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(user: ItemsItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        })
        viewModel.getSearchUsers().observe(this) { githubUsers ->
            adapter.submitList(githubUsers)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        showUserGitHub()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme_setting -> {
                // Handle klik pada menu theme_setting di sini
                val intent = Intent(this, ThemeSettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showUserGitHub() {
        showLoading(true)
        val apiService = ApiConfig.getApiService()
        val call = apiService.getListUsers("Arif")

        call.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val githubResponse = response.body()
                    val userList = githubResponse?.items ?: emptyList()
                    adapter.submitList(userList)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

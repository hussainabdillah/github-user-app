package com.dicoding.githubuserapp.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.database.FavoriteUser
import com.dicoding.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuserapp.ui.detail.DetailActivity
import com.dicoding.githubuserapp.ui.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var viewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Favorite User");

        adapter = FavoriteUserAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager
        binding.rvFavoriteUser.adapter= adapter

        viewModel = obtainViewModel(this@FavoriteUserActivity)
        viewModel.getAllFavoriteUser().observe(this) { userList ->
            adapter.submitList(userList)

        }

        adapter.setOnItemClickListener(object : FavoriteUserAdapter.OnItemClickListener {
            override fun onItemClick(user: FavoriteUser) {
                val intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }
}
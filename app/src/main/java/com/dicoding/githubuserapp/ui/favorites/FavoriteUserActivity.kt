package com.dicoding.githubuserapp.ui.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuserapp.ui.UserAdapter

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
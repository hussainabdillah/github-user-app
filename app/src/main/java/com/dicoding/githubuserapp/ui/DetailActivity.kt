package com.dicoding.githubuserapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.response.DetailUserResponse
import com.dicoding.githubuserapp.database.FavoriteUser
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.dicoding.githubuserapp.ui.favorites.FavoriteUserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var ivProfileDetail: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvName: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var favButton: FloatingActionButton
    private var buttonState: Boolean = false
    private var favoriteUser: FavoriteUser? = null
    private var detailUser = DetailUserResponse()

    companion object {
        var ARG_USERNAME = "username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this@DetailActivity)

        ivProfileDetail = binding.ivProfileDetail
        tvUsername = binding.tvUsername
        tvName = binding.tvName
        tvFollowers = binding.tvFollowers
        tvFollowing = binding.tvFollowing
        progressBar = binding.progressBar
        favButton = binding.fab

        favoriteUser = FavoriteUser()
        detailUser = DetailUserResponse()

        val username = intent.getStringExtra(ARG_USERNAME)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(ARG_USERNAME).toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.userDetail.observe(this) { userDetail ->
            if (userDetail != null) {
                tvUsername.text = userDetail.login
                tvName.text = userDetail.name
                tvFollowers.text = "${userDetail.followers} followers"
                tvFollowing.text = "${userDetail.following} following"

                Glide.with(this)
                    .load(userDetail.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivProfileDetail)

                showLoading(false)
            }
        }
        showLoading(true)
        viewModel.setUserDetail(username ?: "")

        viewModel.userDetail.observe(this, { })

        favButton.setOnClickListener{
            if (!buttonState) {
                buttonState = true
                favButton.setImageResource(R.drawable.ic_favorite_fill)
                insertToDatabase(detailUser)
            } else {
                buttonState = false
                favButton.setImageResource(R.drawable.ic_favorite)

            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun insertToDatabase(detailList: DetailUserResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id!!
            favoriteUser?.login = detailList.login
            favoriteUser?.htmlUrl = detailList.htmlUrl
            favoriteUser?.avatarUrl = detailList.avatarUrl
            viewModel.insert(favoriteUser as FavoriteUser)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            progressBar.visibility =  View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}

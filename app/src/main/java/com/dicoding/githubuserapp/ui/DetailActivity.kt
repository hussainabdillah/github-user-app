package com.dicoding.githubuserapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
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

        ivProfileDetail = binding.ivProfileDetail
        tvUsername = binding.tvUsername
        tvName = binding.tvName
        tvFollowers = binding.tvFollowers
        tvFollowing = binding.tvFollowing
        progressBar = binding.progressBar

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
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            progressBar.visibility =  View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}

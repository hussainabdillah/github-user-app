package com.dicoding.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.dicoding.githubuserapp.database.FavoriteUser
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.dicoding.githubuserapp.ui.ViewModelFactory
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
    private var isFavorited = false

    companion object {
        var ARG_USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val htmlUrl = intent.getStringExtra("htmlUrl")
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareText = "Check this awesome GitHub profile: $htmlUrl "
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("User Detail");


        ivProfileDetail = binding.ivProfileDetail
        tvUsername = binding.tvUsername
        tvName = binding.tvName
        tvFollowers = binding.tvFollowers
        tvFollowing = binding.tvFollowing
        progressBar = binding.progressBar
        favButton = binding.fab


        val username = intent.getStringExtra(ARG_USERNAME)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(ARG_USERNAME).toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel = obtainViewModel(this@DetailActivity)
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

        viewModel.userDetail.observe(this) { userDetail ->
            userDetail?.let {
                val login = userDetail.login ?: "hussainabdillah"
                val avatarUrl = userDetail.avatarUrl ?: "https://avatars.githubusercontent.com/u/95351829?v=4"
                val htmlUrl = userDetail.htmlUrl ?: "https://github.com/hussainabdillah"
                insertFavoriteUser(login, avatarUrl, htmlUrl)
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun insertFavoriteUser(login: String, avatarUrl: String, htmlUrl: String) {
        viewModel.isFavorited(login).observe(this) { favoriteUser ->
            isFavorited = favoriteUser != null

            if (isFavorited) {
                favButton.setImageResource(R.drawable.ic_favorite_fill)
            } else {
                favButton.setImageResource(R.drawable.ic_favorite)
            }

            favButton.setOnClickListener {
                val data = FavoriteUser(login = login, avatarUrl = avatarUrl, htmlUrl = htmlUrl)

                if (!isFavorited) {
                    viewModel.insert(data)
                    isFavorited = true
                    favButton.setImageResource(R.drawable.ic_favorite_fill)
                }
                else {
                    viewModel.delete(data)
                    isFavorited = false
                    favButton.setImageResource(R.drawable.ic_favorite)
                }
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}

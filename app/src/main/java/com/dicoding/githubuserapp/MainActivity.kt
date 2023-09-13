package com.dicoding.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvGithubUser: RecyclerView
    private val list = ArrayList<GithubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGithubUser = findViewById(R.id.rv_githubuser)
        rvGithubUser.setHasFixedSize(true)

        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvGithubUser.layoutManager = LinearLayoutManager(this)
        val listGithubUserAdapter = ListGithubUserAdapter(list)
        rvGithubUser.adapter = listGithubUserAdapter

        listGithubUserAdapter.setOnItemClickCallback(object : ListGithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(hero: GithubUser) {
        Toast.makeText(this, "User " + hero.name, Toast.LENGTH_SHORT).show()
    }
}
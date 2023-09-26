package com.dicoding.githubuserapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = FollowersFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowersFragment.ARG_USERNAME, username)
                }
                fragment
            }
            1 -> {
                val fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowingFragment.ARG_USERNAME, username)
                }
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}


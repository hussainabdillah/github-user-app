package com.dicoding.githubuserapp.ui.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuserapp.database.FavoriteUser

class FavoriteUserDiffCallback (private val oldFavoriteUserList: List<FavoriteUser>, private val newFavoriteUserList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteUserList.size

    override fun getNewListSize(): Int = newFavoriteUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUserList[oldItemPosition].id == newFavoriteUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavoriteUser = oldFavoriteUserList[oldItemPosition]
        val newFavoriteUser = newFavoriteUserList[newItemPosition]
        return oldFavoriteUser.login == newFavoriteUser.login &&  oldFavoriteUser.avatarUrl == newFavoriteUser.avatarUrl && oldFavoriteUser.htmlUrl == newFavoriteUser.htmlUrl
    }

}
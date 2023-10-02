package com.dicoding.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuserapp.database.FavoriteUser
import com.dicoding.githubuserapp.database.FavoriteUserDao
import com.dicoding.githubuserapp.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteUserRepository(application: Application) {
    private val mFavoritesDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoritesDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoritesDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoritesDao.delete(favoriteUser) }
    }

    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoritesDao.update(favoriteUser) }
    }
}
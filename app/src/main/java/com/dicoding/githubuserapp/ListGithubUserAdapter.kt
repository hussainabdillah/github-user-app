package com.dicoding.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListGithubUserAdapter(private val listUser: ArrayList<GithubUser>): RecyclerView.Adapter<ListGithubUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_github_user, parent, false)
        return ListViewHolder(view)
    }
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.ivProfile)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
        val tvName: TextView = itemView.findViewById((R.id.tv_item_name))
    }


    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, photo) = listUser[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvUsername.text = username
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }
}



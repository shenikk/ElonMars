package com.example.elonmars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class PhotoAdapter(private val dataSet: ArrayList<PhotoItem>, private val onItemClicked: (PhotoViewHolder, PhotoItem) -> Unit) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.titleText.text = currentItem.date
        holder.dateText.text = currentItem.title
        holder.configureImage(currentItem)

        onItemClicked(holder, currentItem)
    }

    override fun getItemCount(): Int = dataSet.size
}
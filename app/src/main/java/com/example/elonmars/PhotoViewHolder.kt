package com.example.elonmars

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView = view.findViewById(R.id.image)
    var titleText: TextView = view.findViewById(R.id.title)
    var dateText: TextView = view.findViewById(R.id.description)

    fun configureImage(currentItem: PhotoItem) {
        Glide.with(imageView.context)
            .load(currentItem.image)
                .centerInside()
            .into(imageView)
    }
}
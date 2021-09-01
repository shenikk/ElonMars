package com.example.elonmars.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.PhotoAdapter]
 */
class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView = view.findViewById(R.id.image)
    var titleText: TextView = view.findViewById(R.id.title)
    var dateText: TextView = view.findViewById(R.id.description)
    var starIcon: ImageView = view.findViewById(R.id.star_icon)

    fun configureImage(currentItem: PhotoItem) {
        Glide.with(imageView.context)
            .load(currentItem.image)
                .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.DATA) //FIXME?
            .into(imageView)
    }
}

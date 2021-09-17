package com.example.elonmars.presentation.gallery.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.PhotoAdapter]
 */
class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView = view.findViewById(R.id.image)
    private val titleText: TextView = view.findViewById(R.id.title)
    private val dateText: TextView = view.findViewById(R.id.description)
    private val starIcon: ImageView = view.findViewById(R.id.star_icon)
    private var progressDrawable = CircularProgressDrawable(imageView.context)

    fun configureHolder(currentItem: PhotoItem) {
        titleText.text = currentItem.date
        dateText.text = currentItem.title
        configureImage(currentItem)

        val imageResource =
            if (currentItem.isFavourite) R.drawable.ic_fav_star_selected else R.drawable.ic_fav_star
        starIcon.setImageResource(imageResource)
    }

    fun setOnStarClickListener(click: () -> Unit) {
        starIcon.setOnClickListener {
            click()
        }
    }

    private fun configureImage(currentItem: PhotoItem) {
        setupProgress()
        Glide.with(imageView.context)
            .load(currentItem.image)
            .centerInside()
            .placeholder(progressDrawable)
            .into(imageView)
    }

    private fun setupProgress() {
        progressDrawable.apply {
            centerRadius = 30f
            strokeWidth = 5f
        }.start()
    }
}

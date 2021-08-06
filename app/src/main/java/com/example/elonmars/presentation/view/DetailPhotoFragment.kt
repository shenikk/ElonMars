package com.example.elonmars.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.elonmars.R

/** Экран с детальной информацией о выбранном фото */
class DetailPhotoFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.detail_photo).apply {
            Glide.with(this)
                    .load(arguments?.getString(GalleryFragment.BUNDLE_KEY_IMAGE))
                    .centerInside()
                    .into(this)
        }
        view.findViewById<TextView>(R.id.description).apply {
            text = arguments?.getString(GalleryFragment.BUNDLE_KEY_DESCRIPTION)
        }
    }
}
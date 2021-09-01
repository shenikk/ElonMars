package com.example.elonmars.presentation.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.elonmars.R

/** Экран с детальной информацией о выбранном фото */
class DetailPhotoFragment : Fragment() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var detailPhoto: ImageView
    private var scaleFactor: Float = 1.0f

    companion object {
        private var MIN_SCALE = 0.1f
        private var MAX_SCALE = 10.0f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_photo, container, false)
        setZoom(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailPhoto = view.findViewById<ImageView>(R.id.detail_photo).apply {
            Glide.with(this)
                .load(arguments?.getString(GalleryFragment.BUNDLE_KEY_IMAGE))
                .centerInside()
                .into(this)
        }

        view.findViewById<TextView>(R.id.description).apply {
            text = arguments?.getString(GalleryFragment.BUNDLE_KEY_DESCRIPTION)
            // нужно для скролла объемных текстов
            this.movementMethod = ScrollingMovementMethod()
        }

        setScaleGestureDetector()
    }

    private fun setScaleGestureDetector() {
        scaleGestureDetector = ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector?): Boolean {
                    detector?.let {
                        scaleFactor *= detector.scaleFactor

                        scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE))

                        detailPhoto.scaleX = scaleFactor
                        detailPhoto.scaleY = scaleFactor
                    }
                    return true
                }
            })
    }

    private fun setZoom(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (scaleGestureDetector.onTouchEvent(event)) {
                    return true
                }
                return false
            }
        })
    }
}

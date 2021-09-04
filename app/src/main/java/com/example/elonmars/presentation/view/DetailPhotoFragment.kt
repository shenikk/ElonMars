package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.viewmodel.DetailPhotoViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** Экран с детальной информацией о выбранном фото */
class DetailPhotoFragment : Fragment() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var detailPhoto: ImageView
    private var scaleFactor: Float = 1.0f
    private var viewModel: DetailPhotoViewModel? = null

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
//        setZoom(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel(view.context)

        val currentItem =
            arguments?.getParcelable<PhotoItem>(GalleryFragment.BUNDLE_KEY_CURRENT_ITEM)

        detailPhoto = view.findViewById<ImageView>(R.id.detail_photo).apply {
            Glide.with(this)
                .load(currentItem?.image)
                .centerInside()
                .into(this)
        }

        view.findViewById<TextView>(R.id.description).apply {
            text = currentItem?.explanation
        }

        view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).apply {
            this.title = currentItem?.title
        }

        view.findViewById<FloatingActionButton>(R.id.detail_fab).apply {
            currentItem?.let {
                setFabIcon(it, this)

                setOnClickListener {
                    viewModel?.setFavourite(currentItem)
                    setFabIcon(currentItem, this)
                }
            }
        }

//        setScaleGestureDetector()
        setUpToolBar(view)
    }

    private fun setFabIcon(currentItem: PhotoItem, fab: FloatingActionButton) {
        val resourse = if (currentItem.isFavourite) R.drawable.ic_done else R.drawable.ic_fav_star
        fab.hide()
        fab.setImageResource(resourse)
        fab.show()
        currentItem.isFavourite = !currentItem.isFavourite
    }

    private fun createViewModel(context: Context) {
        val appComponent = MyApplication.getAppComponent(context)
        val activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()

        viewModel = ViewModelProvider(this, activityComponent.getViewModelFactory()).get(
            DetailPhotoViewModel::class.java
        )
    }

    private fun setUpToolBar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val collapsingToolbarLayout =
            view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
    }

//    private fun setScaleGestureDetector() {
//        scaleGestureDetector = ScaleGestureDetector(
//            context,
//            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//                override fun onScale(detector: ScaleGestureDetector?): Boolean {
//                    detector?.let {
//                        scaleFactor *= detector.scaleFactor
//
//                        scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE))
//
//                        detailPhoto.scaleX = scaleFactor
//                        detailPhoto.scaleY = scaleFactor
//                    }
//                    return true
//                }
//            })
//    }
//
//    private fun setZoom(view: View) {
//        view.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent): Boolean {
//                if (scaleGestureDetector.onTouchEvent(event)) {
//                    return true
//                }
//                return false
//            }
//        })
//    }
}

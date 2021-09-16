package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.enums.GalleryType
import com.example.elonmars.presentation.adapter.PhotoAdapter
import com.example.elonmars.presentation.extensions.getColorFromAttr
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.viewmodel.GalleryViewModel
import io.reactivex.plugins.RxJavaPlugins

/** Экран со списком фото */
class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noFavourites: TextView
    private var viewModel: GalleryViewModel? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var photoText: TextView
    private lateinit var favouritePhotoText: TextView
    private var galleryType: GalleryType = GalleryType.RANDOM

    companion object {
        const val PRIMARY_TITLE_SIZE = 28f
        const val SECONDARY_TITLE_SIZE = 18f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }
        recyclerView = view.findViewById(R.id.recycler)
        // чтобы не моргало при обновлении одного айтема
        recyclerView.itemAnimator = null
        progressBar = view.findViewById(R.id.progress_bar)
        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            this.setOnRefreshListener {
                viewModel?.loadDataOnForceAsync()
            }
        }
        noFavourites = view.findViewById(R.id.no_favourite_photos_text)
        setUpButtons(view)

        // Необходимо для устранения ошибки самого RxJava2 (UndeliverableException)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            logError("Exception: ${throwable.toString()}")
        }
    }

    private fun updateText(noFavourites: TextView, dataSet: List<PhotoItem>) {
        val visibility = if (dataSet.isEmpty()) View.VISIBLE else View.GONE
        noFavourites.visibility = visibility
    }

    private fun setUpAdapter(dataSet: List<PhotoItem>) {
        photoAdapter = PhotoAdapter(dataSet) { holder, currentItem ->
            holder.itemView.setOnClickListener { view ->
                findNavController().navigate(
                    GalleryFragmentDirections.actionGalleryFragmentToDetailPhotoFragment(
                        currentItem
                    )
                )
            }
            holder.setOnStarClickListener {
                viewModel?.setFavourite(currentItem)

                // обновляем звездочку после нажатия
                currentItem.isFavourite = !currentItem.isFavourite
                photoAdapter.notifyItemChanged(holder.adapterPosition)
            }
        }
    }

    private fun provideDependencies(context: Context) {
        val appComponent = MyApplication.getAppComponent(context)
        val activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()

        viewModel = ViewModelProvider(this, activityComponent.getViewModelFactory()).get(GalleryViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getPhotoItemsLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
                }
            })

            it.getFavPhotoItemsLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showFavData(list)
                }
            })

            it.getProgressLiveData().observe(viewLifecycleOwner, { t ->
                if (t != null) {
                    showProgress(t)
                }
            })

            it.getErrorLiveData().observe(viewLifecycleOwner, { error ->
                showError(error)
            })

            it.getRefreshingProgressLiveData().observe(viewLifecycleOwner, { isRefreshing ->
                showRefreshProgress(isRefreshing)
            })

            it.getContentTypeLiveData().observe(viewLifecycleOwner, { type ->
                setType(type)
            })
        }
    }

    private fun setType(contentType: Int) {
        galleryType = if (contentType == GalleryType.RANDOM.ordinal) {
            viewModel?.loadDataAsync()
            GalleryType.RANDOM
        } else {
            viewModel?.getFavouritePhotos()
            GalleryType.FAVOURITE
        }
    }

    private fun showError(throwable: Throwable) {
        logError("showError called with error = $throwable")
        showSnackbar(R.string.fragment_error_message)
    }

    private fun showProgress(isVisible: Boolean) {
        if (isVisible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showData(list: List<PhotoItem>) {
        if (GalleryType.RANDOM == galleryType) {
            setUpAdapter(list)
            recyclerView.adapter = photoAdapter
            updateText(noFavourites, list)
        }
    }

    private fun showFavData(list: List<PhotoItem>) {
        setUpAdapter(list)
        recyclerView.adapter = photoAdapter

        updateText(noFavourites, list)
    }

    private fun showRefreshProgress(isRefreshing: Boolean) {
        swipeRefresh.isRefreshing = isRefreshing
    }

    private fun setUpButtons(view: View) {
        photoText = view.findViewById<TextView>(R.id.photos).apply {
            setOnClickListener {
                this.textSize = PRIMARY_TITLE_SIZE
                this.setTextColor(this.context.getColorFromAttr(android.R.attr.textColorPrimary))
                favouritePhotoText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.grey_title
                    )
                )
                favouritePhotoText.textSize = SECONDARY_TITLE_SIZE

                swipeRefresh.isEnabled = true
                viewModel?.setContentType(GalleryType.RANDOM)
            }
        }

        favouritePhotoText = view.findViewById<TextView>(R.id.favourite).apply {
            setOnClickListener {
                this.textSize = PRIMARY_TITLE_SIZE
                photoText.textSize = SECONDARY_TITLE_SIZE
                this.setTextColor(this.context.getColorFromAttr(android.R.attr.textColorPrimary))
                photoText.setTextColor(ContextCompat.getColor(view.context, R.color.grey_title))

                swipeRefresh.isEnabled = false
                viewModel?.setContentType(GalleryType.FAVOURITE)
            }
        }
    }
}

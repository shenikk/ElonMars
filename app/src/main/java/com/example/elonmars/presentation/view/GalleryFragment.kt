package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.GalleryType
import com.example.elonmars.presentation.adapter.PhotoAdapter
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.viewmodel.GalleryViewModel

/** Экран со списком фото */
class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noFavourites: TextView
    private var viewModel: GalleryViewModel? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var stockText: TextView
    private lateinit var favouriteStockText: TextView

    companion object {
        const val BUNDLE_KEY_CURRENT_ITEM = "currentItem"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
    }

    private fun updateText(noFavourites: TextView, dataSet: ArrayList<PhotoItem>) {
        if (dataSet.isEmpty()) {
            noFavourites.visibility = View.VISIBLE
        } else {
            noFavourites.visibility = View.GONE
        }
    }

    private fun setUpAdapter(dataSet: ArrayList<PhotoItem>) {
        photoAdapter = PhotoAdapter(dataSet) { holder, currentItem ->
            holder.itemView.setOnClickListener { view ->
                val bundle = bundleOf(BUNDLE_KEY_CURRENT_ITEM to currentItem)
                view.findNavController().navigate(R.id.detail_photo_fragment, bundle)
            }
            holder.starIcon.setOnClickListener {
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
        }
    }

    private fun showError(throwable: Throwable) {
        logError("showError called with error = $throwable")
        showSnackbar(throwable.toString())
    }

    private fun showProgress(isVisible: Boolean) {
        if (isVisible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showData(list: ArrayList<PhotoItem>) {
        setUpAdapter(list)
        recyclerView.adapter = photoAdapter
        viewModel?.setContentType(GalleryType.RANDOM)

        updateText(noFavourites, list)
    }

    private fun showFavData(list: ArrayList<PhotoItem>) {
        setUpAdapter(list)
        recyclerView.adapter = photoAdapter
        viewModel?.setContentType(GalleryType.FAVOURITE)

        updateText(noFavourites, list)
    }

    private fun showRefreshProgress(isRefreshing: Boolean) {
        swipeRefresh.isRefreshing = isRefreshing
    }

    private fun setUpButtons(view: View) {
        stockText = view.findViewById<TextView>(R.id.photos).apply {
            setOnClickListener {
                this.textSize = 28f
                this.setTextColor(ContextCompat.getColor(view.context, R.color.black_title))
                favouriteStockText.setTextColor(ContextCompat.getColor(view.context, R.color.grey_title))
                favouriteStockText.textSize = 18f

                swipeRefresh.isEnabled = true
                viewModel?.loadDataAsync()
            }
        }

        favouriteStockText = view.findViewById<TextView>(R.id.favourite).apply {
            setOnClickListener {
                this.textSize = 28f
                stockText.textSize = 18f
                this.setTextColor(ContextCompat.getColor(view.context, R.color.black_title))
                stockText.setTextColor(ContextCompat.getColor(view.context, R.color.grey_title))

                swipeRefresh.isEnabled = false
                viewModel?.getFavouritePhotos()
            }
        }
    }

    // TODO delete it later
//    private fun processScrollToEnd(recyclerView: RecyclerView) {
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    setUpRetrofit(photoAdapter)
//                }
//            }
//        })
//    }
}

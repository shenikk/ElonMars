package com.example.elonmars.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.adapter.PhotoAdapter
import com.example.elonmars.presentation.viewmodel.GalleryViewModel
import com.google.android.material.snackbar.Snackbar

/** Экран со списком фото */
class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var progressBar: ProgressBar

    private val TAG = "GalleryFragment"
    private var viewModel: GalleryViewModel? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout

    companion object {
        const val BUNDLE_KEY_DESCRIPTION = "Description"
        const val BUNDLE_KEY_IMAGE = "Image"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel()
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }
        recyclerView = view.findViewById(R.id.recycler)
        progressBar = view.findViewById(R.id.progress_bar)
        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            this.setOnRefreshListener {
                viewModel?.loadDataOnForceAsync()
            }
        }
    }

    private fun setUpAdapter(dataSet: ArrayList<PhotoItem>) {
        photoAdapter = PhotoAdapter(dataSet) { holder, currentItem ->
            holder.itemView.setOnClickListener { view ->
                val bundle = bundleOf(
                    BUNDLE_KEY_DESCRIPTION to currentItem.explanation,
                        BUNDLE_KEY_IMAGE to currentItem.image)
                view.findNavController().navigate(R.id.detail_photo_fragment, bundle)
            }
        }
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getPhotoItemsLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
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
        Log.e(TAG, "showError called with error = $throwable")
        Snackbar.make(recyclerView, throwable.toString(), Snackbar.LENGTH_SHORT).show()
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
    }

    private fun showRefreshProgress(isRefreshing: Boolean) {
        swipeRefresh.isRefreshing = isRefreshing
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
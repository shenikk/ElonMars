package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.databinding.FragmentGalleryBinding
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.adapter.PhotoAdapter
import com.example.elonmars.presentation.enums.GalleryType
import com.example.elonmars.presentation.extensions.getColorFromAttr
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.viewmodel.GalleryViewModel
import io.reactivex.plugins.RxJavaPlugins

/** Экран со списком фото */
class GalleryFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private var viewModel: GalleryViewModel? = null
    private var galleryType: GalleryType = GalleryType.RANDOM

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PRIMARY_TITLE_SIZE = 28f
        const val SECONDARY_TITLE_SIZE = 18f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }

        initUI()
        setUpButtons(view)

        // Необходимо для устранения ошибки самого RxJava2 (UndeliverableException)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            logError("Exception: ${throwable.toString()}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        // чтобы не моргало при обновлении одного айтема
        binding.recycler.itemAnimator = null

        binding.swipeRefreshLayout.apply {
            this.setOnRefreshListener {
                viewModel?.loadDataOnForceAsync()
            }
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
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showData(list: List<PhotoItem>) {
        if (GalleryType.RANDOM == galleryType) {
            setUpAdapter(list)
            binding.recycler.adapter = photoAdapter
            updateText(binding.noFavouritePhotosText, list)
        }
    }

    private fun showFavData(list: List<PhotoItem>) {
        setUpAdapter(list)
        binding.recycler.adapter = photoAdapter

        updateText(binding.noFavouritePhotosText, list)
    }

    private fun showRefreshProgress(isRefreshing: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun setUpButtons(view: View) {
        binding.photos.apply {
            setOnClickListener {
                this.textSize = PRIMARY_TITLE_SIZE
                this.setTextColor(this.context.getColorFromAttr(android.R.attr.textColorPrimary))
                binding.favourite.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.grey_title
                    )
                )
                binding.favourite.textSize = SECONDARY_TITLE_SIZE

                binding.swipeRefreshLayout.isEnabled = true
                viewModel?.setContentType(GalleryType.RANDOM)
            }
        }

        binding.favourite.apply {
            setOnClickListener {
                this.textSize = PRIMARY_TITLE_SIZE
                binding.photos.textSize = SECONDARY_TITLE_SIZE
                this.setTextColor(this.context.getColorFromAttr(android.R.attr.textColorPrimary))
                binding.photos.setTextColor(ContextCompat.getColor(view.context, R.color.grey_title))

                binding.swipeRefreshLayout.isEnabled = false
                viewModel?.setContentType(GalleryType.FAVOURITE)
            }
        }
    }
}

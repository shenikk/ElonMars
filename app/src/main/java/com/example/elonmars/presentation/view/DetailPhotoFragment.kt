package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.databinding.FragmentDetailPhotoBinding
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.viewmodel.DetailPhotoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** Экран с детальной информацией о выбранном фото */
class DetailPhotoFragment : Fragment() {

    private var viewModel: DetailPhotoViewModel? = null
    private var currentItem: PhotoItem? = null
    private var _binding: FragmentDetailPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        currentItem = DetailPhotoFragmentArgs.fromBundle(args).myArgument
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel(view.context)
        initUI(view)
    }

    private fun initUI(view: View) {
        binding.detailPhoto.apply {
            Glide.with(view)
                .load(currentItem?.image)
                .centerInside()
                .into(this)
        }

        binding.description.apply {
            text = currentItem?.explanation
            if (currentItem?.explanation == "") {
                text = this.context.getString(R.string.no_description)
            }
        }

        binding.collapsingToolbar.apply {
            this.title = currentItem?.title
        }

        binding.detailFab.apply {
            currentItem?.let {
                setFabIcon(it, this)

                setOnClickListener {
                    currentItem?.let { currentItem ->
                        viewModel?.setFavourite(currentItem)
                        setFabIcon(currentItem, this)

                        val snackbarText =
                            if (currentItem.isFavourite) R.string.snackbar_message_delete
                            else R.string.snackbar_message_add
                        showSnackbar(snackbarText)
                    }
                }
            }
        }

        binding.backButton.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFabIcon(currentItem: PhotoItem, fab: FloatingActionButton) {
        val resource = if (currentItem.isFavourite) R.drawable.ic_done else R.drawable.ic_fav_star
        fab.hide()
        fab.setImageResource(resource)
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
}

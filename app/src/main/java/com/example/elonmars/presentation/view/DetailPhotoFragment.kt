package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.viewmodel.DetailPhotoViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** Экран с детальной информацией о выбранном фото */
class DetailPhotoFragment : Fragment() {
    
    private lateinit var detailPhoto: ImageView
    private var viewModel: DetailPhotoViewModel? = null

    private var currentItem: PhotoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        currentItem = DetailPhotoFragmentArgs.fromBundle(args).myArgument
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel(view.context)

        detailPhoto = view.findViewById<ImageView>(R.id.detail_photo)
            .apply {
            Glide.with(view)
                .load(currentItem?.image)
                .centerInside()
                .into(this)
        }

        view.findViewById<TextView>(R.id.description).apply {
            text = currentItem?.explanation
            if (currentItem?.explanation == "") {
                text = this.context.getString(R.string.no_description)
            }
        }

        view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).apply {
            this.title = currentItem?.title
        }

        view.findViewById<FloatingActionButton>(R.id.detail_fab).apply {
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

        view.findViewById<ImageView>(R.id.back_button).apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
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

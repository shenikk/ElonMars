package com.example.elonmars.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryFragment : Fragment() {

    private var NUMBER_OF_PHOTOS = 10

    private var dataSet: ArrayList<PhotoItem> = arrayListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    companion object {
        val BUNDLE_KEY_DESCRIPTION = "Description"
        val BUNDLE_KEY_IMAGE = "Image"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler)

        setUpRecycler(recyclerView)
        setUpRetrofit(photoAdapter)
    }

    private fun setUpRecycler(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(this@GalleryFragment.context, 2, RecyclerView.VERTICAL, false)
        setUpAdapter()
        recyclerView.adapter = photoAdapter
        processScrollToEnd(recyclerView)
    }

    private fun setUpAdapter() {
        photoAdapter = PhotoAdapter(dataSet) { holder, currentItem ->
            holder.itemView.setOnClickListener { view ->
                val bundle = bundleOf(
                    BUNDLE_KEY_DESCRIPTION to currentItem.explanation,
                        BUNDLE_KEY_IMAGE to currentItem.image)
                view.findNavController().navigate(R.id.detail_photo_fragment, bundle)
            }
        }
    }

    // TODO добавить кэширование и шиммеры при загрузке картинок
    // TODO добавить описание ко всем классам и публичным методам
    // TODO поправить архитектуру
    // TODO добавить спиннер при загрузке картинок
    private fun setUpRetrofit(photoAdapter: PhotoAdapter) {
        val call = Common.retrofit.getPhotos(API_KEY, NUMBER_OF_PHOTOS)
        call.enqueue(object : Callback<ArrayList<PhotoItem>> {
            override fun onResponse(call: Call<ArrayList<PhotoItem>>, response: Response<ArrayList<PhotoItem>>) {
                if (response.isSuccessful) {

                    response.body()?.forEach {
                        dataSet.add(PhotoItem(it.date, it.title, it.image, it.explanation))
                    }
                    photoAdapter.notifyDataSetChanged()

                    Toast.makeText(this@GalleryFragment.context, "Success!", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successfully get a list of images")
                }
            }

            override fun onFailure(call: Call<ArrayList<PhotoItem>>, t: Throwable) {
                Toast.makeText(this@GalleryFragment.context, "Fail!", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Failed to get a list of images")
            }
        })
    }

    private fun processScrollToEnd(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setUpRetrofit(photoAdapter)
                }
            }
        })
    }
}
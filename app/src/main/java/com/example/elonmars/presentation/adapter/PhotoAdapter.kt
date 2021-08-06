package com.example.elonmars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.R

/**
 * Адаптер для отображения элементов списка на главном экране.
 *
 * @param dataSet список элементов [PhotoItem]
 * @param onItemClicked лямбда с полезной нагрузкой, которая выполняется по клику на элемент адаптера
 */
class PhotoAdapter(private val dataSet: ArrayList<PhotoItem>, private val onItemClicked: (PhotoViewHolder, PhotoItem) -> Unit) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.titleText.text = currentItem.date
        holder.dateText.text = currentItem.title
        holder.configureImage(currentItem)

        onItemClicked(holder, currentItem)
    }

    override fun getItemCount(): Int = dataSet.size
}
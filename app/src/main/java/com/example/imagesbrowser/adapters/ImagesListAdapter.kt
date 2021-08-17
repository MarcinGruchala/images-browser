package com.example.imagesbrowser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesbrowser.R
import com.example.imagesbrowser.databinding.ItemImagesListBinding
import com.example.imagesbrowser.models.ImagesListResponse

class ImagesListAdapter(
    private val imagesDataArray: ImagesListResponse
) : RecyclerView.Adapter<ImagesListAdapter.ImagesListViewHolder>()  {

    class ImagesListViewHolder(
        val binding: ItemImagesListBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesListViewHolder {
        return ImagesListViewHolder(
            ItemImagesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ImagesListViewHolder,
        position: Int
    ) {
        holder.binding.tvIDNumberItem.text = holder.itemView.context.getString(
            R.string.tv_image_id,
            imagesDataArray[position].id.toInt()
        )
        Glide.with(holder.itemView).load(imagesDataArray[position].download_url).into(holder.binding.ivImage)
    }

    override fun getItemCount() = imagesDataArray.size
}
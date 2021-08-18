package com.example.imagesbrowser.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesbrowser.R
import com.example.imagesbrowser.databinding.ItemImagesListBinding
import com.example.imagesbrowser.models.ImagesListResponse
import com.example.imagesbrowser.models.ImagesListResponseItem

class ImagesListAdapter(
    private val imagesDataArray: ImagesListResponse,
    private val imagesBitmapList: List<Bitmap>,
    private val itemClickListener: (ImagesListResponseItem) -> Unit
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
        holder.binding.apply {
            tvIDNumberItem.text = holder.itemView.context.getString(
                R.string.tv_image_id,
                imagesDataArray[position].id.toInt()
            )
            ivImage.setImageBitmap(imagesBitmapList[position])
        }
        holder.itemView.setOnClickListener { itemClickListener(imagesDataArray[position]) }
    }

    override fun getItemCount() = imagesDataArray.size
}
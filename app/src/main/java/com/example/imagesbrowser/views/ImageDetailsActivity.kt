package com.example.imagesbrowser.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.imagesbrowser.R
import com.example.imagesbrowser.databinding.ActivityImageDetailsBinding
import com.example.imagesbrowser.models.remote.ImagesListResponseItem

class ImageDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
    }

    private fun setUI() {
        val item = intent.getSerializableExtra("ITEM_DETAILS") as ImagesListResponseItem
        setTextViews(item)
        setImageView(item)
    }

    private fun setTextViews(
        item: ImagesListResponseItem
    ) {
        binding.apply {
            tvImageIdDetails.text = getString(
                R.string.tv_image_id,
                item.id.toInt()
            )
            tvAuthor.text = getString(
                R.string.tvAuthor,
                item.author
            )
            tvHeight.text = getString(
                R.string.tvHeight,
                item.height
            )
            tvWidth.text = getString(
                R.string.tvWidth,
                item.width
            )
            tvUrl.text = getString(
                R.string.tvDownloadUrl,
                item.download_url
            )
        }

    }

    private fun setImageView(
        item: ImagesListResponseItem
    ) {
        val loadingDrawable = CircularProgressDrawable(this)
        loadingDrawable.strokeWidth = 10f
        loadingDrawable.centerRadius = 50f
        loadingDrawable.setColorSchemeColors(
            ContextCompat.getColor(
                this,
                R.color.purple
            )
        )
        loadingDrawable.start()
        Glide.with(this)
            .load(item.download_url)
            .placeholder(loadingDrawable)
            .into(binding.ivDetailedImage)
    }
}
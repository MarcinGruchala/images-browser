package com.example.imagesbrowser.presentation.imageDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.imagesbrowser.R
import com.example.imagesbrowser.databinding.FragmentImageDetailsBinding
import com.example.imagesbrowser.networking.model.ImagesListResponseItem
import com.example.imagesbrowser.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentImageDetailsBinding
//    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
    }

    private fun setUI() {
        val item = viewModel.imageDetails.value
        item?.let {
            setTextViews(item)
            setImageView(item)
        }
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
        val loadingDrawable = CircularProgressDrawable(requireContext())
        loadingDrawable.strokeWidth = 10f
        loadingDrawable.centerRadius = 50f
        loadingDrawable.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
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

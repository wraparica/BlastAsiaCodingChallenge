package com.example.androidcodingchallenge

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidcodingchallenge.databinding.CardPhotosBinding
import com.example.androidcodingchallenge.databinding.ItemLoadMoreBinding
import com.example.androidcodingchallenge.domain.model.Photos

class PhotosListAdapter(
    private val photosCallback: (Photos) -> Unit,
    private val loadMoreCallback: () -> Unit
) : ListAdapter<Photos, RecyclerView.ViewHolder>(PhotosDiffCallback()) {

    private var isLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
        return if (viewType == PhotosListViewType.PHOTOS.ordinal) {
            PhotosViewHolder.from(parent)
        } else {
            LoadMoreViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotosViewHolder) {
            holder.bind(getItem(position), photosCallback)
        } else if (holder is LoadMoreViewHolder) {
            holder.bind(isLoading, loadMoreCallback)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    fun getPhotosCount(): Int = itemCount - 1

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyItemChanged(itemCount - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < getPhotosCount()) {
            PhotosListViewType.PHOTOS.ordinal
        } else {
            PhotosListViewType.LOAD_MORE.ordinal
        }
    }
}

enum class PhotosListViewType {
    PHOTOS,
    LOAD_MORE
}

class PhotosViewHolder(
    private val binding: CardPhotosBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photos: Photos?, callback: (Photos) -> Unit) {
        photos ?: return
        binding.author.text = photos.author

        Log.d("downloadurl", photos.download_url.toString())
        Glide.with(binding.image)
            .load(photos.download_url)
            .centerCrop()
            .placeholder(R.drawable.ic_twotone_image_24)
            .error(R.drawable.ic_twotone_broken_image_24)
            .into(binding.image)

        binding.root.setOnClickListener { callback(photos) }
    }

    companion object {

        fun from(parent: ViewGroup) = PhotosViewHolder(
            CardPhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class LoadMoreViewHolder(
    private val binding: ItemLoadMoreBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(isLoading: Boolean, callback: () -> Unit) {
        if (isLoading) {
            setLoading()
        } else {
            setLoadMore(callback)
        }
    }

    private fun setLoadMore(callback: () -> Unit) {
        binding.text.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.root.setOnClickListener {
            setLoading()
            callback()
        }
    }

    private fun setLoading() {
        binding.text.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.root.setOnClickListener(null)
    }

    companion object {

        fun from(parent: ViewGroup) = LoadMoreViewHolder(
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class PhotosDiffCallback : DiffUtil.ItemCallback<Photos>() {
    override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean =
        oldItem == newItem
}


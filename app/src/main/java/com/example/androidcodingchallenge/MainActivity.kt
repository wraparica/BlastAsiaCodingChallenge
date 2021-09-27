package com.example.androidcodingchallenge

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidcodingchallenge.databinding.ActivityMainBinding
import com.example.androidcodingchallenge.domain.model.Photos
import com.example.androidcodingchallenge.util.Debouncer
import com.example.androidcodingchallenge.util.RecyclerViewLoadMoreScroll
import com.example.androidcodingchallenge.domain.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotosListAdapter

    private val viewModel: CodingChallengeViewModel by viewModels()
    private val debouncer = Debouncer()
    private var scrollToTop: Boolean = true
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private var isFirstLoad: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()

        if (viewModel.isConnected()) {
            refreshList()
        }

        isFirstLoad = false
    }

    private fun setupUI() {
        adapter = PhotosListAdapter(::viewPhotos, ::loadMore)
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager) { loadMore() }
        binding.recycler.addOnScrollListener(scrollListener)
        binding.recycler.adapter = adapter

        binding.refreshLayout.setOnRefreshListener { refreshList() }

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                debouncer.run {
                    scrollToTop = true
                    lifecycleScope.launch { viewModel.getPhotos(s?.toString() ?: "") }
                }
            }
        })
    }

    private fun viewPhotos(photos: Photos) {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(photos.url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun loadMore() {
        adapter.setLoading(true)
        lifecycleScope.launch { viewModel.loadMorePhotos(adapter.getPhotosCount()) }
    }

    private fun refreshList() {
        adapter.setLoading(true)
        scrollToTop = true
        lifecycleScope.launch { viewModel.refresh() }
    }

    private fun observeData() {
        viewModel.photos.observe(this) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> showLoading()
                    is Result.Success -> submitList(result.value)
                }
            }
        }
        viewModel.eventNetworkError.observe(this) {
            it?.let {
                stopLoading()
                if (adapter.getPhotosCount() == 0) {
                    showNetworkError()
                } else {
                    Snackbar.make(
                        binding.root,
                        R.string.no_internet_try_again,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                viewModel.onDoneShowNetworkError()
            }
        }
        viewModel.eventServerError.observe(this) {
            it?.let {
                stopLoading()
                Snackbar.make(
                    binding.root,
                    R.string.something_went_wrong_try_again,
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.onDoneShowServerError()
            }
        }
    }

    private fun stopLoading() {
        binding.refreshLayout.isRefreshing = false
        adapter.setLoading(false)
    }

    private fun showEmptyState(@DrawableRes drawableId: Int, @StringRes stringId: Int) {
        binding.recycler.visibility = View.GONE
        binding.emptyState.visibility = View.VISIBLE
        binding.emptyStateImage.setImageResource(drawableId)
        binding.emptyStateText.text = getString(stringId)
    }

    private fun showList() {
        binding.recycler.visibility = View.VISIBLE
        binding.emptyState.visibility = View.GONE
    }

    private fun showLoading() {
        binding.refreshLayout.isRefreshing = true
        showEmptyState(R.drawable.ic_baseline_album_24, R.string.loading_photos)
    }

    private fun showNetworkError() {
        stopLoading()
        showEmptyState(R.drawable.ic_round_wifi_off_24, R.string.no_internet_try_again)
    }

    private fun submitList(photos: List<Photos>) {
        stopLoading()
        binding.recycler.visibility = View.GONE
        adapter.submitList(photos) {
            if (scrollToTop && photos.isNotEmpty()) {
                binding.recycler.scrollToPosition(0)
                scrollToTop = false
            }
        }
        scrollListener.setLoaded()
        if (photos.isEmpty()) {
            showEmptyState(R.drawable.ic_baseline_search_24, R.string.no_results)
        } else {
            showList()
        }
    }
}

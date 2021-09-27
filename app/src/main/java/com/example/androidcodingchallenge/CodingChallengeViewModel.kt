package com.example.androidcodingchallenge

import androidx.lifecycle.*
import com.example.androidcodingchallenge.domain.model.Photos
import com.example.androidcodingchallenge.domain.repository.ConnectivityRepository
import com.example.androidcodingchallenge.domain.repository.DispatcherRepository
import com.example.androidcodingchallenge.domain.repository.PhotosRepository
import com.example.androidcodingchallenge.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class CodingChallengeViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val dispatchers: DispatcherRepository
) : ViewModel() {

    private var _photos = MediatorLiveData<Result<List<Photos>>>()
    val photos: LiveData<Result<List<Photos>>>
        get() = _photos

    private val _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _eventServerError = MutableLiveData<Boolean>()
    val eventServerError: LiveData<Boolean>
        get() = _eventServerError

    private var _photosDataSource: LiveData<Result<List<Photos>>> = MutableLiveData()

    init {
        _photos.postValue(Result.Loading())
        if (!connectivityRepository.isConnectedToNetwork()) {
            onNetworkError()
        }
    }

    suspend fun getPhotos(filter: String?) = withContext(dispatchers.getMain()) {
        _photos.removeSource(_photosDataSource)
        _photosDataSource = photosRepository.fetchPhotos(filter).asLiveData()
        _photos.addSource(_photosDataSource) { _photos.postValue(it) }
    }

    fun isConnected() = connectivityRepository.isConnectedToNetwork()

    suspend fun refresh() = withContext(dispatchers.getIO()) {
        if (!connectivityRepository.isConnectedToNetwork()) {
            onNetworkError()
        } else {
            val photos = _photos.value
            if (photos !is Result.Success || photos.value.isEmpty()) {
                _photos.postValue(Result.Loading())
            }

            val result = photosRepository.refreshPhotos()
            if (result is Result.Failure) onError()

            getPhotos(null)
        }
    }

    suspend fun loadMorePhotos(position: Int) = withContext(dispatchers.getIO()) {
        try {
            photosRepository.fetchPhotos(position)
        } catch (e: Exception) {
            onError()
        }
    }

    private fun onError() {
        if (connectivityRepository.isConnectedToNetwork()) {
            onServerError()
        } else {
            onNetworkError()
        }
    }

    private fun onServerError() = _eventServerError.postValue(true)

    fun onDoneShowServerError() = _eventServerError.postValue(null)

    private fun onNetworkError() = _eventNetworkError.postValue(true)

    fun onDoneShowNetworkError() = _eventNetworkError.postValue(null)
}
package com.photowalk.android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photowalk.android.domain.Photo
import com.photowalk.android.networking.WebClient
import com.photowalk.android.util.Constants.Companion.FLICKR_API_KEY
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_ACCURACY
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_FORMAT
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_METHOD
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_PAGE
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_PER_PAGE
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_RADIUS
import com.photowalk.android.util.Constants.Companion.FLICKR_SEARCH_TAGS
import kotlinx.coroutines.launch
import java.util.ArrayList

class PhotosViewModel : ViewModel() {
    private val mutablePhotosListLiveData = MutableLiveData<List<Photo>>()
    private val photosListLiveData: LiveData<List<Photo>> = mutablePhotosListLiveData

    var photosAdapter = PhotosAdapter()
    var photosListFinal: ArrayList<Photo> = arrayListOf<Photo>()

    fun loadPhotos(lat: Double, lon: Double): LiveData<List<Photo>> {
        viewModelScope.launch {
            //the call to the api with parameters
            val searchResponse = WebClient.client.fetchImages(
                FLICKR_SEARCH_METHOD,
                FLICKR_SEARCH_FORMAT,
                FLICKR_SEARCH_TAGS,
                1,
                lat,
                lon,
                FLICKR_SEARCH_RADIUS,
                FLICKR_SEARCH_PER_PAGE,
                FLICKR_SEARCH_PAGE,
                FLICKR_SEARCH_ACCURACY,
                FLICKR_API_KEY
            )
            //api response
            val photosList = searchResponse.photos.photo.map { photo ->
                Photo(
                    id = photo.id,
                    url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                    title = photo.title
                )
            }
            //check for empty return as some regions might not have images in the radius set
            //maybe store some of the older images and add from that list in case the return is empty
            if (photosList.isNotEmpty()) {
                if (photosListFinal.isEmpty()) {
                    //initial list, first image added
                    photosListFinal.add(photosList[0])
                } else {
                    for (photo in photosList) {
                        //check if the id of the photo is in the list as we don't want duplicates
                        if (!photosListFinal.any { it -> it.id == photo.id }) {
                            photosListFinal.add(0, photo)
                            break
                        }
                    }
                }
                mutablePhotosListLiveData.postValue(photosListFinal)
            }
        }
        return photosListLiveData
    }
}

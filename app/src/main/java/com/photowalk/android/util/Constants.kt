package com.photowalk.android.util


class Constants{
    companion object{
        const val FLICKR_API_KEY = "31326043474cf8bc55a21ca8808f7c07"
        const val FLICKR_SEARCH_RADIUS = 2 //radius of search in km around the GPS location
        const val FLICKR_SEARCH_TAGS = "landscape" //type of images with tags we want to see
        const val FLICKR_SEARCH_FORMAT = "json" //return type format
        const val FLICKR_SEARCH_METHOD = "flickr.photos.search"
        const val FLICKR_SEARCH_PER_PAGE = 100 //number of results per return page
        const val FLICKR_SEARCH_PAGE = 1 //number of pages returned
        const val FLICKR_SEARCH_ACCURACY = 6 //World level is 1; Country is ~3; Region is ~6; City is ~11; Street is ~16
    }
}
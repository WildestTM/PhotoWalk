package com.photowalk.android.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.photowalk.android.R
import com.photowalk.android.domain.Photo
import com.photowalk.android.util.LocationHelper
import kotlinx.android.synthetic.main.activity_photos.*

class PhotosActivity : AppCompatActivity() {

    private val photosViewModel: PhotosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        // Set the toolbar as support action bar
        setSupportActionBar(toolbar)

        // Now get the support action bar
        val actionBar = supportActionBar

        // Set toolbar title/app title
        actionBar!!.title = getString(R.string.app_name)

        // Set action bar elevation
        actionBar.elevation = 4.0F

        photosRecyclerView.adapter = photosViewModel.photosAdapter
        photosRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_run -> {
                if(item.title.equals("Start")) {
                    item.title = "Stop"
                    startRecording()
                } else if(item.title.equals("Stop")) {
                    item.title = "Start"
                    LocationHelper().stopListeningUserLocation(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Starts the GPS tracking
     */
    private fun startRecording() {
        LocationHelper().startListeningUserLocation(
            this,
            object : LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    photosViewModel.loadPhotos(location.latitude, location.longitude)
                        .observe(this@PhotosActivity,
                            Observer<List<Photo>> { list ->
                                with(photosViewModel.photosAdapter) {
                                    photos.clear()
                                    photos.addAll(list)
                                    notifyDataSetChanged()
                                }
                            })
                    Log.d("Location", "" + location.latitude + "," + location.longitude)
                }
            })
    }
}

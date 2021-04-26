package com.example.buseettask.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buseettask.base.BaseActivity
import com.example.buseettask.databinding.ActivityMainBinding
import com.example.buseettask.network.ApiResponse
import com.example.buseettask.network.Venues
import com.example.buseettask.utils.CustomDialog.showDialog
import com.example.buseettask.utils.ProgressLoading
import com.example.buseettask.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : BaseActivity(), OnMapReadyCallback, RestaurantAdapter.ClickListener {
    private lateinit var binding: ActivityMainBinding
    var latitiude: Double = 0.0
    var longtiude: Double = 0.0
    val TAG = "MainActivity"
    private var googleMap: GoogleMap? = null
    private lateinit var currentLoction: LatLng
    private val viewModel: MainViewModel by viewModels()
   private lateinit var restaurantList: List<Venues>

    private lateinit var  adapter :RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        initList()
        observeViewModel()
    }
    private fun initList()
    {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter  = RestaurantAdapter(this, this, listOf())
        binding.orderRV.layoutManager = layoutManager
        binding.orderRV.adapter = adapter

    }
    private fun observeViewModel(){
        viewModel.status.observe(this, observerStatus)

    }
    private val observerStatus = Observer<Status<ApiResponse>> {
        when (it) {
            is Status.Loading -> onLoading()
            is Status.Error -> onError(it)
            is Status.Success -> onSuccess(it.data)
        }
    }
    override fun getLayoutResource(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onNewLocation(lat: Double, long: Double) {
        latitiude=lat
        longtiude=long
        currentLoction = LatLng(latitiude, longtiude)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoction, 16f))

    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            googleMap?.setOnCameraIdleListener() {
                latitiude= googleMap?.cameraPosition?.target?.latitude!!
                longtiude= googleMap?.cameraPosition?.target?.longitude!!

                viewModel.getRestaurantsList(50, latitiude, longtiude)

            }
        }
    }

    private fun drawCurrentMarker() {
        if (googleMap == null) return
        googleMap!!.clear()
        googleMap!!.addMarker(
                MarkerOptions()
                        .position(currentLoction)
                        .title("your location")

        )
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoction, 16f))

    }

    override fun onItemClick(item: Venues) {
        binding.restaurantItem.root.visibility = View.VISIBLE
        binding.restaurantItem.TVRetaurantname.text=item.name
        binding.restaurantItem.TVAddress.text=item.location.address
        binding.restaurantItem.IVDirection.setOnClickListener {
            val uri = "google.navigation:q= ${item.location.lat},${item.location.lng}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            this.startActivity(intent)
        }
    }
    private fun onLoading() {
        ProgressLoading.show(this)
        Log.e("MainActivity", "onLoading")
    }

    private fun onSuccess(data: ApiResponse) {
        ProgressLoading.dismiss()


        restaurantList=data.response.venues.sortedBy {
            it.distance
        }
        adapter.submitList(restaurantList)
        Log.e("MainActivity", data.response.venues.size.toString())

        Log.e("MainActivity", "onSuccess")

    }

    private fun onError(error: Status.Error) {
        Log.e("MainActivity", "onError")

        binding.orderRV .isVisible = false
        ProgressLoading.dismiss()
        val message = error.getMessage(this ?: return)
        this
            .showDialog(
                    message = message.message,
                    iconID = message.iconRes,
                    colorID = message.colorId
            )
    }

    override fun onBackPressed() {
        if(binding.restaurantItem.root.visibility==View.VISIBLE)
        {
            googleMap!!.clear()
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoction, 16f))
            binding.restaurantItem.root.visibility=View.GONE
        }
        else
        {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle("close your app")
            alertDialog.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            alertDialog.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which -> })
            alertDialog.show()
        }



    }
}
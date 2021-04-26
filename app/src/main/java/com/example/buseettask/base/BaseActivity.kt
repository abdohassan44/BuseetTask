package com.example.buseettask.base

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.buseettask.R
import com.example.buseettask.utils.GPSDialog
import com.example.buseettask.utils.*
import com.example.buseettask.utils.CustomDialog.showDialog
import com.example.buseettask.utils.PermissionsUtil.isPermissionGranted
import com.example.buseettask.utils.PermissionsUtil.requestPermission
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


abstract class BaseActivity : AppCompatActivity(), InternetConnectionListener,
    ConnectivityReceiver.ConnectivityReceiverListener {

    private val TAG = "BaseActivity"

    private var locationManager: LocationManager? = null

    private var mLocationListener: LocationListener? = null

    private var currentLat = 0.0
    private var currentLng = 0.0

   // private val locationInterval = 25L

    private var mLastClickTime = System.currentTimeMillis()
    private val CLICK_TIME_INTERVAL: Long = 300

    private val locationRequest: LocationRequest? by lazy {
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            val newLocation = locationResult?.lastLocation
            val lat = newLocation?.latitude
            val lon = newLocation?.longitude

            Log.e(TAG, "locationCallback result lat $lat lon $lon")
            if (lat != null && lon != null)
                onNewLocation(lat, lon)

        }
    }

    private fun startLocationService() {
        try {
            mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        } catch (ex: SecurityException) {
            Log.e(TAG, "startLocationService SecurityException ${ex.message}")

        }
    }

    private val mFusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val locationManger: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        startLocationService()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        registerReceiver(gpsSwitchStateReceiver, filter)
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        enableMyLocation()


    } // fun of onCreate


    protected abstract fun getLayoutResource(): View


    @SuppressLint("MissingPermission")
    protected fun Activity.enableMyLocation() {
        Log.e(TAG, "enableMyLocation ")
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            GPSDialog.show(this)
        else if (isPermissionGranted(this)) {
            Log.e(TAG, "isPermissionGranted")
            try {

                mFusedLocationProviderClient.lastLocation?.addOnSuccessListener(this) { location: Location? ->

                    val lat = location?.latitude
                    val lon = location?.longitude
                    Log.e(
                        TAG,
                        "GPS result lat $lat lon $lon"
                    )

                    if (lat != null && lon != null) {
                        GPSDialog.dismiss()
                        onNewLocation(lat, lon)
                    } else
                        requestOnline(activity = this)
                }
            } catch (e: Exception) {
                requestOnline(activity = this)
                Log.e(TAG, "location error : $e ")

            }
        } else
            this.requestPermission(onPermissionGranted = {
                this.enableMyLocation()
                Log.e(TAG, "requestPermission")
            }, onPermissionDenied = {
                showDialog(
                    message = getString(R.string.please_enable_permissions),
                    iconID = CustomDialog.errorID,
                    colorID = R.color.red,
                    onDismiss = {
                        enableMyLocation()
                    })
            })

    }


    @SuppressLint("MissingPermission")
    private fun requestOnline(activity: Activity) {
        val networkLocation = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val lat = networkLocation?.latitude
        val lon = networkLocation?.longitude
        Log.e(
            TAG,
            "requestOnline result lat $lat lon $lon"
        )
        if (lat != null && lon != null) {
            GPSDialog.dismiss()
            onNewLocation(lat, lon)
        } else
            requestLocation(activity)

    }


    abstract fun onNewLocation(lat: Double, long: Double)

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this

    }

    //The device never recorded its location
    @TargetApi(Build.VERSION_CODES.M)
    private fun requestLocation(activity: Activity) {
        Log.e(
            TAG,
            "requestLocation"
        )

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        } else {

            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                10f,
                mLocationListener as LocationListener
            )
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                3000,
                10f,
                mLocationListener as LocationListener
            )

            locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            mLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.e(TAG, "location listener ${location.latitude}")


                    currentLat = location.latitude
                    currentLng = location.longitude
                    onNewLocation(lat = currentLat, long = currentLng)


                    if (currentLat != 0.0 || currentLng != 0.0) {

                        Log.e(TAG, "location listener remove update  $currentLat , $currentLng")

                        if (locationManager != null) {
                            locationManager?.removeUpdates(this)
                            locationManager = null
                        }

                    }

                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

                }

                override fun onProviderEnabled(provider: String) {

                }

                override fun onProviderDisabled(provider: String) {

                }

            }
        }

    } // fun of requestLocation

    override fun onConnected(status: Boolean) {
        Log.e(TAG, "onConnected : $status")
        if (!status)
            InternetConnectionDialog.show(this)
        else
            InternetConnectionDialog.dismiss()
    } // fun of onConnected

    override fun onDestroy() {
        super.onDestroy()
        stopLocationService()
    }

    private fun stopLocationService() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    private val gpsSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (isGpsEnabled || isNetworkEnabled) {

                    GPSDialog.dismiss()

                } else {
                    onResume()
                }
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return@onNetworkConnectionChanged
        }
        mLastClickTime = now
        if (!isConnected) {
            NetworkDialog.show(this)

        } else {
            thread {
                Thread.sleep(2000)
                try {
                    NetworkDialog.dismiss()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}
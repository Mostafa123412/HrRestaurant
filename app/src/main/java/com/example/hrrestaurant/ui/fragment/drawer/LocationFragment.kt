package com.example.hrrestaurant.ui.fragment.drawer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toImmutableList

@AndroidEntryPoint
class LocationFragment : Fragment() {

//    private lateinit var map: GoogleMap
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val LOCATION_PERMISSION_REQUEST_CODE = 1
//    private var targetLocation: String? = null
//    private var directionsPolyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fusedLocationClient = LocationServices
//            .getFusedLocationProviderClient(requireActivity())
//        targetLocation = LatLng(30.05244, 31.33551).toString() // replace with your desired location
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?{
//        getMapAsync(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            enableMyLocation()
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray,
//    ) {
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                enableMyLocation()
//            }
//        }
//    }

//    @SuppressLint("MissingPermission")
//    private fun enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            map.isMyLocationEnabled = true
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                location?.let {
//                    val currentLatLng: LatLng = LatLng(location.latitude, location.longitude)
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                    map.addMarker(MarkerOptions().position(currentLatLng))
//
//                    if (targetLocation != null) {
//                        val directions = DirectionsApiRequest(getGeoContext())
//                            .origin(currentLatLng.toString())
//                            .destination(targetLocation!!)
//                            .mode(TravelMode.DRIVING)
//                            .await()
//
////                        if (directions.routes.isNotEmpty()) {
////                            val route = directions.routes[0]
////                            val overviewPolyline = route.overviewPolyline
////                            val decodedPath = PolyUtil.decode(overviewPolyline.encodedPath)
////                            decodedPath.map {
////                                com.google.maps.model.LatLng(it.lat)
////                            }
////                            directionsPolyline?.remove()
////                            directionsPolyline = map.addPolyline(PolylineOptions()
////                                .addAll(decodedPath.toMutableList().asIterable()))
////                        }
//                    }
//                }
//            }
//        }
//    }

//    private fun getGeoContext(): GeoApiContext {
//        val context = GeoApiContext.Builder()
//            .apiKey(Constants.DIRECTION_SDK_KEY)
//            .build()
//        return context
//    }
}

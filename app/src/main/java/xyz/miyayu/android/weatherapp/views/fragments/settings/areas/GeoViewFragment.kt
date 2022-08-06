package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.GeoViewBinding

class GeoViewFragment : Fragment(R.layout.geo_view), OnMapReadyCallback {
    private var _binding: GeoViewBinding? = null
    private val binding
        get() = _binding!!

    private val args: GeoViewFragmentArgs by navArgs()

    private lateinit var googleMap: GoogleMap

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GeoViewBinding.bind(view)
        binding.apply {
            placeName.text = args.name
            placeNameSub.text = "(${args.subName})"

            if (args.subName == null || args.name == args.subName) {
                placeNameSub.isVisible = false
            }
        }

        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync(this)
        childFragmentManager.beginTransaction().add(R.id.google_map_frame, mapFragment).commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        try {
            val mapStyle =
                MapStyleOptions.loadRawResourceStyle(
                    WeatherApplication.instance.applicationContext,
                    R.raw.style_json
                )
            googleMap.setMapStyle(mapStyle)
        } catch (_: Throwable) {
        }

        val geoPosition = LatLng(args.lat.toDouble(), args.lon.toDouble())
        val makerPosition = MarkerOptions()
            .position(geoPosition)
            .title(args.name)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geoPosition, 8f))
        googleMap.addMarker(makerPosition)
    }
}
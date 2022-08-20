package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.GeoViewBinding
import xyz.miyayu.android.weatherapp.repositories.AreaRepository

class GeoViewFragment : Fragment(R.layout.geo_view), OnMapReadyCallback, TextWatcher {
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
            areaNameInputEdit.addTextChangedListener(this@GeoViewFragment)
            addLocationBtn.setOnClickListener {
                val inputText = areaNameInputEdit.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    AreaRepository.insertArea(
                        areaName = inputText,
                        latitude = args.lat.toDouble(),
                        longitude = args.lon.toDouble()
                    )
                }
                view.findNavController().navigate(GeoViewFragmentDirections.toSearchView())
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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        binding.addLocationBtn.isEnabled = !s.isNullOrEmpty()
    }
}
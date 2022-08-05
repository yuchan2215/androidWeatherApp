package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.GeoViewBinding

class GeoViewFragment : Fragment(R.layout.geo_view), OnMapReadyCallback {
    private var _binding: GeoViewBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var googleMap: GoogleMap

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GeoViewBinding.bind(view)

        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync(this)
        childFragmentManager.beginTransaction().add(R.id.google_map_frame, mapFragment).commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }
}
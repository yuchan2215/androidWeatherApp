package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.GeoSearchFragmentBinding

class GeoSearchFragment : Fragment(R.layout.geo_search_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = GeoSearchFragmentBinding.bind(view).apply {
            areaSearchView.setIconifiedByDefault(false)
        }
    }
}
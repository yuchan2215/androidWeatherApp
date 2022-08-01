package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import xyz.miyayu.android.weatherapp.databinding.WeatherResultFragmentBinding

/**
 * 天気を表示するフラグメント
 */
class WeatherResultFragment : Fragment() {
    private val args: WeatherResultFragmentArgs by navArgs()
    private lateinit var binding: WeatherResultFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherResultFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.areaName.text = args.name
    }
}
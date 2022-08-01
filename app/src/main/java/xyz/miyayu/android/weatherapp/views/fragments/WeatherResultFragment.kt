package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.WeatherResultFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.repositories.SettingRepository
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel
import xyz.miyayu.android.weatherapp.views.fragments.settings.areas.DeleteAreaDialogFragment

/**
 * 天気を表示するフラグメント
 */
class WeatherResultFragment : Fragment(R.layout.weather_result_fragment) {
    private val args: WeatherResultFragmentArgs by navArgs()
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactories.getWeatherViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        val binding = WeatherResultFragmentBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.areaName.text = args.areaName
        binding.areaDeleteButton.setOnClickListener {
            val area = Area(args.areaId, args.areaName)
            //地域を削除するダイアログを表示する
            DeleteAreaDialogFragment(area) {
                view.findNavController().navigate(WeatherResultFragmentDirections.backTop())
            }.show(childFragmentManager, DeleteAreaDialogFragment::class.java.name)
        }
        lifecycleScope.launch {
            val apiKey = SettingRepository.getApiKey()
            viewModel.getWeather(args.areaName, apiKey)
        }

        //読み込んだ結果を画面に表示する。
        viewModel.weather.observe(viewLifecycleOwner) {
            it?.let {
                with(binding) {
                    weatherType.text = it.description.firstOrNull()?.desc.orEmpty()
                    val temp = it.main.getTemp()
                    val feelTemp = it.main.getFeelTemp()
                    val text = getString(R.string.temp_display, temp, feelTemp)
                    tempText.text = text
                }
            }
        }
    }
}
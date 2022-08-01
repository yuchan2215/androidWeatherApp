package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.WeatherResultFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel
import xyz.miyayu.android.weatherapp.views.fragments.settings.areas.DeleteAreaDialogFragment

/**
 * 天気を表示するフラグメント
 */
class WeatherResultFragment : Fragment() {
    private val args: WeatherResultFragmentArgs by navArgs()
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: WeatherResultFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactories.getWeatherViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherResultFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.areaName.text = args.areaName
        binding.areaDeleteButton.setOnClickListener {
            val area = Area(args.areaId, args.areaName)
            //地域を削除するダイアログを表示する
            DeleteAreaDialogFragment(area) {
                view.findNavController().navigate(WeatherResultFragmentDirections.backTop())
            }.show(childFragmentManager, "Delete")
        }
        with(viewModel) {
            setting.observe(viewLifecycleOwner) { keySetting ->
                //天気を読み込む
                getWeather(args.areaName, keySetting.value)
            }
            //読み込んだ結果を画面に表示する。
            weather.observe(viewLifecycleOwner) {
                it?.let {
                    with(binding) {
                        weatherType.text = it.description.firstOrNull()?.desc
                        val temp = it.main.getTemp()
                        val feelTemp = it.main.getFeelTemp()
                        val text = getString(R.string.temp_display, temp, feelTemp)
                        tempText.text = text
                    }
                }
            }
        }
    }
}
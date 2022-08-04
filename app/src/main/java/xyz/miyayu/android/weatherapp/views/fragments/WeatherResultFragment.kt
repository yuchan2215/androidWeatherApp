package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.WeatherResultFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.network.json.Weather
import xyz.miyayu.android.weatherapp.utils.ErrorStatus
import xyz.miyayu.android.weatherapp.utils.Response
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel
import xyz.miyayu.android.weatherapp.viewmodel.factory.WeatherViewModelFactory
import xyz.miyayu.android.weatherapp.views.fragments.settings.areas.DeleteAreaDialogFragment

/**
 * 天気を表示するフラグメント
 */
class WeatherResultFragment : Fragment(R.layout.weather_result_fragment) {
    private val args: WeatherResultFragmentArgs by navArgs()
    private lateinit var viewModel: WeatherViewModel
    private val area: Area = Area(args.areaId, args.areaName)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = WeatherViewModelFactory(area)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        val binding = WeatherResultFragmentBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            this.viewmodel = this@WeatherResultFragment.viewModel

            //エリアを削除するボタンが押された時
            areaDeleteButton.setOnClickListener {
                val area = Area(args.areaId, args.areaName)

                //地域を削除するか尋ねるダイアログを表示する
                DeleteAreaDialogFragment(area) {
                    view.findNavController().navigate(WeatherResultFragmentDirections.backTop())
                }.show(childFragmentManager, DeleteAreaDialogFragment::class.java.name)
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            setAllGone(binding)
            when (val status = it) {
                is Response.Loading -> {
                    loadingBind(binding)
                }
                is Response.ErrorResponse -> {
                    errorBind(binding, status.errorStatus)
                }
                is Response.SuccessResponse<*> -> {
                    if (status.body !is Weather) {
                        throw IllegalStateException("body is not Weather")
                    }
                    successBind(binding, status.body)
                }
            }
        }
    }

    private fun loadingBind(binding: WeatherResultFragmentBinding) {
        binding.loadingView.isVisible = true
    }

    private fun errorBind(binding: WeatherResultFragmentBinding, errorStatus: ErrorStatus) {
        when (errorStatus) {
            is ErrorStatus.ErrorWithMessage -> {
                binding.errorView.isVisible = true
                binding.errorViewText.text = getString(errorStatus.messageId)
            }
            is ErrorStatus.UnAuthorizedErrorWithMessage -> {
                binding.errorView.isVisible = true
                binding.errorViewText.text = getString(errorStatus.messageId)
            }
            is ErrorStatus.AreaErrorWithMessage -> {
                binding.areaNotFoundErrorView.isVisible = true
                binding.areaNotFoundErrorText.text = getString(errorStatus.messageId)
            }
        }
    }

    private fun successBind(binding: WeatherResultFragmentBinding, weather: Weather) {
        val temp = weather.main.getTemp()
        val feelTemp = weather.main.getFeelTemp()
        val text = getString(R.string.temp_display, temp, feelTemp)

        binding.areaName.text = args.areaName
        binding.tempText.text = text
        binding.weatherType.text = weather.description.firstOrNull()?.desc.orEmpty()
        binding.resultView.isVisible = true
    }

    private fun setAllGone(binding: WeatherResultFragmentBinding) {
        binding.resultView.isVisible = false
        binding.errorView.isVisible = false
        binding.loadingView.isVisible = false
        binding.areaNotFoundErrorView.isVisible = false
    }
}
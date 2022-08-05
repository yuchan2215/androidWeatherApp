package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.GeoSearchFragmentBinding
import xyz.miyayu.android.weatherapp.utils.ErrorStatus
import xyz.miyayu.android.weatherapp.utils.Response
import xyz.miyayu.android.weatherapp.viewmodel.GeoSearchViewModel
import xyz.miyayu.android.weatherapp.viewmodel.factory.GeoSearchViewModelFactory

class GeoSearchFragment : Fragment(R.layout.geo_search_fragment),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var viewModel: GeoSearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = GeoSearchViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[GeoSearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = GeoSearchFragmentBinding.bind(view).apply {
            areaSearchView.setIconifiedByDefault(false)
            areaSearchView.setOnQueryTextListener(this@GeoSearchFragment)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadingView.isVisible = it
        }
        viewModel.geoResponse.observe(viewLifecycleOwner) { it ->
            binding.resultTitleText.text = if (it == null) ""
            else when (it) {
                is Response.Loading -> {
                    ""//何もしない
                }
                is Response.SuccessResponse -> {
                    getString(R.string.geo_founded, it.body.size)
                }
                is Response.ErrorResponse -> {
                    val id = when (val errorStatus = it.errorStatus) {
                        is ErrorStatus.ErrorWithMessage -> errorStatus.messageId
                        is ErrorStatus.AreaErrorWithMessage -> errorStatus.messageId
                        is ErrorStatus.UnAuthorizedErrorWithMessage -> {
                            //TODO APIキーエラー処理
                            errorStatus.messageId
                        }
                    }
                    getString(id)
                }
                null -> throw IllegalStateException("")
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.fetchGeos(it) }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false

    }
}
package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel
import xyz.miyayu.android.weatherapp.views.adapters.AreasListAdapter

/**
 * トップ画面のフラグメント
 */
class TopFragment : Fragment(R.layout.top_fragment) {
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactories.getSettingViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
    }

    /**
     * 設定ボタンがタップされたら設定画面に推移する。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = TopFragmentBinding.bind(view)
        binding.settingButton.setOnClickListener {
            view.findNavController().navigate(TopFragmentDirections.openSetting())
        }

        // 地域一覧のアダプター
        val listAdapter = AreasListAdapter { area ->
            view.findNavController()
                .navigate(
                    TopFragmentDirections.actionTopFragmentToWeatherResult(
                        area.name,
                        area.id
                    )
                )
        }

        binding.areaRecyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@TopFragment.context)
            //項目ごとに区切り線を入れる。
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
        viewModel.areaList.observe(viewLifecycleOwner) { items ->
            items.let {
                listAdapter.submitList(it)
            }
        }
    }
}
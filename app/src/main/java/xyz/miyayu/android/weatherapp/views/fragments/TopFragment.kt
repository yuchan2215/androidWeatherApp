package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel
import xyz.miyayu.android.weatherapp.views.adapters.AreasListAdapter

/**
 * トップ画面のフラグメント
 */
class TopFragment : Fragment() {
    private lateinit var binding: TopFragmentBinding
    private lateinit var viewModel: SettingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactories.getSettingViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 設定ボタンがタップされたら設定画面に推移する。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingButton.setOnClickListener {
            view.findNavController().navigate(TopFragmentDirections.openSetting())
        }

        // 地域一覧のアダプター
        val listAdapter = AreasListAdapter { area ->
            view.findNavController()
                .navigate(TopFragmentDirections.actionTopFragmentToWeatherResult(area.name))
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
package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.SettingFragmentBinding
import xyz.miyayu.android.weatherapp.views.adapters.ListDataAdapter
import xyz.miyayu.android.weatherapp.viewModel.setting.SettingListViewModel
import xyz.miyayu.android.weatherapp.viewModel.setting.SettingListViewModelFactory

class SettingFragment : Fragment() {
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var _listViewModelFactory: SettingListViewModelFactory
    private lateinit var _listViewModel: SettingListViewModel
    private lateinit var _adapter: ListDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //バインディングをする
        _binding = SettingFragmentBinding.inflate(inflater, container, false).apply {
            listViewModel = _listViewModel
            lifecycleOwner = viewLifecycleOwner
            lvSettingList.adapter = ListDataAdapter(listViewModel as SettingListViewModel)
        }

        _adapter = binding.lvSettingList.adapter as ListDataAdapter

        //ビューモデルが更新された時の処理
        _listViewModel.listItems.observe(viewLifecycleOwner) { list ->
            _adapter.replaceData(list)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = (activity?.application as WeatherApplication).database.settingDao()

        _listViewModelFactory = SettingListViewModelFactory(
            settingDao = dataSource,
            apiKeyTapEvent = {
                view?.findNavController()?.navigate(SettingFragmentDirections.openApiKeyInput())
            },
            areasTapEvent = {
                view?.findNavController()?.navigate(SettingFragmentDirections.actionSettingFragmentToAreasListFragment())
            })

        _listViewModel =
            ViewModelProvider(this, _listViewModelFactory)[SettingListViewModel::class.java]
                .apply { observe(this@SettingFragment) }
    }
}

package xyz.miyayu.android.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.databinding.SettingFragmentBinding
import xyz.miyayu.android.weatherapp.view.setting.ListDataAdapter
import xyz.miyayu.android.weatherapp.viewModel.setting.SettingListViewModel

class SettingFragment : Fragment() {
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

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
        _listViewModel = ViewModelProvider(this)[SettingListViewModel::class.java]
        _listViewModel.observe(this)
    }
}

package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.SettingFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Setting
import xyz.miyayu.android.weatherapp.viewmodel.SettingListViewModel
import xyz.miyayu.android.weatherapp.viewmodel.SettingListViewModelFactory
import xyz.miyayu.android.weatherapp.views.adapters.SettingListAdapter

/**
 * 設定画面のフラグメント。
 * 設定項目はRoomから取得したデータのプレビューを表示する。
 * TODO APIキーをショルダーハッキング等のリスク対策のために一部隠す。
 * TODO 地域の件数を表示する
 */
class SettingFragment : Fragment() {
    private lateinit var binding: SettingFragmentBinding
    private lateinit var fragmentViewModel: SettingListViewModel

    /**
     * ViewModelの生成と設定
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = (activity?.application as WeatherApplication).database.settingDao()

        val listViewModelFactory = SettingListViewModelFactory(
            settingDao = dataSource,
            apiKeyTapEvent = {
                view?.findNavController()?.navigate(SettingFragmentDirections.openApiKeyInput())
            },
            areasTapEvent = {
                view?.findNavController()
                    ?.navigate(SettingFragmentDirections.actionSettingFragmentToAreasListFragment())
            })

        fragmentViewModel =
            ViewModelProvider(this, listViewModelFactory)[SettingListViewModel::class.java]
                .also { it.observeList() }
    }

    /**
     * ViewModelのデータをオブザーブし、リストのデータを更新する。
     */
    private fun SettingListViewModel.observeList() {
        // 設定が変わった時にViewModelを更新するオブザーバー
        val apiKeyObserver = Observer<Setting> { setting ->
            val newList = listItems.value?.apply {
                get(0).value = setting?.value ?: ""
            } ?: throw IllegalStateException("ListItems is Empty!!")
            replaceItems(newList)
        }
        // オブザーブ実行。
        apiKey.observe(this@SettingFragment, apiKeyObserver)
    }

    /**
     * Viewの生成
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 各種バインディング
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            listViewModel = fragmentViewModel
            lifecycleOwner = viewLifecycleOwner
            lvSettingList.adapter = SettingListAdapter(listViewModel as SettingListViewModel)
        }

        val adapter = binding.lvSettingList.adapter as SettingListAdapter

        //ビューモデルが更新された時の処理
        this.fragmentViewModel.listItems.observe(viewLifecycleOwner) { list ->
            adapter.replaceData(list)
        }
    }
}
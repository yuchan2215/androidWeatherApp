package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.findNavController
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.SettingFragmentBinding
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel
import xyz.miyayu.android.weatherapp.views.adapters.SettingListAdapter

/**
 * 設定画面のフラグメント。
 * 設定項目はRoomから取得したデータのプレビューを表示する。
 * TODO APIキーをショルダーハッキング等のリスク対策のために一部隠す。
 * TODO 地域の件数を表示する
 */
class SettingFragment : Fragment(R.layout.setting_fragment) {
    private lateinit var adapter: SettingListAdapter
    private lateinit var settingViewModel: SettingViewModel

    /**
     * ViewModelの生成と設定
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingViewModelFactory = ViewModelFactories.getSettingViewModelFactory()

        settingViewModel =
            ViewModelProvider(this, settingViewModelFactory)[SettingViewModel::class.java]
    }

    /**
     * 各種バインディング
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingListAdapter()
        val binding = SettingFragmentBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            lvSettingList.adapter = adapter
        }

        //タップした時の処理を追加
        with(adapter) {
            setAreasListener {
                view.findNavController()
                    .navigate(SettingFragmentDirections.actionSettingFragmentToAreasListFragment())
            }
            setApiKeyListener {
                view.findNavController().navigate(SettingFragmentDirections.openApiKeyInput())
            }
        }

        //ViewModelの値をobserveする。
        settingViewModel.apiKey.map {
            val size = it.value?.length ?: 0
            if (size == 0) {
                return@map getString(R.string.api_key_not_configured)
            } else {
                return@map getString(R.string.api_key_configured)
            }
        }.observe(viewLifecycleOwner) {
            adapter.setApiKeyPreview(it)
        }

        settingViewModel.areaList.map {
            val size = it.size
            return@map resources.getQuantityString(R.plurals.regions_preview, size, size)
        }.observe(viewLifecycleOwner) {
            adapter.setAreasPreview(it)
        }
    }
}
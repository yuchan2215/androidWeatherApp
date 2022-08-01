package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.utils.ViewModelFactories
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel
import xyz.miyayu.android.weatherapp.views.adapters.AreasListAdapter

/**
 * 地域リストのフラグメント。
 * 項目をタップすると削除するかどうか尋ね、フローティングボタンをタップすると項目追加画面に推移する。
 */
class AreasListFragment : Fragment(R.layout.area_list_fragment) {
    private lateinit var viewModel: SettingViewModel

    companion object {
        private const val TAG = "AreaList"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactories.getSettingViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = AreasListAdapter(
            onItemClicked = {
                //削除確認のフラグメントを表示する
                DeleteAreaDialogFragment(it).show(childFragmentManager, "Check")
            })

        AreaListFragmentBinding.bind(view).apply {
            //地域を追加するフラグメントを表示する
            addLocationBtn.setOnClickListener {
                EnterAreaDialogFragment(
                    //入力が完了した時のリスナー
                    confirmListener = { areaName ->
                        lifecycleScope.launch {
                            val apiKey = WeatherApplication.instance.database.settingDao()
                                .getItemOnce().value ?: ""
                            runAddAreaProcess(apiKey, areaName)
                        }
                        //地域を追加するプロセスを実行する。
                    }).show(childFragmentManager, "add")
            }

            areaRecyclerView.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@AreasListFragment.context)
                //項目ごとに区切り線を入れる。
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                )
            }

        }
        //地域が更新されたらリストを更新する。
        viewModel.areaList.observe(this.viewLifecycleOwner) { items ->
            items.let {
                listAdapter.submitList(it)
            }
        }
    }

    enum class AvailableStatus(val message: String = "") {
        OK,
        NG(WeatherApplication.instance.getString(R.string.area_check_notfound_message)),
        ERROR(WeatherApplication.instance.getString(R.string.area_check_error_message)),
        API_KEY_NOT_EXIST(WeatherApplication.instance.getString(R.string.api_key_not_found_error_message)),
        UNAUTHORIZED(WeatherApplication.instance.getString(R.string.error_unauthorized))
    }

    /**
     * 地域を追加する
     */
    private fun addArea(area: String) {
        val areaObj = Area(name = area)
        CoroutineScope(Dispatchers.IO).launch {
            WeatherApplication.instance.database.areaDao().insert(areaObj)
        }
    }

    private fun openApiKeySetting() {
        view?.findNavController()
            ?.navigate(AreasListFragmentDirections.toRestartApiKey())
    }

    private fun runAddAreaProcess(apiKey: String, areaName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val status = isAreaIsAvailable(apiKey, areaName)) {
                //OKの時
                AvailableStatus.OK -> addArea(areaName)
                //APIキーの再設定が必要なエラーが発生した時
                AvailableStatus.API_KEY_NOT_EXIST, AvailableStatus.UNAUTHORIZED -> {
                    //APIキーを再設定するか尋ねるエラー
                    AreaApiErrorDialogFragment(
                        title = getString(R.string.api_error),
                        message = status.message + "\n" + getString(R.string.api_resetting_question),
                        confirmEvent = { openApiKeySetting() },
                        neutralEvent = { addArea(areaName) }
                    ).show(childFragmentManager, "RESET")
                }
                //APIキーの再設定が不要なエラーが発生した時
                AvailableStatus.ERROR, AvailableStatus.NG -> {
                    //エラー
                    AreaErrorDialogFragment(
                        title = getString(R.string.error),
                        message = status.message,
                        neutralEvent = { addArea(areaName) }
                    ).show(childFragmentManager, "ALERT")
                }
            }
        }
    }


    /**
     * 地域が有効かどうか確認する。
     */
    private suspend fun isAreaIsAvailable(apiKey: String, area: String): AvailableStatus {
        if (apiKey.isEmpty()) {
            return AvailableStatus.API_KEY_NOT_EXIST
        }
        return withContext(Dispatchers.IO) {
            try {
                val weather = WeatherApi.retrofitService.getWeather(apiKey, area)
                return@withContext when (weather.raw().code) {
                    200 -> AvailableStatus.OK
                    401 -> AvailableStatus.UNAUTHORIZED
                    404 -> AvailableStatus.NG
                    else -> AvailableStatus.ERROR
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext AvailableStatus.ERROR
            }
        }
    }
}
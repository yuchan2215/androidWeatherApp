package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.content.DialogInterface
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
import xyz.miyayu.android.weatherapp.constant.HTTPResponseCode
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.repositories.SettingRepository
import xyz.miyayu.android.weatherapp.utils.DialogAction
import xyz.miyayu.android.weatherapp.viewmodel.AreaListFragmentViewModel
import xyz.miyayu.android.weatherapp.viewmodel.factory.AreaListFragmentViewModelFactory
import xyz.miyayu.android.weatherapp.views.adapters.AreaListAdapter

/**
 * 地域リストのフラグメント。
 * 項目をタップすると削除するかどうか尋ね、フローティングボタンをタップすると項目追加画面に推移する。
 */
class AreasListFragment : Fragment(R.layout.area_list_fragment) {
    private lateinit var viewModel: AreaListFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = AreaListFragmentViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[AreaListFragmentViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = object : AreaListAdapter() {
            //項目が選択された時
            override fun onItemClicked(area: Area) {
                //削除ボタンのアクション
                val deleteDialogAction = object : DialogAction {
                    override val buttonText: String
                        get() = getString(R.string.delete)

                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        viewModel.deleteArea(area)
                    }
                }
                //ダイアログを表示する
                BuiltDialogFragment(
                    title = getString(R.string.delete_area_title),
                    message = null,
                    positive = deleteDialogAction
                ).show(childFragmentManager, this.hashCode().toString())
            }
        }

        AreaListFragmentBinding.bind(view).apply {
            //地域を追加するフラグメントを表示する
            addLocationBtn.setOnClickListener {
                EnterAreaDialogFragment(
                    //入力が完了した時のリスナー
                    confirmListener = { areaName ->
                        lifecycleScope.launch {
                            val apiKey = SettingRepository.getApiKey() ?: ""
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

    private fun openApiKeySetting() {
        view?.findNavController()
            ?.navigate(AreasListFragmentDirections.toRestartApiKey())
    }

    private fun runAddAreaProcess(apiKey: String, areaName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val status = isAreaIsAvailable(apiKey, areaName)) {
                //OKの時
                AvailableStatus.OK -> viewModel.addArea(areaName)
                //APIキーの再設定が必要なエラーが発生した時
                AvailableStatus.API_KEY_NOT_EXIST, AvailableStatus.UNAUTHORIZED -> {
                    //APIキーを再設定するか尋ねるエラー
                    AreaApiErrorDialogFragment(
                        title = getString(R.string.api_error),
                        message = status.message + "\n" + getString(R.string.api_resetting_question),
                        confirmEvent = { openApiKeySetting() },
                        neutralEvent = { viewModel.addArea(areaName) }
                    ).show(childFragmentManager, AreaApiErrorDialogFragment::class.java.name)
                }
                //APIキーの再設定が不要なエラーが発生した時
                AvailableStatus.ERROR, AvailableStatus.NG -> {
                    //エラー
                    AreaErrorDialogFragment(
                        title = getString(R.string.error),
                        message = status.message,
                        neutralEvent = { viewModel.addArea(areaName) }
                    ).show(childFragmentManager, AreaErrorDialogFragment::class.java.name)
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
                    HTTPResponseCode.OK -> AvailableStatus.OK
                    HTTPResponseCode.UNAUTHORIZED -> AvailableStatus.UNAUTHORIZED
                    HTTPResponseCode.NOT_FOUND -> AvailableStatus.NG
                    else -> AvailableStatus.ERROR
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext AvailableStatus.ERROR
            }
        }
    }
}
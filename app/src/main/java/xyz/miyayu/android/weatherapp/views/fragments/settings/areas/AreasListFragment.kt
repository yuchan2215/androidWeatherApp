package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
class AreasListFragment : Fragment() {

    private lateinit var binding: AreaListFragmentBinding
    private lateinit var viewModel: SettingViewModel

    companion object {
        private const val TAG = "AreaList"
    }

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
        binding = AreaListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = AreasListAdapter(
            onItemClicked = {
                //削除確認のフラグメントを表示する
                DeleteAreaDialogFragment(it).show(childFragmentManager, "Check")
            })

        with(binding) {
            //地域を追加するフラグメントを表示する
            addLocationBtn.setOnClickListener {
                EnterAreaDialogFragment {
                    //APIキーを取得
                    viewModel.apiKey.observe(viewLifecycleOwner) {}
                    val apiKey = viewModel.apiKey.value?.value ?: ""

                    //地域が存在すれば地域を追加する
                    CoroutineScope(Dispatchers.IO).launch {
                        val status = isAreaIsAvailable(apiKey, it)
                        when (status) {
                            AvailableStatus.OK -> {
                                addArea(it)
                            }
                            else -> {}
                        }
                    }
                }.show(childFragmentManager, "add")
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
        NG,
        ERROR,
        API_KEY_NOT_EXIST(WeatherApplication.instance.getString(R.string.api_key_not_found_error_message)),
        UNAUTHORIZED(WeatherApplication.instance.getString(R.string.error_unauthorized))
    }

    /**
     * 地域を追加する
     */
    fun addArea(area: String) {
        val areaObj = Area(name = area)
        CoroutineScope(Dispatchers.IO).launch {
            WeatherApplication.instance.database.areaDao().insert(areaObj)
        }
    }


    /**
     * 地域が有効かどうか確認する。
     */
    suspend fun isAreaIsAvailable(apiKey: String, area: String): AvailableStatus {
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
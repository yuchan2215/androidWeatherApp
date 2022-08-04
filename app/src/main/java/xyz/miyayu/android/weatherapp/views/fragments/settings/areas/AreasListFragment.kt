package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.repositories.AreaRepository
import xyz.miyayu.android.weatherapp.repositories.WeatherRepository
import xyz.miyayu.android.weatherapp.utils.ErrorStatus
import xyz.miyayu.android.weatherapp.utils.Response
import xyz.miyayu.android.weatherapp.viewmodel.AreaListFragmentViewModel
import xyz.miyayu.android.weatherapp.viewmodel.factory.AreaListFragmentViewModelFactory
import xyz.miyayu.android.weatherapp.views.adapters.AreaListAdapter
import xyz.miyayu.android.weatherapp.views.fragments.dialog.AddAreaDialogFragment

/**
 * 地域リストのフラグメント。
 * 項目をタップすると削除するかどうか尋ね、フローティングボタンをタップすると項目追加画面に推移する。
 */
class AreasListFragment : Fragment(R.layout.area_list_fragment) {
    companion object {
        const val AREA_LIST_FRAGMENT_KEY = "AREA_LIST_FRAGMENT"
        const val ADD_AREA = "ADD_AREA"
    }

    private lateinit var viewModel: AreaListFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = AreaListFragmentViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[AreaListFragmentViewModel::class.java]

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener()

        val listAdapter = object : AreaListAdapter() {
            /**項目が選択された時に、削除するかどうか尋ねるフラグメントを表示する。*/
            override fun onItemClicked(area: Area) {
                showAreaDeleteDialog(requireContext(), area)
            }
        }


        AreaListFragmentBinding.bind(view).apply {
            //地域を追加するフラグメントを表示する
            addLocationBtn.setOnClickListener {
                AddAreaDialogFragment().show(
                    childFragmentManager,
                    AddAreaDialogFragment::class.java.name
                )
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

    /**子フラグラメントの結果を聞くリスナー*/
    private fun setFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            AREA_LIST_FRAGMENT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ADD_AREA)?.let { areaName ->
                receivedAreaNameListener(areaName)
            }
        }
    }

    /**
     * エリアの名前が呼び出された時のリスナー。
     * 試しに天気を取得し、[Response]の内容によって処理を振り分ける。
     */
    private fun receivedAreaNameListener(areaName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                when (val response = WeatherRepository.fetchWeather(areaName)) {
                    //通常は呼び出されない
                    is Response.Loading -> throw IllegalStateException()
                    is Response.SuccessResponse<*> -> AreaRepository.insertArea(areaName)
                    is Response.ErrorResponse -> onFetchErrorListener(response, areaName)
                }
            } catch (e: Throwable) {
                onFetchErrorListener(Response.createUnknownError(), areaName)
            }
        }
    }

    /**
     * エラーが発生した際に呼び出される。
     * [Response.ErrorResponse]の内容によって表示するダイアログを振り分ける。
     */
    private fun onFetchErrorListener(response: Response.ErrorResponse, areaName: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            when (val errorStatus = response.errorStatus) {
                is ErrorStatus.UnAuthorizedErrorWithMessage -> {
                    showApiKeyResetRequireDialog(areaName, errorStatus.messageId)
                }
                is ErrorStatus.AreaErrorWithMessage, is ErrorStatus.ErrorWithMessage -> {
                    val messageId = when (errorStatus) {
                        is ErrorStatus.AreaErrorWithMessage -> errorStatus.messageId
                        is ErrorStatus.ErrorWithMessage -> errorStatus.messageId
                        else -> throw IllegalStateException()
                    }
                    showErrorDialog(areaName, messageId)
                }

            }
        }
    }

    /**
     * APIキーの再設定が必要なことを表すダイアログを表示する。
     */
    private fun showApiKeyResetRequireDialog(areaName: String, messageId: Int) {
        val message =
            "${getString(messageId)}\n${getString(R.string.api_resetting_question)}"

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.api_error)
            .setMessage(message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                view?.findNavController()?.navigate(
                    AreasListFragmentDirections.toRestartApiKey()
                )
            }
            .setNeutralButton(R.string.force_set) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    AreaRepository.insertArea(areaName)
                }
            }
            .show()
    }

    /**
     * エラーのダイアログを表示する。
     */
    private fun showErrorDialog(areaName: String, messageId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setMessage(messageId)
            .setPositiveButton(R.string.cancel, null)
            .setNeutralButton(R.string.force_set) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    AreaRepository.insertArea(areaName)
                }
            }
            .show()
    }


    /**
     * エリアを削除するかどうか尋ねるダイアログを表示する。
     */
    private fun showAreaDeleteDialog(context: Context, area: Area) {
        AlertDialog.Builder(context)
            .setTitle(R.string.delete_area_title)
            .setMessage(area.name)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteArea(area)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
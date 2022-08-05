package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Area
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
            /**項目が選択された時に、削除するかどうか尋ねるフラグメントを表示する。*/
            override fun onItemClicked(area: Area) {
                showAreaDeleteDialog(requireContext(), area)
            }
        }


        AreaListFragmentBinding.bind(view).apply {
            //地域を追加するフラグメントを表示する
            addLocationBtn.setOnClickListener {
                view.findNavController().navigate(AreasListFragmentDirections.toGeoSearch())
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
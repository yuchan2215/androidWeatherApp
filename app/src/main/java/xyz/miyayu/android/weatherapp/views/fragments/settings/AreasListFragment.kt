package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModelFactory
import xyz.miyayu.android.weatherapp.views.adapters.AreasListAdapter

/**
 * 地域リストのフラグメント。
 * 項目をタップすると削除するかどうか尋ね、フローティングボタンをタップすると項目追加画面に推移する。
 */
class AreasListFragment : Fragment() {

    private lateinit var binding: AreaListFragmentBinding
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val areaSource = (activity?.application as WeatherApplication).database.areaDao()
        val settingSource = (activity?.application as WeatherApplication).database.settingDao()
        val viewModelFactory = SettingViewModelFactory(settingSource, areaSource)
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
                EnterAreaDialogFragment().show(childFragmentManager, "add")
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
}
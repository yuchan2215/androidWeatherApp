package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.miyayu.android.weatherapp.views.adapters.AreasListAdapter
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.AreaListFragmentBinding
import xyz.miyayu.android.weatherapp.viewModel.setting.AreasListViewModel
import xyz.miyayu.android.weatherapp.viewModel.setting.AreasListViewModelFactory

class AreasListFragment : Fragment() {
    private var _binding: AreaListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AreasListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataSource = (activity?.application as WeatherApplication).database.areaDao()
        val viewModelFactory = AreasListViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AreasListViewModel::class.java]

        _binding = AreaListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = AreasListAdapter {
            //削除確認を開く
            DeleteAreaDialogFragment(it).show(childFragmentManager, "Check")
        }

        with(binding) {
            //追加ボタンの処理
            addLocationBtn.setOnClickListener {
                EnterAreaDialogFragment().show(childFragmentManager, "add")
            }
            areaRecyclerView.apply {
                adapter = listAdapter
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                )
                layoutManager = LinearLayoutManager(this@AreasListFragment.context)
            }

        }
        viewModel.allAreas.observe(this.viewLifecycleOwner) { items ->
            items.let {
                listAdapter.submitList(it)
            }
        }
    }
}
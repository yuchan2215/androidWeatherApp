package xyz.miyayu.android.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        viewModel = ViewModelProvider(this,viewModelFactory)[AreasListViewModel::class.java]

        _binding = AreaListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AreasListAdapter {
            //TODO タップされた時の処理
        }
        binding.areaRecyclerView.adapter = adapter
        viewModel.allAreas.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.areaRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }


}
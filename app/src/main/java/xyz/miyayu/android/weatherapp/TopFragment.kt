package xyz.miyayu.android.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding

class TopFragment : Fragment() {
    private var _binding: TopFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
package xyz.miyayu.android.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import xyz.miyayu.android.weatherapp.databinding.ApiInputFragmentBinding
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding

class ApiKeyInputFragment : Fragment() {
    private var _binding: ApiInputFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ApiInputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
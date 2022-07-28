package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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

        binding.settingButton.setOnClickListener {
            view?.findNavController()?.navigate(TopFragmentDirections.openSetting())
        }
        return binding.root
    }
}
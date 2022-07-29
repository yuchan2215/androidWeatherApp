package xyz.miyayu.android.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding

/**
 * トップ画面のフラグメント
 */
class TopFragment : Fragment() {
    private lateinit var binding: TopFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 設定ボタンがタップされたら設定画面に推移する。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingButton.setOnClickListener {
            view.findNavController().navigate(TopFragmentDirections.openSetting())
        }
    }
}
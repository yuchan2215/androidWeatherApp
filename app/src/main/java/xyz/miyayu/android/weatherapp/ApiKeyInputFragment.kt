package xyz.miyayu.android.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.data.API_KEY_COL
import xyz.miyayu.android.weatherapp.data.Setting
import xyz.miyayu.android.weatherapp.databinding.ApiInputFragmentBinding
import xyz.miyayu.android.weatherapp.databinding.TopFragmentBinding

class ApiKeyInputFragment : Fragment(),TextWatcher {
    private var _binding: ApiInputFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ApiInputFragmentBinding.inflate(inflater, container, false).apply {
            itemKey.addTextChangedListener(this@ApiKeyInputFragment)
            saveBtn.isEnabled = false
            saveBtn.setOnClickListener {
                // テキストをデータベースに格納する。
                val inputText = binding.itemKey.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    (activity?.application as WeatherApplication).database.settingDao().insert(
                        Setting(API_KEY_COL,inputText)
                    )
                }
                //戻る
                view?.findNavController()
                    ?.navigate(ApiKeyInputFragmentDirections.backToSetting())
            }
        }
        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    /**
     * テキストが含まれるならセーブボタンを有効化する
     */
    override fun afterTextChanged(s: Editable?) {
        binding.saveBtn.isEnabled = s?.isNotEmpty() ?: false
    }
}
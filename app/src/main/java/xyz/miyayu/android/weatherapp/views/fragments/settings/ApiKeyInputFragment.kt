package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.ApiInputFragmentBinding
import xyz.miyayu.android.weatherapp.model.entity.Setting

/**
 * APIキーを入力するためのフラグメント。
 */
class ApiKeyInputFragment : Fragment() {
    private lateinit var binding: ApiInputFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApiInputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 入力の変更の監視
     * セーブボタンの処理の定義
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            itemKey.addTextChangedListener(textChangeListener)

            saveBtn.apply {
                isEnabled = false
                setOnClickListener(saveButtonListener)
            }
        }
    }

    /**
     * セーブボタンが押された時に、データを追加して前の画面に戻る。
     */
    private val saveButtonListener = View.OnClickListener {
        // テキストをデータベースに格納する
        val inputText = binding.itemKey.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            (activity?.application as WeatherApplication).database.settingDao().insert(
                Setting(value = inputText)
            )
        }
        //戻る
        view?.findNavController()
            ?.navigate(ApiKeyInputFragmentDirections.backToSetting())
    }

    /**
     * テキストの変更を監視し、セーブボタンの有効状態を変化させる。
     */
    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            binding.saveBtn.isEnabled = s?.isNotEmpty() ?: false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
package xyz.miyayu.android.weatherapp.views.fragments.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.databinding.ApiInputFragmentBinding
import xyz.miyayu.android.weatherapp.repositories.SettingRepository

/**
 * APIキーを入力するためのフラグメント。
 */
class ApiKeyInputFragment : Fragment(R.layout.api_input_fragment) {
    private lateinit var binding: ApiInputFragmentBinding


    /**
     * 入力の変更の監視
     * セーブボタンの処理の定義
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ApiInputFragmentBinding.bind(view).apply {
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
            SettingRepository.setApiKey(inputText)
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
            binding.saveBtn.isEnabled = !s.isNullOrEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
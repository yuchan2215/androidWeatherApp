package xyz.miyayu.android.weatherapp.views.fragments.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.views.fragments.settings.areas.AreasListFragment

/**
 * 地域を追加する画面のフラグメント。
 * 何も入力されていない状態であればConfirmボタンを無効化する。
 */
class AddAreaDialogFragment : DialogFragment(), TextWatcher {

    private lateinit var dialog: AlertDialog

    /**
     * 起動時にConfirmを無効化
     */
    override fun onResume() {
        super.onResume()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // テキストの入力フォームのレイアウト（マージンを確保するために使用）
        val textInputLayout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.layout_common_edit_dialog, null)

        // 入力フォームのView。
        val editText = textInputLayout.findViewById<AppCompatEditText>(R.id.input_field)
            .apply {
                //ヒントと、Confirmボタン無効化のためのリスナーを設定
                hint = getString(R.string.add_area_hint)
                addTextChangedListener(this@AddAreaDialogFragment)
            }


        // ダイアログを作成
        return activity?.let {
            dialog = AlertDialog.Builder(it)
                .setTitle(R.string.add_area_title)
                .setView(textInputLayout)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    //結果を渡す。
                    parentFragmentManager.setFragmentResult(
                        AreasListFragment.AREA_LIST_FRAGMENT_KEY,
                        bundleOf(AreasListFragment.ADD_AREA to editText.text.toString())
                    )
                }.create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun afterTextChanged(s: Editable?) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = s.isNullOrEmpty()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
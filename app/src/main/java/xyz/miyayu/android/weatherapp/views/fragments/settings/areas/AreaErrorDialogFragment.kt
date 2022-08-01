package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.miyayu.android.weatherapp.R

/**
 * APIキーに関するエラーが発生した時に、そのまま追加するか尋ねるイベント
 */
class AreaErrorDialogFragment(
    private val title: String,
    private val message: String,
    private val neutralEvent: () -> Unit
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        /**
         * 左ボタンが押された時の処理
         */
        val neutralListener = OnClickListener { _, _ ->
            neutralEvent.invoke()
            dismiss()
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setTitle(title)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setNeutralButton(R.string.force_set, neutralListener)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
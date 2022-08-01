package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.miyayu.android.weatherapp.R

/**
 * APIキーに関するエラーが発生した時に、APIキーを修正するか尋ねるフラグメント
 */
class AreaApiErrorDialogFragment(
    private val title: String,
    private val message: String,
    private val confirmEvent: () -> Unit,
    private val neutralEvent: () -> Unit
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /**
         * 右ボタンが押された時の処理
         */
        val positiveListener = OnClickListener { _, _ ->
            confirmEvent.invoke()
            dismiss()
        }

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
                .setPositiveButton(R.string.yes, positiveListener)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
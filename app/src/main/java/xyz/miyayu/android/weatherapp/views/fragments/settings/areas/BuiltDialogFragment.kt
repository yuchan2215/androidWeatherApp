package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.miyayu.android.weatherapp.utils.DialogAction

abstract class BuiltDialogFragment(
    private val title: String?,
    private val message: String?,
    private val negative: DialogAction?,
    private val neutral: DialogAction?,
    private val positive: DialogAction?
) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity).apply {
                //タイトルを設定
                this@BuiltDialogFragment.title?.let {
                    setTitle(it)
                }
                //メッセージを設定
                this@BuiltDialogFragment.message?.let {
                    setMessage(it)
                }
                this@BuiltDialogFragment.negative?.let {
                    setNegativeButton(it.buttonText, it)
                }
                this@BuiltDialogFragment.positive?.let {
                    setPositiveButton(it.buttonText, it)
                }
                this@BuiltDialogFragment.neutral?.let {
                    setNeutralButton(it.buttonText, it)
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
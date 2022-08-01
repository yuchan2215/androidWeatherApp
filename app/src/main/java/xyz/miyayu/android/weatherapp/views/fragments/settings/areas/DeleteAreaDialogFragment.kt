package xyz.miyayu.android.weatherapp.views.fragments.settings.areas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.entity.Area

/**
 * 地域を削除するときの確認ダイアログのフラグメント。
 */
class DeleteAreaDialogFragment(private val area: Area, private val confirmEvent: () -> Unit = {}) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /**
         * Confirmボタンが押された時に削除するためのリスナー
         */
        val confirmListener = OnClickListener { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                WeatherApplication.instance.database.areaDao()
                    .delete(area)
            }
            confirmEvent.invoke()
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(area.name)
                .setTitle(R.string.delete_area_title)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm, confirmListener)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
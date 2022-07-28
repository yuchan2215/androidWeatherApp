package xyz.miyayu.android.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.data.Area
import java.lang.IllegalStateException

class DeleteAreaDialogFragment(private val area: Area) : DialogFragment() {

    private val onCancel = OnClickListener { _, _ -> }
    private val onConfirm = OnClickListener { _, _ ->
        CoroutineScope(Dispatchers.IO).launch {
            (activity?.application as WeatherApplication).database.areaDao()
                .delete(area)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(area.name)
                .setTitle(R.string.delete_area_title)
                .setNegativeButton(R.string.cancel, onCancel)
                .setPositiveButton(R.string.confirm, onConfirm)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}
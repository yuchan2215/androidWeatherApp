package xyz.miyayu.android.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.data.Area
import java.lang.IllegalStateException

class EnterAreaDialogFragment : DialogFragment(), TextWatcher {

    private lateinit var editText: AppCompatEditText
    private lateinit var dialog: AlertDialog

    /**
     * 地域を追加するためのリスナー
     */
    private val addAreaListener = OnClickListener { _, _ ->
        CoroutineScope(Dispatchers.IO).launch {
            val area = Area(name = editText.editableText.toString())
            (activity?.application as WeatherApplication).database.areaDao().insert(area)
        }
    }

    /**
     * 起動時にConfirmを無効化
     */
    override fun onResume() {
        super.onResume()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogLayout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.layout_common_edit_dialog, null)

        //ヒントの設定と、文字の更新リスナーを設定
        editText = dialogLayout.findViewById<AppCompatEditText>(R.id.input_field)
            .apply {
                hint = getString(R.string.add_area_hint)
                addTextChangedListener(this@EnterAreaDialogFragment)
            }


        return activity?.let {
            dialog = AlertDialog.Builder(it)
                .setTitle(R.string.add_area_title)
                .setView(dialogLayout)
                .setNegativeButton(R.string.cancel){_,_ -> }
                .setPositiveButton(R.string.confirm, addAreaListener)
                .create()
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun afterTextChanged(s: Editable?) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = s?.isNotEmpty() ?: false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
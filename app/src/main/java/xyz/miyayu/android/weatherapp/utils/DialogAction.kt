package xyz.miyayu.android.weatherapp.utils

import android.content.DialogInterface

interface DialogAction : DialogInterface.OnClickListener {
    val buttonText: String
}
package xyz.miyayu.android.weatherapp.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import xyz.miyayu.android.weatherapp.R

class LoadingView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(
            R.layout.loading_view, this, true
        )
    }
}
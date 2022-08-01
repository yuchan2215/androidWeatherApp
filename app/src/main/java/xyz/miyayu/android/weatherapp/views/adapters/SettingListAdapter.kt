package xyz.miyayu.android.weatherapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.databinding.SettingListItemBinding

/**
 * 設定画面のBaseAdapter。
 * １段目はAPIキー
 * ２段目は地域
 */
class SettingListAdapter : BaseAdapter() {
    private val resources = WeatherApplication.instance.resources
    private val itemsList = listOf(
        //1段目(APIキー)
        SettingItem(
            resources.getString(R.string.api_key_item)
        ),
        //2段目(地域)
        SettingItem(
            resources.getString(R.string.area_item)
        ),
    )


    data class SettingItem(
        val title: String,
        var preview: String = "",
        var tapEvent: () -> Unit = {}
    )

    fun setApiKeyPreview(str: String) {
        itemsList[API_KEY_INDEX].preview = str
        notifyDataSetChanged()
    }

    fun setApiKeyListener(tapEvent: () -> Unit) {
        itemsList[API_KEY_INDEX].tapEvent = tapEvent
        notifyDataSetChanged()
    }

    fun setAreasPreview(str: String) {
        itemsList[AREAS_INDEX].preview = str
        notifyDataSetChanged()
    }

    fun setAreasListener(tapEvent: () -> Unit) {
        itemsList[AREAS_INDEX].tapEvent = tapEvent
        notifyDataSetChanged()
    }

    override fun getCount() = itemsList.size
    override fun getItem(position: Int) = itemsList[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            SettingListItemBinding.inflate(inflater, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: throw IllegalStateException()
        }.apply {
            val item = itemsList[position]
            listItem.setOnClickListener {
                item.tapEvent.invoke()
            }
            settingItem.text = item.title
            settingItemPreview.text = item.preview
            executePendingBindings()
        }
        return binding.root
    }

    companion object {
        private const val API_KEY_INDEX = 0
        private const val AREAS_INDEX = 1
    }
}
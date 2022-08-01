package xyz.miyayu.android.weatherapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import xyz.miyayu.android.weatherapp.databinding.SettingListItemBinding
import xyz.miyayu.android.weatherapp.viewmodel.ListData
import xyz.miyayu.android.weatherapp.viewmodel.SettingListViewModel

/**
 * 設定のトップ画面のアダプター。
 */
class SettingListAdapter(private val listViewModel: SettingListViewModel) : BaseAdapter() {

    private var listDataList: List<ListData> = listOf()
    override fun getCount() = listDataList.size
    override fun getItem(position: Int) = listDataList[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            SettingListItemBinding.inflate(inflater, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: throw IllegalStateException()
        }

        with(binding) {
            listData = listDataList[position]
            settingData = listViewModel
            listItem.setOnClickListener {
                (listData as ListData).tapEvent.invoke()
            }
            executePendingBindings()
        }
        return binding.root
    }

    fun replaceData(listDataList: List<ListData>) {
        this.listDataList = listDataList
        notifyDataSetChanged()
    }

}
package xyz.miyayu.android.weatherapp.view.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import xyz.miyayu.android.weatherapp.databinding.SettingListItemBinding
import xyz.miyayu.android.weatherapp.viewModel.setting.ListData
import xyz.miyayu.android.weatherapp.viewModel.setting.SettingListViewModel
import java.lang.IllegalStateException

class ListDataAdapter(private val listViewModel: SettingListViewModel) : BaseAdapter() {

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
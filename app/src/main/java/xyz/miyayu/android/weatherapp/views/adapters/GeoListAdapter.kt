package xyz.miyayu.android.weatherapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.miyayu.android.weatherapp.databinding.GeoListItemBinding
import xyz.miyayu.android.weatherapp.network.json.direct.Direct

/**
 * 地域一覧のアダプター。
 * タップされた時の処理は引数にて指定する。
 */
abstract class GeoListAdapter :
    ListAdapter<Direct, GeoListAdapter.GeoViewHolder>(DiffCallback) {

    abstract fun onItemClicked(area: Direct)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoViewHolder {
        return GeoViewHolder(
            GeoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GeoViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class GeoViewHolder(private var binding: GeoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(direct: Direct) {
            binding.apply {
                geoNameSub.text = direct.name
                geoName.text = direct.names.currentName() ?: direct.name
                //もし２つの表示が違うなら表示する。
                geoNameSub.isVisible = geoNameSub.text.toString() != geoName.text.toString()

                country.text = direct.country
                state.text = direct.state ?: direct.country
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Direct>() {
            override fun areContentsTheSame(oldItem: Direct, newItem: Direct): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Direct, newItem: Direct): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}
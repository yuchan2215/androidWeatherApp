package xyz.miyayu.android.weatherapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.miyayu.android.weatherapp.databinding.AreaListItemBinding
import xyz.miyayu.android.weatherapp.model.entity.Area

/**
 * 地域一覧のアダプター。
 * タップされた時の処理は引数にて指定する。
 */
abstract class AreaListAdapter :
    ListAdapter<Area, AreaListAdapter.AreaViewHolder>(DiffCallback) {

    abstract fun onItemClicked(area: Area)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        return AreaViewHolder(
            AreaListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class AreaViewHolder(private var binding: AreaListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(area: Area) {
            binding.apply {
                areaName.text = area.name
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Area>() {
            override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}
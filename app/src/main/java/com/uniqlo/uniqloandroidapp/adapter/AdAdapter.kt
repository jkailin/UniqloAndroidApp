package com.uniqlo.uniqloandroidapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniqlo.uniqloandroidapp.R
import com.uniqlo.uniqloandroidapp.data.Ad
import com.uniqlo.uniqloandroidapp.databinding.AdBinding
import com.uniqlo.uniqloandroidapp.ui.discover.DiscoverViewModel
import kotlinx.android.synthetic.main.ad.view.*

class AdAdapter(private val viewModel: DiscoverViewModel) : ListAdapter<Ad, AdAdapter.ViewHolder>(
    AdDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val textColor: String = "#" + item.textColor

        var shortDescription: TextView =  holder.itemView.findViewById<TextView>(R.id.picture_text)
        shortDescription.setTextColor(Color.parseColor(textColor))

        if(item.showText!=1)
            shortDescription.isVisible = false

        holder.bind(viewModel, item)

//        doAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: AdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: DiscoverViewModel, item: Ad) {

            binding.viewmodel = viewModel
            binding.ad = item
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(
                    binding
                )
            }
        }

    }

    class AdDiffCallback : DiffUtil.ItemCallback<Ad>() {
        override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean {
            return oldItem.adId == newItem.adId
//            return true
        }

        override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean {
            return oldItem == newItem
        }

    }

}
package com.baryshev.currency.presentation.main.view

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.baryshev.currency.R
import com.baryshev.currency.domain.main.MainData

class CurrenciesAdapter(private val clickListener: ClickListener) : ListAdapter<MainData.Currency, CurrenciesAdapter.ViewHolder>(
        DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MainData.Currency> = object : DiffUtil.ItemCallback<MainData.Currency>() {
            override fun areItemsTheSame(oldItem: MainData.Currency?,
                                         newItem: MainData.Currency?): Boolean = oldItem?.id == newItem?.id

            override fun areContentsTheSame(oldItem: MainData.Currency?,
                                            newItem: MainData.Currency?): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_currency,
                                                               parent,
                                                               false), clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = getItem(position)
        holder.bind(forecast)
    }

    fun getCurrencyByPosition(position: Int): MainData.Currency = getItem(position)

    class ViewHolder(itemView: View,
                     clickListener: ClickListener) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView
        private val tvName: TextView

        init {
            itemView.setOnClickListener { clickListener.onClick(adapterPosition) }
            tvTitle = itemView.findViewById(R.id.tvFromTitle)
            tvName = itemView.findViewById(R.id.tvFromName)
        }


        internal fun bind(currency: MainData.Currency) {
            with(currency) {
                tvTitle.text = this.cc
                tvName.text = this.name
            }
        }
    }

    interface ClickListener {
        fun onClick(pos: Int)
    }
}
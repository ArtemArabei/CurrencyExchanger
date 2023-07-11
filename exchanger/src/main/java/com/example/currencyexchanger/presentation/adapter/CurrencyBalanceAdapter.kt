package com.example.currencyexchanger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyexchanger.databinding.ItemCurrencyBalanceBinding
import com.example.currencyexchanger.domain.model.CurrencyBalance

class CurrencyBalanceAdapter : ListAdapter<CurrencyBalance, CurrencyBalanceViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyBalanceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CurrencyBalanceViewHolder(
            binding = ItemCurrencyBalanceBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyBalanceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {

        private val DIFF_UTIL = object : DiffUtil.ItemCallback<CurrencyBalance>() {

            override fun areItemsTheSame(
                oldItem: CurrencyBalance,
                newItem: CurrencyBalance,
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: CurrencyBalance,
                newItem: CurrencyBalance,
            ): Boolean {
                return oldItem.balance == newItem.balance
            }
        }
    }

}
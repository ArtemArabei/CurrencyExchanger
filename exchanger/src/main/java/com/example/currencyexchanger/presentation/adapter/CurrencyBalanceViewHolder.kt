package com.example.currencyexchanger.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchanger.databinding.ItemCurrencyBalanceBinding
import com.example.currencyexchanger.domain.model.CurrencyBalance
import com.example.currencyexchanger.utils.roundToNDecimalPlacesToString

class CurrencyBalanceViewHolder(
    private val binding: ItemCurrencyBalanceBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CurrencyBalance) = with(binding) {
        currencyBalance.text = item.balance.roundToNDecimalPlacesToString(2)
        currencyName.text = item.name
    }

}
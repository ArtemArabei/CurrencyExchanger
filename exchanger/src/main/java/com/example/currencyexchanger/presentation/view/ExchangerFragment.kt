package com.example.currencyexchanger.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchanger.R
import com.example.currencyexchanger.databinding.FragmentExchangerBinding
import com.example.currencyexchanger.di.ExchangerComponent
import com.example.currencyexchanger.di.ExchangerComponentProvider
import com.example.currencyexchanger.domain.model.ExchangeMessage
import com.example.currencyexchanger.presentation.adapter.CurrencyBalanceAdapter
import com.example.currencyexchanger.presentation.model.RequestResult
import com.example.currencyexchanger.presentation.view.CurrencyConvertedDialogFragment.Companion.TAG
import com.example.currencyexchanger.utils.roundToNDecimalPlacesToString
import com.example.currencyexchanger.utils.showErrorDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangerFragment : Fragment() {

    private var _binding: FragmentExchangerBinding? = null
    private val binding get() = requireNotNull(_binding)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ExchangerViewModel

    private lateinit var exchangerComponent: ExchangerComponent

    private val adapter by lazy {
        CurrencyBalanceAdapter()
    }

    private var isUiReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentExchangerBinding.inflate(inflater, container, false)
            .also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exchangerComponent =
            (requireActivity()
                .applicationContext as ExchangerComponentProvider)
                .provideExchangerComponent()
        exchangerComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangerViewModel::class.java]

        observeCurrencyDataChanges()
        observeBalancesChanges()
        observeCurrenciesToSellChanges()
        observeCurrenciesToReceiveChanges()
        observeExchangeResultMessage()
        initUi()
    }

    private fun observeCurrencyDataChanges() {
        lifecycleScope.launch {
            viewModel.requestResultToShow.collect { result ->
                when (result) {
                    is RequestResult.Success -> {
                        isUiReady = true
                        val currencyExchangeData = result.currencyExchangeData
                        viewModel.receiveCurrencyExchangeRates(currencyExchangeData)
                        viewModel.createStartBalances(currencyExchangeData)
                        viewModel.updateListCurrenciesToReceive()
                        with(binding) {
                            buttonSubmit.setOnClickListener {
                                if (amountToSellEditTextField.text.isNotEmpty()) {
                                    val amountToSell = amountToSellEditTextField.text.toString()
                                    val currencyToSell =
                                        currencyToSellSpinner.selectedItem.toString()
                                    val currencyToReceive =
                                        currencyToReceiveSpinner.selectedItem.toString()
                                    val amountToReceive = viewModel.showConversionInfo(
                                        amountToSell, currencyToSell, currencyToReceive
                                    )
                                    viewModel.makeExchange(
                                        amountToSell.toDouble(),
                                        amountToReceive,
                                        currencyToSell,
                                        currencyToReceive
                                    )
                                    amountToSellEditTextField.setText(R.string.zero_amount_text)
                                    amountToReceiveTextField.setText(R.string.zero_amount_text)
                                }
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeBalancesChanges() {
        lifecycleScope.launch {
            viewModel.currencyBalancesToShow.collect { currencyBalances ->
                if (isUiReady) {
                    viewModel.updateListCurrenciesToSell()
                    val linearLayoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL, false
                    )
                    binding.currencyBalances.adapter = adapter
                    binding.currencyBalances.layoutManager = linearLayoutManager
                    adapter.submitList(currencyBalances.sortedByDescending { it.balance })
                    binding.amountToSellEditTextField.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) = Unit

                        override fun beforeTextChanged(
                            s: CharSequence, start: Int, count: Int, after: Int
                        ) = Unit

                        override fun onTextChanged(
                            s: CharSequence, start: Int, before: Int, count: Int
                        ) {
                            if (binding.amountToSellEditTextField.text.isNotEmpty()) {
                                showConversionInfo()
                            }
                        }
                    })
                }
            }
        }
    }

    private fun observeCurrenciesToSellChanges() {
        lifecycleScope.launch {
            viewModel.currenciesToSellToShow.collect { currenciesToSell ->
                if (isUiReady) {
                    with(binding) {
                        val currencyToSellSpinnerAdapter = ArrayAdapter(
                            requireContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            currenciesToSell
                        )
                        currencyToSellSpinner.adapter = currencyToSellSpinnerAdapter
                        currencyToSellSpinner.onItemSelectedListener =
                            object : OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                ) {
                                    if (amountToSellEditTextField.text.isNotEmpty()) {
                                        showConversionInfo()
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun observeCurrenciesToReceiveChanges() {
        lifecycleScope.launch {
            viewModel.currenciesToReceiveToShow.collect { currenciesToReceive ->
                if (isUiReady) {
                    with(binding) {
                        val currencyToReceiveSpinnerAdapter = ArrayAdapter(
                            requireContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            currenciesToReceive
                        )
                        currencyToReceiveSpinner.adapter = currencyToReceiveSpinnerAdapter
                        currencyToReceiveSpinner.onItemSelectedListener =
                            object : OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                ) {
                                    if (amountToSellEditTextField.text.isNotEmpty()) {
                                        showConversionInfo()
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun observeExchangeResultMessage() {
        lifecycleScope.launch {
            viewModel.exchangeResultToShow.collect {
                if (isUiReady && it is ExchangeMessage.SuccessMessage) {
                    val message = getString(
                        R.string.message_text,
                        it.amountOfCurrencyToSell,
                        it.currencyToSell,
                        it.amountOfCurrencyToReceive,
                        it.currencyToReceive,
                        it.commission
                    )
                    CurrencyConvertedDialogFragment
                        .newInstance(message)
                        .show(childFragmentManager, TAG)
                }
            }
        }
    }

    private fun initUi() {
        with(binding) {
            myBalances.text = getString(R.string.my_balances_text)
            currencyExchange.text = getString(R.string.currency_exchange_text)
            sellIcon.setImageResource(R.drawable.baseline_arrow_circle_up_24)
            receiveIcon.setImageResource(R.drawable.baseline_arrow_circle_down_24)
            sellText.text = getString(R.string.sell_text)
            receiveText.text = getString(R.string.receive_text)
            amountToSellEditTextField.setText(getString(R.string.zero_amount_text))
            amountToReceiveTextField.text = getString(R.string.zero_amount_text)
        }

        viewModel.errorToShow.observe(this.viewLifecycleOwner) {
            showErrorDialog(it)
        }
    }

    private fun showConversionInfo() {
        with(binding) {
            val amountToSell = amountToSellEditTextField.text.toString()
            val currencyToSell = currencyToSellSpinner.selectedItem.toString()
            val currencyToReceive = currencyToReceiveSpinner.selectedItem.toString()
            val amountToReceive = viewModel.showConversionInfo(
                amountToSell, currencyToSell, currencyToReceive
            )
            amountToReceiveTextField.text = amountToReceive.roundToNDecimalPlacesToString(2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



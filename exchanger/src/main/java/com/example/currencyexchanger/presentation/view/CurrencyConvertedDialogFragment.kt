package com.example.currencyexchanger.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.currencyexchanger.R

class CurrencyConvertedDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = requireArguments().getString(ARG_MESSAGE)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.currency_converted_text)
                .setCancelable(true)
                .setMessage(message)
                .setNegativeButton(R.string.done_button_text) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException(context?.getString(R.string.something_went_wrong_text))
    }

    companion object{

        const val ARG_MESSAGE = "MESSAGE_KEY"
        const val TAG = "CURRENCY_CONVERTER_DIALOG_FRAGMENT"
        fun newInstance(message: String): CurrencyConvertedDialogFragment{

            return CurrencyConvertedDialogFragment().apply {
                arguments = bundleOf().apply { putString(ARG_MESSAGE, message) }
            }
        }
    }

}
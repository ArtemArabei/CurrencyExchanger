package com.example.currencyexchanger.utils

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showErrorDialog(message: String) {

    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }

}

fun Double.roundToNDecimalPlacesToString(n: Int) = String.format("%.${n}f", this)

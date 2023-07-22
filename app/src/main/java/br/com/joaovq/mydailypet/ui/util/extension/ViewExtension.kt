package br.com.joaovq.mydailypet.ui.util.extension

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

fun View.visible() {
    this.isVisible = true
}

fun View.gone() {
    this.isVisible = false
}
fun AppCompatActivity.hideKeyboard() {
    val ipmm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    ipmm.hideSoftInputFromWindow(window.currentFocus?.windowToken, 0)
}

package br.com.joaovq.mydailypet.core.util.extension

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

fun Fragment.toast(
    context: Context = requireContext(),
    text: String,
    length: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(context, text, length).show()
}

fun Fragment.snackbar(
    view: View = requireView(),
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    actionText: String = "",
    action: (() -> Unit)? = null,
) {
    val snackbar = Snackbar.make(view, message, length)
    action?.let {
        snackbar.setAction(actionText) {
            action()
        }
    }
    snackbar.show()
}

fun Fragment.simpleDatePickerDialog(action: ((year: Int, month: Int, dayOfMonth: Int) -> Unit)) {
    val datePickerDialog = DatePickerDialog(requireContext())
    val instance = Calendar.getInstance()
    datePickerDialog.setOnDateSetListener { picker, year, month, dayOfMonth ->
        action(year, month, dayOfMonth)
    }
    datePickerDialog.datePicker.maxDate = instance.timeInMillis
    datePickerDialog.show()
}

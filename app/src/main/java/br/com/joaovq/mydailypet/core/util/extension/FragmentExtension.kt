package br.com.joaovq.mydailypet.core.util.extension

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.app.AppMenuItem
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.Date

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

fun Fragment.simpleDatePickerDialog(
    title: String,
    calendarConstraints: CalendarConstraints.Builder? = null,
    @StyleRes theme: Int = R.style.Theme_MyDailyPet_DatePickerOverlay,
    action: ((date: Date) -> Unit),
) {
    val calendarConstraintsBuilder = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .setInputMode(INPUT_MODE_CALENDAR)
        .setCalendarConstraints(
            calendarConstraints?.build() ?: calendarConstraintsBuilder.build(),
        )
        .setTheme(theme)
        .build()
    datePicker.addOnPositiveButtonClickListener { time ->
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        calendar.add(Calendar.DATE, 1)
        action(calendar.time)
    }
    datePicker.show(childFragmentManager, "")
}

fun Fragment.createPopMenu(
    view: View,
    title: String = getString(R.string.see_details_pet_item_menu),
    items: List<AppMenuItem> = listOf(),
): PopupMenu {
    val popUpMenu = PopupMenu(requireContext(), view)
    items.forEach {
        popUpMenu.menu.add(it.title)
    }
    popUpMenu.setOnMenuItemClickListener { item ->
        val first = items.firstOrNull { item.title == getString(it.title) }
        if (first != null) {
            first.action()
            true
        } else {
            false
        }
    }
    return popUpMenu
}

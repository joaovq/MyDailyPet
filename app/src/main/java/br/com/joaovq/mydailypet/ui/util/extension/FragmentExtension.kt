package br.com.joaovq.mydailypet.ui.util.extension

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.LayoutBottomSheetSuccessBinding
import br.com.joaovq.mydailypet.ui.AppMenuItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.TimeZone

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
    calendarConstraints: CalendarConstraints.Builder? =
        CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now()),
    @StyleRes theme: Int = R.style.Theme_MyDailyPet_DatePickerOverlay,
    tag: String = this.javaClass.simpleName,
    action: ((year: Int, month: Int, day: Int) -> Unit),
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .setInputMode(INPUT_MODE_CALENDAR)
        .setCalendarConstraints(
            calendarConstraints?.build(),
        )
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setTheme(theme)
        .build()
    datePicker.addOnPositiveButtonClickListener { time ->
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = time
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        action(year, month, day)
    }
    datePicker.show(childFragmentManager, tag)
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

fun Fragment.simpleBottomSheetDialog(
    text: String,
    description: String = "",
    @DrawableRes imageId: Int = R.drawable.ic_time,
    onDismiss: () -> Unit = {},
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val binding = LayoutBottomSheetSuccessBinding.inflate(layoutInflater)
    binding.ivImageBottomSheet.setImageResource(imageId)
    binding.tvMessageBottomSheetDialog.text = text
    binding.tvDescriptionBottomSheetDialog.text = description
    bottomSheetDialog.setContentView(binding.root)
    lifecycleScope.launch {
        delay(3000)
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.show()
}

fun Fragment.simpleAlertDialog(
    @StringRes title: Int = R.string.app_name_not_trim,
    @StringRes message: Int,
    @StringRes textNegativeButton: Int = R.string.text_negative_button,
    @StringRes textPositiveButton: Int = R.string.text_positive_button,
    actionClickNegativeButton: (() -> Unit) = {},
    actionClickPositiveButton: (() -> Unit) = {},
) {
    val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
    dialogBuilder.setTitle(title)
    dialogBuilder.setMessage(message)
    dialogBuilder.setNegativeButton(textNegativeButton) { dialog, _ ->
        actionClickNegativeButton()
        dialog.dismiss()
    }
    dialogBuilder.setPositiveButton(textPositiveButton) { dialog, _ ->
        actionClickPositiveButton()
        dialog.dismiss()
    }
    dialogBuilder.show()
}

fun Fragment.simpleTimePicker(
    @StringRes title: Int = R.string.text_add_time,
    tag: String = this.javaClass.simpleName,
    @TimeFormat timeFormat: Int = TimeFormat.CLOCK_24H,
    inputMode: Int = INPUT_MODE_CLOCK,
    onPositiveButtonClick: (hour: Int, minute: Int) -> Unit,
) {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(timeFormat)
        .setTitleText(title)
        .setInputMode(inputMode)
        .build()
    timePicker.addOnPositiveButtonClickListener {
        Timber.tag(tag).e(timePicker.hour.toString())
        Timber.tag(tag).e(timePicker.minute.toString())
        onPositiveButtonClick(timePicker.hour, timePicker.minute)
    }
    timePicker.show(childFragmentManager, tag)
}

fun Fragment.createHelpDialog(
    @DrawableRes icon: Int? = R.drawable.ic_round_logo_2,
    @StringRes message: Int,
    @StringRes textButton: Int = R.string.text_positive_button,
    onClickPositiveButton: (DialogInterface, Int) -> Unit = { dialog, i -> },
) {
    val alertDialog = MaterialAlertDialogBuilder(requireContext())
    icon?.let {
        alertDialog.setIcon(it)
    }
    alertDialog.setTitle(R.string.app_name_not_trim)
    alertDialog.setMessage(
        message,
    )
    alertDialog.setNegativeButton(textButton, onClickPositiveButton)
    alertDialog.show()
}

fun Fragment.goToSettingsAlertDialogForPermission(
    @StringRes message: Int,
) {
    simpleAlertDialog(
        message = message,
        textPositiveButton = R.string.text_goto_settings,
    ) {
        val uri = Uri.fromParts(
            "package",
            requireActivity().packageName,
            null,
        )
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(uri)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(
                intent,
            )
        }
    }
}

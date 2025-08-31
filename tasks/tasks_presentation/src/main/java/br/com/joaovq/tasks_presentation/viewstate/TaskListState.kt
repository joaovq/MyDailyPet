package br.com.joaovq.tasks_presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.tasks_domain.model.Task

sealed class TaskListState(
    @StringRes val message: Int,
) {
    data class Success(
        val data: List<Task>,
        private val messageSuccess: Int = br.com.joaovq.core_ui.R.string.message_success,
    ) :
        TaskListState(messageSuccess)

    object SubmittedSuccess : TaskListState(br.com.joaovq.core_ui.R.string.message_success)
    object DeleteSuccess : TaskListState(br.com.joaovq.core_ui.R.string.message_success)
    object UpdateSuccess : TaskListState(br.com.joaovq.core_ui.R.string.message_success)
    data class Error(
        val exception: Throwable,
        private val messageError: Int = br.com.joaovq.core_ui.R.string.message_error,
    ) :
        TaskListState(messageError)
}

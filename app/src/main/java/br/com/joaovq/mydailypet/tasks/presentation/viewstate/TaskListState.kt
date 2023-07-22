package br.com.joaovq.mydailypet.tasks.presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.tasks.domain.model.Task

sealed class TaskListState(
    @StringRes val message: Int,
) {
    data class Success(
        val data: List<Task>,
        private val messageSuccess: Int = R.string.message_success,
    ) :
        TaskListState(messageSuccess)

    object SubmittedSuccess : TaskListState(R.string.message_success)
    object DeleteSuccess : TaskListState(R.string.message_success)
    object UpdateSuccess : TaskListState(R.string.message_success)
    data class Error(
        val exception: Exception,
        private val messageError: Int = R.string.message_error,
    ) :
        TaskListState(messageError)
}

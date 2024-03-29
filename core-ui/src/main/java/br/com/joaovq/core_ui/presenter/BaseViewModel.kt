package br.com.joaovq.core_ui.presenter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<I, S> : ViewModel() {
    protected abstract val _state: MutableStateFlow<S>
    abstract fun dispatchIntent(intent: I)
}

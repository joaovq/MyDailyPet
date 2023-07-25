package br.com.joaovq.mydailypet.ui

import android.text.Editable
import android.text.TextWatcher

class TextWatcherProvider(
    private val actionBeforeTextChanged: ((text: CharSequence?) -> Unit)? = null,
    private val actionOnTextChanged: ((text: CharSequence?) -> Unit)? = null,
    private val actionAfterTextChanged: ((editable: Editable?) -> Unit)? = null,
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        actionBeforeTextChanged?.let {
            it(p0)
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        actionOnTextChanged?.let {
            it(p0)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        actionAfterTextChanged?.let {
            it(p0)
        }
    }
}

package com.example.numberstesttask.numbers.presentaion

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

sealed class UiState {

    abstract fun apply(inputLayout: TextInputLayout, inputEditText: TextInputEditText)

    class Success() : UiState() {

        override fun apply(inputLayout: TextInputLayout, inputEditText: TextInputEditText) =
            inputEditText.setText("")
    }

    abstract class AbstractError(
        private val message: String,
        private val errorEnabled: Boolean
    ) : UiState() {

        override fun apply(inputLayout: TextInputLayout, inputEditText: TextInputEditText) =
            with(inputLayout) {
                isErrorEnabled = errorEnabled
                error = message
            }
    }

    data class ShowError(private val message: String) : AbstractError(message, true)

    class ClearError : AbstractError("", false)
}
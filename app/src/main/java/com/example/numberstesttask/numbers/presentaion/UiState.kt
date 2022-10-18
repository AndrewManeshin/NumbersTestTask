package com.example.numberstesttask.numbers.presentaion

sealed class UiState {

    class Success() : UiState() {

    }

    data class Error(private val message: String) : UiState() {

    }
}
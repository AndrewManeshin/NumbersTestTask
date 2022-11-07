package com.example.numberstesttask.details.presentation

import androidx.lifecycle.ViewModel
import com.example.numberstesttask.details.data.NumberFactDetails

class NumberDetailsViewModel(
    private val data: NumberFactDetails.Read
) : ViewModel(), NumberFactDetails.Read {

    override fun read(): String = data.read()
}
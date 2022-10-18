package com.example.numberstesttask.numbers.presentaion

import android.widget.TextView

data class NumberUi(
    private val id: String,
    private val fact: String
) {

    fun map(head: TextView, subtitle: TextView) {
        head.text = id
        subtitle.text = fact
    }
}
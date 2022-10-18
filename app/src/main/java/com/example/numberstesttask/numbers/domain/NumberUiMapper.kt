package com.example.numberstesttask.numbers.domain

import com.example.numberstesttask.numbers.presentaion.NumberUi

class NumberUiMapper : NumberFact.Mapper<NumberUi> {
    override fun map(id: String, fact: String) = NumberUi(id, fact)
}
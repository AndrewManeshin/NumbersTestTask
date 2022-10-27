package com.example.numberstesttask.numbers.data

import com.example.numberstesttask.numbers.domain.NumberFact

class NumberDataToDomain: NumberData.Mapper<NumberFact> {
    override fun map(id: String, fact: String) = NumberFact(id, fact)
}
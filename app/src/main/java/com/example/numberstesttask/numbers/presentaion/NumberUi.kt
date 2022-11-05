package com.example.numberstesttask.numbers.presentaion

import android.widget.TextView

data class NumberUi(private val id: String, private val fact: String) : Mapper<Boolean, NumberUi> {

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, fact)

    interface Mapper<T> {
        fun map(id: String, fact: String): T
    }

    override fun map(source: NumberUi) = source.id == id
}

class DetailsUi : NumberUi.Mapper<String> {
    override fun map(id: String, fact: String) = "$id\n\n$fact"
}

class ListItemUi(
    private val title: TextView,
    private val subtitle: TextView
) : NumberUi.Mapper<Unit> {

    override fun map(id: String, fact: String) {
        title.text = id
        subtitle.text = fact
    }
}
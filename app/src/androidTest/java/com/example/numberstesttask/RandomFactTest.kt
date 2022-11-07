package com.example.numberstesttask

import org.junit.Test

class RandomFactTest: BaseTest() {

    @Test
    fun test() = Page.Numbers().run {
        getRandomFactButton.view().click()
        with(recycler) {
            viewInRecycler(0, titleItem).checkText("1")
            viewInRecycler(0, subtitleItem).checkText("fact about 1")
            getRandomFactButton.view().click()
            viewInRecycler(0, titleItem).checkText("2")
            viewInRecycler(0, subtitleItem).checkText("fact about 2")
            viewInRecycler(1, titleItem).checkText("1")
            viewInRecycler(1, subtitleItem).checkText("fact about 1")
        }
    }
}
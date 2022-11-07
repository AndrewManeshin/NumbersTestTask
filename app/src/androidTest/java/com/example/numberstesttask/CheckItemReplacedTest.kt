package com.example.numberstesttask

import org.junit.Test

class CheckItemReplacedTest : BaseTest() {

    @Test
    fun test_history(): Unit = Page.Numbers().run {

        input.view().typeText("1")
        getFactButton.view().click()
        with(recycler) {
            viewInRecycler(0, titleItem).checkText("1")
            viewInRecycler(0, subtitleItem).checkText("fact about 1")
        }

        input.view().typeText("2")
        getFactButton.view().click()
        with(recycler) {
            viewInRecycler(0, titleItem).checkText("2")
            viewInRecycler(0, subtitleItem).checkText("fact about 2")
            viewInRecycler(1, titleItem).checkText("1")
            viewInRecycler(1, subtitleItem).checkText("fact about 1")
        }

        input.view().typeText("1")
        getFactButton.view().click()
        with(recycler) {
            viewInRecycler(0, titleItem).checkText("1")
            viewInRecycler(0, subtitleItem).checkText("fact about 1")
            viewInRecycler(1, titleItem).checkText("2")
            viewInRecycler(1, subtitleItem).checkText("fact about 2")
        }
    }
}
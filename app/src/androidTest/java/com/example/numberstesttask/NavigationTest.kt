package com.example.numberstesttask

import androidx.test.espresso.Espresso.pressBack
import org.junit.Test

class NavigationTest : BaseTest() {

    @Test
    fun details_navigation() {

        val numbersPage = Page.Numbers()
        numbersPage.run {
            input.view().typeText("10")
            getFactButton.view().click()
            with(recycler) {
                viewInRecycler(0, titleItem).checkText("10")
                viewInRecycler(0, subtitleItem).checkText("fact about 10")
                viewInRecycler(0, subtitleItem).click()
            }
        }

        Page.Details().details.view().checkText("10\n\nfact about 10")

        pressBack()

        numbersPage.run {
            recycler.viewInRecycler(0, titleItem).checkText("10")
            recycler.viewInRecycler(0, subtitleItem).checkText("fact about 10")
        }
    }
}
package com.example.numberstesttask

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.numberstesttask.main.presentaion.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest : BaseTest() {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

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
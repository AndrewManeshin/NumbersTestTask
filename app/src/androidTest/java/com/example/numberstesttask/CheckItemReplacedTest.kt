package com.example.numberstesttask

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.numberstesttask.main.presentaion.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckItemReplacedTest : BaseTest() {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

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
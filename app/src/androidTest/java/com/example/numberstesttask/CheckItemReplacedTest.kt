package com.example.numberstesttask

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.numberstesttask.main.presentaion.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckItemReplacedTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_history() {
        //1. Enter 10

        onView(ViewMatchers.withId(R.id.inputEditText)).perform(ViewActions.typeText("10"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.getFactButton)).perform(ViewActions.click())

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.titleTextView)
        ).check(matches(withText("10")))

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.subtitleTextView)
        ).check(matches(withText("fact about 10")))

        //2. Enter 20
        onView(ViewMatchers.withId(R.id.inputEditText)).perform(ViewActions.typeText("20"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.getFactButton)).perform(ViewActions.click())

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.titleTextView)
        ).check(matches(withText("20")))
        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.subtitleTextView)
        ).check(matches(withText("fact about 20")))

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(1, R.id.titleTextView)
        ).check(matches(withText("10")))
        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(1, R.id.subtitleTextView)
        ).check(matches(withText("fact about 10")))

        //3. Enter 10 again
        onView(ViewMatchers.withId(R.id.inputEditText)).perform(ViewActions.typeText("10"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.getFactButton)).perform(ViewActions.click())

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.titleTextView)
        ).check(matches(withText("10")))
        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(0, R.id.subtitleTextView)
        ).check(matches(withText("fact about 10")))

        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(1, R.id.titleTextView)
        ).check(matches(withText("20")))
        onView(
            RecyclerViewMatcher(R.id.historyRecyclerView)
                .atPosition(1, R.id.subtitleTextView)
        ).check(matches(withText("fact about 20")))
    }
}
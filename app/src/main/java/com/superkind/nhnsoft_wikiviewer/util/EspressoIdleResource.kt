package com.superkind.nhnsoft_wikiviewer.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdleResource {
    private const val IDLING_NAME = "GLOBAL"
    @JvmField val countingIdlingResource = CountingIdlingResource(IDLING_NAME)
    fun increment(){
        countingIdlingResource.increment()
    }
    fun decrement(){
        if(!countingIdlingResource.isIdleNow){
            countingIdlingResource.decrement()
        }
    }
}
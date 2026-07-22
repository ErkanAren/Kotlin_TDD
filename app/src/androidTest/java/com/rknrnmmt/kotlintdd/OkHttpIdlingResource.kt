package com.rknrnmmt.kotlintdd

import androidx.test.espresso.IdlingResource
import okhttp3.OkHttpClient

class OkHttpIdlingResource(
    private val client: OkHttpClient
) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName() = "OkHttp"

    override fun isIdleNow(): Boolean {
        val idle = client.dispatcher.runningCallsCount() == 0
        if (idle) callback?.onTransitionToIdle()
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
        client.dispatcher.idleCallback = Runnable {
            callback.onTransitionToIdle()
        }
    }

    fun close() {
        callback = null
        client.dispatcher.idleCallback = null
    }
}
package com.rknrnmmt.kotlintdd.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import petros.efthymiou.groovy.utils.MainCoroutineScopeRule

open class BaseUnitTest {

    @get:Rule
    var coroutineScopeRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}
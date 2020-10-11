package com.deange.mechnotifier.dagger

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Scope
@Target(CLASS, FUNCTION)
@Retention(BINARY)
annotation class SingleInApp

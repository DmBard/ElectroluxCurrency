package com.baryshev.currency.di.vm

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MapKey
@MustBeDocumented
@Retention(RUNTIME)
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

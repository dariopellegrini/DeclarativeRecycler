package com.dariopellegrini.declarativerecycler

import kotlin.reflect.KProperty

open class DifferentiableDelegate<T>(val configure: () -> DiffRecyclerManager<T>) where T: Row, T: Differentiable<T> {
    var value = listOf<T>()

    private val diffRecyclerManager: DiffRecyclerManager<T> by lazy {
        configure()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
        this.value = value
        diffRecyclerManager.reload(value)
    }
}
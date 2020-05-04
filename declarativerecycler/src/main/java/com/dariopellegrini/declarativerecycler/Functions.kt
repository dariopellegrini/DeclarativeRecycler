package com.dariopellegrini.declarativerecycler

fun <T>bind(configure: () -> DiffRecyclerManager<T>) where T: Row, T: Differentiable<T> = DifferentiableDelegate(configure)
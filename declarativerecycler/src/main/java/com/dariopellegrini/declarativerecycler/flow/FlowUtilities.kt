package com.dariopellegrini.declarativerecycler.flow

import com.dariopellegrini.declarativerecycler.DiffRecyclerManager
import com.dariopellegrini.declarativerecycler.Differentiable
import com.dariopellegrini.declarativerecycler.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T> Flow<List<T>>.bind(diffRecyclerManager: DiffRecyclerManager<T>) where T : Row, T : Differentiable<T> =
        this.onEach {
            diffRecyclerManager.reload(it)
        }
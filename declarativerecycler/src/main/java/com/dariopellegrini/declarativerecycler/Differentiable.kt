package com.dariopellegrini.declarativerecycler

interface Differentiable<T> {
    fun isTheSame(new: T): Boolean
    fun hasSameContent(new: T): Boolean
}
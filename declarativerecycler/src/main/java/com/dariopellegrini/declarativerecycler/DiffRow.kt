package com.dariopellegrini.declarativerecycler

interface DiffRow: Row, Differentiable<DiffRow> {
    val id: Any
    val content: Any

    override fun isTheSame(new: DiffRow): Boolean {
        return this.id == new.id
    }

    override fun hasSameContent(new: DiffRow): Boolean {
        return this.content == new.content
    }
}

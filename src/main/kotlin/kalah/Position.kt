package kalah

data class Position(private val index: Int) {

    init {
        if (!(0 until SIZE).contains(index)) {
            throw IllegalArgumentException("$index is out of range")
        }
    }

    fun next(turn: Turn): Position {
        return when (Position((index + 1) % SIZE)) {
            turn.opponent_kalah() -> Position((index + 2) % SIZE)
            else -> Position((index + 1) % SIZE)
        }
    }

    fun opposite(): Position {
        if (this == Turn.FIRST.kalah() || this == Turn.SECOND.kalah()) {
            throw IllegalArgumentException()
        }
        return Position(12 - index)
    }

    fun <E> getAt(list: List<E>): E {
        return list[index]
    }

    fun <E> setAt(list: List<E>, value: E): List<E> {
        return list.mapIndexed { index, e ->
            when (index) {
                this.index -> value
                else -> e
            }
        }
    }

    override fun toString(): String {
        return "Position($index)"
    }
}
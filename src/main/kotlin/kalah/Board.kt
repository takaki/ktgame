package kalah

data class Board(private val list: List<Int>) {
    companion object {
        fun init(n: Int): Board {
            return Board((0 until SIZE).map {
                when (Position(it)) {
                    Turn.FIRST.kalah(), Turn.SECOND.kalah() -> 0
                    else -> n
                }
            })
        }
    }

    init {
        if (list.size != SIZE) {
            throw IllegalArgumentException("incorrect list size")
        }
    }

    private fun clear(pos: Position): Board {
        return Board(pos.setAt(list, 0))
    }

    private fun put(pos: Position, num: Int): Board {
        return Board(pos.setAt(list, pos.getAt(list) + num))
    }

    fun distribute(turn: Turn, pos: Position):
            Pair<Board, Position> {
        return with(pos.getAt(list)) {
            if (this == 0) {
                throw IllegalArgumentException("$pos is empty.")
            }
            0 until this
        }.fold(Pair(this.clear(pos), pos)) { (b, p), _ ->
            with(p.next(turn)) {
                Pair(b.put(this, 1), this)
            }
        }
    }

    private fun canCapture(pos: Position, turn: Turn): Boolean {
        return turn.inRange(pos) && pos.getAt(list) == 1 && pos.opposite().getAt(list) > 0
    }

    fun tryCaptureStone(pos: Position, turn: Turn): Board {
        return if (canCapture(pos, turn)) {
            this.clear(pos).clear(pos.opposite()).put(turn.kalah(), pos.opposite().getAt(list) + 1)
        } else {
            this
        }
    }

    fun gatherStonesIntoKalah(): Board {
        return Turn.values().firstOrNull { countOwnStones(it) == 0 }?.let {
            with(it.opponent()) {
                this.range().fold(this@Board) { b, p ->
                    b.clear(p)
                }.put(this.kalah(), countOwnStones(this))
            }
        } ?: this

    }

    fun countOwnStones(turn: Turn): Int {
        return turn.range().map { it.getAt(list) }.sum()
    }

    fun isFinish(): Boolean {
        return countOwnStones(Turn.FIRST) + countOwnStones(Turn.SECOND) == 0
    }

    fun score(turn: Turn): Int {
        return turn.kalah().getAt(list)
    }

    fun count(position: Position): Int {
        return position.getAt(list)
    }

    override fun toString(): String {
        return "${list.subList(0, 6).joinToString(":")}[${list[6]}]" +
                "${list.subList(7, 13).joinToString(":")}[${list[13]}]"
    }

}
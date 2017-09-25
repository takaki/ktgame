package kalah

enum class Turn {
    FIRST {
        override fun opponent(): Turn {
            return SECOND
        }

        override fun range(): List<Position> {
            return IntRange(0, 5).map { Position(it) }

        }

        override fun kalah(): Position {
            return Position(6)
        }
    },
    SECOND {
        override fun opponent(): Turn {
            return FIRST
        }

        override fun range(): List<Position> {
            return IntRange(7, 12).map { Position(it) }

        }

        override fun kalah(): Position {
            return Position(13)
        }
    };

    abstract fun kalah(): Position
    abstract fun range(): List<Position>
    abstract fun opponent(): Turn

    fun inRange(position: Position): Boolean {
        return range().contains(position)
    }

    fun isKalah(position: Position): Boolean {
        return kalah() == position
    }

    fun opponent_kalah(): Position {
        return opponent().kalah()
    }

}
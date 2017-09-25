package kalah

const val SIZE = 14
const val STONE = 72
const val EVEN = STONE / 2

const val MAX_VALUE = 100
const val MIN_VALUE = -100
const val FIRST_WIN = 99
const val SECOND_WIN = -99

const val NORMAL = 0
const val KALAH = 1
const val GAMEOVER = 2

enum class GameResult {
    GAME_OVER,
    CONTINUE
}

class GameState(val board: Board, val turn: Turn) {
    companion object {
        var count = 0
        fun init(n: Int): GameState {
            return GameState(Board.init(n), Turn.FIRST)
        }
    }


    fun moveStone(pos: Position): GameState {
        if (!turn.inRange(pos)) {
            throw IllegalArgumentException("Illegal move.")
        }
        return board.distribute(turn, pos).let { (b, p) ->
            Pair(b.tryCaptureStone(p, turn), p)
        }.let { (b, p) ->
            Pair(b, if (turn.isKalah(p)) turn else turn.opponent())
        }.let { (board, t) ->
            GameState(board.gatherStonesIntoKalah(), t)
        }
    }

    fun validMoves(): List<Position> {
        return turn.range().filter { position ->
            board.count(position) > 0
        }
    }

    fun result(): GameResult {
        val n1 = board.score(Turn.FIRST)
        val n2 = board.score(Turn.SECOND)
        return when {
            board.isFinish() -> GameResult.GAME_OVER
            n1 > EVEN -> GameResult.GAME_OVER
            n2 > EVEN -> GameResult.GAME_OVER
            else -> GameResult.CONTINUE
        }
    }

    fun evaluate(): Int {
        count += 1
        val n1 = board.score(Turn.FIRST)
        val n2 = board.score(Turn.SECOND)
        return when {
            n1 > EVEN -> FIRST_WIN
            n2 > EVEN -> SECOND_WIN
            n1 == EVEN && n2 == EVEN -> 0
            else -> n1 - n2
        }
    }

    override fun toString(): String {
        return "$board($turn)"
    }

}
/*
# 評価関数の呼び出し回数
count = 0

# debug 用
def print_count(): print count
 */
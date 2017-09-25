package kalah

fun main(args: Array<String>) {
    val depth = 7
    val gs = GameState.init(4)
    val (v, moves) = PlayNegaScout().play(depth, gs)
    moves.fold(gs) { g, m ->
        g.moveStone(m).apply { println("$m ${g.turn.toString().first()} ${this.board}") }
    }
    println(v)
    println(GameState.count)
}

class PlayNegaScout {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return negaScout(depth, gameState, MIN_VALUE, MAX_VALUE)
    }

    private fun flag(gameState: GameState): Int = if (gameState.turn == Turn.FIRST) 1 else -1

    private fun negaScout(depth: Int, gameState: GameState, alpha: Int, beta: Int): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate() * flag(gameState), emptyList<Position>())
        } else {
            gameState.validMoves().fold(Pair(alpha, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate() * flag(gameState), listOf())
                    GameResult.CONTINUE -> when {
                        gameState.turn == newgs.turn -> negaScout(depth, newgs, maxOf(alpha, value), beta)
                        else -> {
                            val a = maxOf(alpha, value)
                            val x = negaScout(depth - 1, newgs, -(a + 1), -a).let { (v, moves) -> Pair(-v, moves) }
                            if (a < x.first && x.first < beta) {
                                negaScout(depth - 1, newgs, -beta, -x.first).let { (v, moves) -> Pair(-v, moves) }
                            } else {
                                x
                            }
                        }
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }.let {
                    maxOf(Pair(value, pv), it, compareBy { it.first }).apply {
                        if (first >= beta) {
                            return this
                        }
                    }
                }
            }
        }

    }

}
package kalah

fun main(args: Array<String>) {
    val depth = 7
    val gs = GameState.init(4)
    val (v, moves) = PlayFailSoftNegaAlpha().play(depth, gs)
    moves.fold(gs) { g, m ->
        g.moveStone(m).apply { println("$m ${g.turn.toString().first()} ${this.board}") }
    }
    println(v)
    println(GameState.count)
}

class PlayFailSoftNegaAlpha {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return negaAlpha(depth, gameState, MIN_VALUE, MAX_VALUE)
    }

    private fun flag(gameState: GameState): Int = if (gameState.turn == Turn.FIRST) 1 else -1

    private fun negaAlpha(depth: Int, gameState: GameState, alpha: Int, beta: Int): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate() * flag(gameState), emptyList<Position>())
        } else {
            gameState.validMoves().fold(Pair(alpha, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                val (v, moves) = when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate() * flag(gameState), listOf())
                    GameResult.CONTINUE -> when {
                        gameState.turn == newgs.turn -> negaAlpha(depth, newgs, maxOf(alpha, value), beta)
                        else -> negaAlpha(depth - 1, newgs, -beta, -maxOf(alpha, value)).let { (v, moves) -> Pair(-v, moves) }
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }
                maxOf(Pair(value, pv), Pair(v, moves), compareBy { it.first }).apply {
                    if (first >= beta) {
                        return this
                    }
                }
            }
        }

    }

}






package kalah

class PlayNegaAlpha {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return negaAlpha(depth, gameState, MAX_VALUE)
    }

    private fun flag(gameState: GameState): Int = if (gameState.turn == Turn.FIRST) 1 else -1

    private fun negaAlpha(depth: Int, gameState: GameState, limit: Int): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate() * flag(gameState), emptyList<Position>())
        } else {
            gameState.validMoves().fold(Pair(MIN_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                val (v, moves) = when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate() * flag(gameState), listOf())
                    GameResult.CONTINUE -> when {
                        gameState.turn == newgs.turn -> negaAlpha(depth, newgs, limit)
                        else -> negaAlpha(depth - 1, newgs, -value).let { (v, moves) -> Pair(-v, moves) }
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }
                maxOf(Pair(value, pv), Pair(v, moves), compareBy { it.first }).apply {
                    if (first >= limit) {
                        return this
                    }
                }
            }
        }

    }

}

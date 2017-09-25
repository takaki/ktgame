package kalah.play

import kalah.*

class PlayNegaMax {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return negamax(depth, gameState)
    }

    private fun flag(gameState: GameState): Int = if (gameState.turn == Turn.FIRST) 1 else -1

    private fun negamax(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate() * flag(gameState), emptyList<Position>())
        } else {
            gameState.validMoves().fold(Pair(MIN_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate(), listOf())
                    GameResult.CONTINUE -> when {
                        gameState.turn == newgs.turn -> negamax(depth, newgs)
                        else -> negamax(depth - 1, newgs).let { (v, moves) -> Pair(-v, moves) }
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }.let {
                    maxOf(Pair(value, pv), it, compareBy { it.first })
                }
            }
        }
    }
}
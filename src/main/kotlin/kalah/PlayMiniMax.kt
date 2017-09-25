package kalah

class PlayMiniMax {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return when (gameState.turn) {
            Turn.FIRST -> moveFirst(depth, gameState)
            Turn.SECOND -> moveSecond(depth, gameState)
        }
    }

    private fun moveFirst(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate(), emptyList())
        } else {
            gameState.validMoves().fold(Pair(MIN_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                val (v, moves) = when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate(), listOf())
                    GameResult.CONTINUE -> when (newgs.turn) {
                        Turn.FIRST -> moveFirst(depth, newgs)
                        Turn.SECOND -> moveSecond(depth - 1, newgs)
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }
                maxOf(Pair(value, pv), Pair(v, moves), compareBy { it.first })
            }
        }
    }

    private fun moveSecond(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate(), emptyList())
        } else {
            gameState.validMoves().fold(Pair(MAX_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                val (v, moves) = when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate(), listOf())
                    GameResult.CONTINUE -> when (newgs.turn) {
                        Turn.FIRST -> moveFirst(depth - 1, newgs)
                        Turn.SECOND -> moveSecond(depth, newgs)
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }
                minOf(Pair(value, pv), Pair(v, moves), compareBy { it.first })
            }
        }
    }
}

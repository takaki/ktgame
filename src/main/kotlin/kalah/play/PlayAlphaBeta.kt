package kalah.play

import kalah.*


class PlayAlphaBeta {
    fun play(depth: Int, gameState: GameState): Pair<Int, List<Position>> {
        return when (gameState.turn) {
            Turn.FIRST -> moveFirst(depth, gameState, MAX_VALUE)
            Turn.SECOND -> moveSecond(depth, gameState, MIN_VALUE)
        }
    }

    fun moveFirst(depth: Int, gameState: GameState, limit: Int): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate(), emptyList())
        } else {
            gameState.validMoves().fold(Pair(MIN_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate(), listOf())
                    GameResult.CONTINUE -> when (newgs.turn) {
                        Turn.FIRST -> moveFirst(depth, newgs, limit)
                        Turn.SECOND -> moveSecond(depth - 1, newgs, value)
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }.let {
                    maxOf(Pair(value, pv), it, compareBy { it.first }).apply {
                        if (first >= limit) {
                            return this
                        }
                    }
                }

            }
        }

    }

    fun moveSecond(depth: Int, gameState: GameState, limit: Int): Pair<Int, List<Position>> {
        return if (depth == 0) {
            Pair(gameState.evaluate(), emptyList())
        } else {
            gameState.validMoves().fold(Pair(MAX_VALUE, emptyList())) { (value, pv), move ->
                val newgs = gameState.moveStone(move)
                when (newgs.result()) {
                    GameResult.GAME_OVER -> Pair(newgs.evaluate(), listOf())
                    GameResult.CONTINUE -> when (newgs.turn) {
                        Turn.FIRST -> moveFirst(depth - 1, newgs, value)
                        Turn.SECOND -> moveSecond(depth, newgs, limit)
                    }
                }.let { (v, moves) ->
                    Pair(v, listOf(move) + moves)
                }.let {
                    minOf(Pair(value, pv), it, compareBy { it.first }).apply {
                        if (first <= limit) {
                            return this
                        }
                    }
                }
            }
        }

    }
}
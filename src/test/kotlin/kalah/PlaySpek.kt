package kalah

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object PleySpek : Spek({
    describe("Play") {
        on("depth=1, stone=4") {
            val depth = 1
            val gs = GameState.init(4)
            val moves = listOf(2, 3).map { Position(it) }
            it("MiniMax") {
                PlayMiniMax().play(depth, gs).second shouldEqual moves
            }
            it("AlphaBeta") {
                PlayAlphaBeta().play(depth, gs).second shouldEqual moves
            }
            it("NegaMax") {
                PlayNegaMax().play(depth, gs).second shouldEqual moves
            }
            it("NegaAlpha") {
                PlayNegaAlpha().play(depth, gs).second shouldEqual moves
            }
        }

        on("depth=1, stone=4 second start") {
            val depth = 1
            val gs = GameState(Board.init(4), Turn.SECOND)
            val moves = listOf(9, 10).map { Position(it) }
            it("MiniMax") {
                PlayMiniMax().play(depth, gs).second shouldEqual moves
            }
            it("AlphaBeta") {
                PlayAlphaBeta().play(depth, gs).second shouldEqual moves
            }
            it("NegaMax") {
                PlayNegaMax().play(depth, gs).second shouldEqual moves
            }
            it("NegaAlpha") {
                PlayNegaAlpha().play(depth, gs).second shouldEqual moves
            }
        }

        on("depth=2, stone=4") {
            val depth = 2
            val gs = GameState.init(4)
            val moves = listOf(2, 4, 8, 9).map { Position(it) }
            it("MiniMax") {
                PlayMiniMax().play(depth, gs).second shouldEqual moves
            }
            it("AlphaBeta") {
                PlayAlphaBeta().play(depth, gs).second shouldEqual moves
            }
            it("NegaMax") {
                PlayNegaMax().play(depth, gs).second shouldEqual moves
            }
            it("NegaAlpha") {
                PlayNegaAlpha().play(depth, gs).second shouldEqual moves
            }
        }

        on("depth=4, stone=4") {
            val depth = 4
            val gs = GameState.init(4)
            val moves = listOf(5, 8, 7, 2, 5, 3, 9).map { Position(it) }
            val value = 2
            it("MiniMax") {
                PlayMiniMax().play(depth, gs).second shouldEqual moves
            }
            it("AlphaBeta") {
                PlayAlphaBeta().play(depth, gs).second shouldEqual moves
            }
            it("NegaMax") {
                PlayNegaMax().play(depth, gs).second shouldEqual moves
            }
            it("NegaAlpha") {
                PlayNegaAlpha().play(depth, gs).second shouldEqual moves
            }
            it("FailSoftNegaAlpha") {
                PlayFailSoftNegaAlpha().play(depth, gs).first shouldEqual value
                PlayFailSoftNegaAlpha().play(depth, gs).second shouldEqual moves
            }
            it("NegaScout") {
                PlayNegaScout().play(depth, gs).first shouldEqual value
            }
        }

    }
})
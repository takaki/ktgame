package kalah

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object BoardSpek : Spek({
    describe("Board") {
        on("init(4)") {
            val b0 = Board.init(4)

            it("初期状態になる") {
                b0 shouldEqual Board(listOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0))
            }
        }
        on("0で分配") {
            val (b0, _) = Board(listOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0))
                    .distribute(Turn.FIRST, Position(0))
            it("分配されている") {
                b0 shouldEqual Board(listOf(0, 5, 5, 5, 5, 4, 0, 4, 4, 4, 4, 4, 4, 0))
            }
        }
        on("相手kalahを通過") {
            val (b0, _) = Board(listOf(0, 0, 0, 0, 0, 8, 1, 0, 0, 0, 0, 0, 0, 1))
                    .distribute(Turn.FIRST, Position(5))
            it("分配されている") {
                b0 shouldEqual Board(listOf(1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1))
            }
        }
    }
})
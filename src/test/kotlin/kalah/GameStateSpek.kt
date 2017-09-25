package kalah

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object GameStateSpek : Spek({
    describe("GameState") {
        GameState.init(4).apply {
            on("初期状態") {
                it("合法手は6種類") {
                    validMoves() shouldEqual Turn.FIRST.range()
                }
                IntRange(6, 13).forEach {
                    it("先手が不正な${it}を指定すると例外をスローする") {
                        { moveStone(Position(it)) } shouldThrow IllegalArgumentException::class
                    }
                }
            }
        }.moveStone(Position(5)).apply {
            on("F5") {
                it("手番が後手に移動する") {
                    turn shouldEqual Turn.SECOND
                }
                it("分配される") {
                    board shouldEqual Board(listOf(4, 4, 4, 4, 4, 0, 1, 5, 5, 5, 4, 4, 4, 0))
                }
            }
        }.apply {
            on("F5後の非合法手") {
                it("合法手は6種類") {
                    validMoves() shouldEqual Turn.SECOND.range()
                }
                ((0..6) + 13).forEach {
                    it("後手が不正な${it}を指定すると例外をスローする") {
                        { moveStone(Position(it)) } shouldThrow IllegalArgumentException::class
                    }
                }
            }

        }.moveStone(Position(8)).apply {
            on("F5->S8") {
                it("手番が後手のまま") {
                    turn shouldEqual Turn.SECOND
                }
                it("分配される") {
                    board shouldEqual Board(listOf(4, 4, 4, 4, 4, 0, 1, 5, 0, 6, 5, 5, 5, 1))
                }
                it("合法手は8-12") {
                    validMoves() shouldEqual listOf(7, 9, 10, 11, 12).map { Position(it) }
                }

            }
        }.moveStone(Position(12)).apply {
            on("F5->S8->S12") {
                it("次の手番が先手") {
                    turn shouldEqual Turn.FIRST
                }
                it("分配される") {
                    board shouldEqual Board(listOf(5, 5, 5, 5, 4, 0, 1, 5, 0, 6, 5, 5, 0, 2))
                }
            }
        }.apply {
            on("f5->s8->s12後の非合法手") {
                it("空白の5を指定すると例外を出す") {
                    { moveStone(Position(5)) } shouldThrow IllegalArgumentException::class
                }
            }
        }.moveStone(Position(0)).apply {
            on("F5->S8->S12->F0") {
                it("次の手番が後手") {
                    turn shouldEqual Turn.SECOND
                }
                it("7を捕獲する") {
                    board shouldEqual Board(listOf(0, 6, 6, 6, 5, 0, 7, 0, 0, 6, 5, 5, 0, 2))
                }
            }
        }
    }
    describe("GameStateの終了チェック") {
        on("先手が配布して空になって終了") {
            val endState = GameState(Board(listOf(0, 0, 0, 0, 0, 2, 23, 1, 2, 1, 2, 1, 2, 14)), Turn.FIRST)
                    .moveStone(Position(5))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
        on("後手が配布して空になって終了") {
            val endState = GameState(Board(listOf(2, 1, 2, 1, 2, 1, 14, 0, 0, 0, 0, 0, 2, 23)), Turn.SECOND)
                    .moveStone(Position(12))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
        on("先手がカラーに一つ入れて終了") {
            val endState = GameState(Board(listOf(0, 0, 0, 0, 0, 1, 23, 1, 2, 1, 2, 1, 2, 15)), Turn.FIRST)
                    .moveStone(Position(5))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
        on("後手がカラーに一つ入れて終了") {
            val endState = GameState(Board(listOf(2, 1, 2, 1, 2, 1, 15, 0, 0, 0, 0, 0, 1, 23)), Turn.SECOND)
                    .moveStone(Position(12))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
        on("先手が捕獲して全部空で終了") {
            val endState = GameState(Board(listOf(0, 0, 0, 0, 1, 0, 23, 1, 0, 0, 0, 0, 0, 23)), Turn.FIRST)
                    .moveStone(Position(4))
            it("25-23になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 23))
            }
        }
        on("後手が捕獲して全部空で終了") {
            val endState = GameState(Board(listOf(0, 0, 0, 0, 1, 0, 23, 1, 0, 0, 0, 0, 0, 23)), Turn.SECOND)
                    .moveStone(Position(7))
            it("23-25になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 25))
            }
        }
        on("先手が捕獲して終了") {
            val endState = GameState(Board(listOf(0, 0, 0, 0, 1, 0, 22, 1, 0, 0, 0, 0, 1, 23)), Turn.FIRST)
                    .moveStone(Position(4))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
        on("後手が捕獲して終了") {
            val endState = GameState(Board(listOf(1, 0, 0, 0, 1, 0, 23, 1, 0, 0, 0, 0, 0, 22)), Turn.SECOND)
                    .moveStone(Position(7))
            it("24-24になる") {
                endState.board shouldEqual Board(listOf(0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 24))
            }
        }
    }
})
package kalah

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object PositionSpek : Spek({
    describe("Position") {
        on("同じポジション") {
            val p0 = Position(0)
            val p1 = Position(0)
            it("equalになる") {
                p0 shouldEqual p1
            }
            it("should be kalah") {
                Position(6) shouldEqual Turn.FIRST.kalah()
                Position(13) shouldEqual Turn.SECOND.kalah()
                Position(5) shouldNotEqual Turn.FIRST.kalah()
                Position(13) shouldNotEqual Turn.FIRST.kalah()
                Position(6) shouldNotEqual Turn.SECOND.kalah()
            }
        }
        on("next") {
            it("normal") {
                Position(1).next(Turn.FIRST) shouldEqual Position(2)
                Position(1).next(Turn.SECOND) shouldEqual Position(2)
            }
            it("kalah") {
                Position(5).next(Turn.FIRST) shouldEqual Turn.FIRST.kalah()
                Position(5).next(Turn.SECOND) shouldEqual Position(7)
                Position(12).next(Turn.SECOND) shouldEqual Turn.SECOND.kalah()
                Position(12).next(Turn.FIRST) shouldEqual Position(0)
            }
        }
        on("opposite") {
            it("should oppisite "){
                Position(0).opposite() shouldEqual Position(12)
                Position(1).opposite() shouldEqual Position(11)
                Position(2).opposite() shouldEqual Position(10)
                Position(3).opposite() shouldEqual Position(9)
                Position(4).opposite() shouldEqual Position(8)
            }
            it("should throw exception at kalah") {
                { Position(6).opposite() } shouldThrow IllegalArgumentException::class
            }
        }

    }
})
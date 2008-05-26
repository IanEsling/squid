class TurnTests extends GroovyTestCase {

    void testTurn() {
        def turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 2
        turn.row = 1
        turn.column = 2
        turn.save(flush: true)

        assertEquals(turn.get(1).player, "A")
        assertEquals(turn.get(1).turnNumber, 2)
        assertEquals(turn.get(1).row, 1)
        assertEquals(turn.get(1).column, 2)
    }
}

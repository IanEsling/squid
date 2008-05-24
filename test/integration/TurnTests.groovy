class TurnTests extends GroovyTestCase {

    void testTurn() {
        def turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 2
        turn.moveTo = "XX"
        turn.save(flush:true)

        assertEquals(turn.get(1).player, "A")
        assertEquals(turn.get(1).turnNumber, 2)
        assertEquals(turn.get(1).moveTo, "XX")
    }
}

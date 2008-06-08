class TurnTests extends GroovyTestCase {

    void testTurn() {
        def turn = new Turn(player:'A', turnNumber:2, row:1, column:2, turnType:TurnType.Move.toString())
//        turn.player = "A"
//        turn.turnNumber = 2
//        turn.row = 1
//        turn.column = 2
//        turn.turnType = TurnType.Move.toString()
        if (turn.save(flush: true))
        {
        assertEquals(Turn.get(1).player, "A")
        assertEquals(Turn.get(1).turnNumber, 2)
        assertEquals(Turn.get(1).row, 1)
        assertEquals(Turn.get(1).column, 2)
        assertEquals(Turn.get(1).turnType, "Move")
        }
        else
        {
            assertTrue("failed to save turn", false)
        }
    }

    void testBogusTurn() {
        def turn = new Turn()
        turn.turnType = "Bobbins"

        if (turn.save(flush: true)) assertTrue(false)

    }
}

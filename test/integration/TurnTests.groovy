class TurnTests extends GroovyTestCase
{

    void testTurn()
    {
        def turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 2
        turn.row = 1
        turn.column = 2
        turn.turnType = Turn.MOVE
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
        turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 3
        turn.row = 3
        turn.column = 3
        turn.turnType = Turn.FIRE
        if (turn.save(flush: true))
        {
            assertEquals(Turn.get(2).player, "A")
            assertEquals(Turn.get(2).turnNumber, 3)
            assertEquals(Turn.get(2).row, 3)
            assertEquals(Turn.get(2).column, 3)
            assertEquals(Turn.get(2).turnType, "Fire")
        }
        else
        {
            assertTrue("failed to save turn", false)
        }
    }

    void testBogusTurn()
    {
        def turn = new Turn()
        turn.turnType = "Bobbins"

        if (turn.save(flush: true)) assertTrue(false)

    }
}

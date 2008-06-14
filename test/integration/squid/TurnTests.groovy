package squid

import squid.Turn

class TurnTests extends GroovyTestCase
{

    void testTurn()
    {
        Turn.list()*.delete()
        def turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 2
        turn.row = 1
        turn.column = 2
        turn.turnType = Turn.MOVE
        if (turn.save(flush: true))
        {
            assertEquals(Turn.findByPlayer('A').player, "A")
            assertEquals(Turn.findByPlayer('A').turnNumber, 2)
            assertEquals(Turn.findByPlayer('A').row, 1)
            assertEquals(Turn.findByPlayer('A').column, 2)
            assertEquals(Turn.findByPlayer('A').turnType, "Move")
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
            assertEquals(Turn.findByPlayerAndTurnNumber('A',3).player, "A")
            assertEquals(Turn.findByPlayerAndTurnNumber('A',3).turnNumber, 3)
            assertEquals(Turn.findByPlayerAndTurnNumber('A',3).row, 3)
            assertEquals(Turn.findByPlayerAndTurnNumber('A',3).column, 3)
            assertEquals(Turn.findByPlayerAndTurnNumber('A',3).turnType, "Fire")
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
        assertFalse("bogus turn validates", turn.validate())
        assertEquals("wrong number of errors", turn.errors.getErrorCount(), 5)
        assertTrue('no player errors', turn.errors.hasFieldErrors('player'))
        assertTrue('no turnNumber errors', turn.errors.hasFieldErrors('turnNumber'))
        assertTrue('no row errors', turn.errors.hasFieldErrors('row'))
        assertTrue('no column errors', turn.errors.hasFieldErrors('column'))
        assertTrue('no turnType errors', turn.errors.hasFieldErrors('turnType'))

    }
}

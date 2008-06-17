package squid

class GameTests extends GroovyTestCase
{
    void testGameOrdering()
    {
        new Game(1, 10, 'A', 'B').save(flush: true)
        new Game(4, 10, 'A', 'B').save(flush: true)
        new Game(2, 10, 'B', 'A').save(flush: true)
        new Game(3, 10, 'C', 'D').save(flush: true)

        assertEquals("games not created", Game.list().size(), 4)
        assertEquals("min game id incorrect", Game.list().min().id, Game.findByRows(1).id)
        assertEquals("max game id incorrect", Game.list().max().id, Game.findByRows(3).id)
        assertEquals("players not created for new game", Game.list().min().players.size(), 2)
    }

    void testWrongPlayerName()
    {
        Game.list()*.delete()
        new Game(10, 10, 'A', 'B').save(flush: true)
        def game = Game.list().max()
        try
        {
            game.newTurn(new Turn(9, 9, Turn.MOVE), 'C')
            fail("player not found exception should have been thrown")
        }
        catch (Exception e)
        {
            assertTrue("Exception thrown ${e}", e instanceof PlayerNotFoundException)
        }
    }
}

package squid

class PlayerTests extends GroovyTestCase
{

    void testPlayerAdded()
    {
        def player = new Player('A')
        player.save(flush: true)
        assertEquals(Player.list().size(), 1)
        assertEquals(Player.findByName('A').name, 'A')
    }

    void testTurnsForPlayer()
    {
        def player = new Player('A')
        player.save(flush:true)
        player.newTurn(new Turn(10, 10, Turn.MOVE))
        player.save(flush:true)
        assertEquals("player doesn't have turn", player.turns.size(), 1)
        assertEquals("first turn has wrong turn number", player.turns.max().turnNumber, 1)
        player.newTurn(new Turn(8, 9, Turn.MOVE))
        player.save(flush:true)
        assertEquals("player doesn't have second turn", player.turns.size(), 2)
        assertEquals("second turn has wrong turn number", player.turns.max().turnNumber, 2)
    }
}

package squid

class GameStateTest extends BaseSquidTestCase
{
    void testPlayerOrdering()
    {
        game.players = new ArrayList<Player>()
        game.players.add(new Player(name: 'PlayerA'))
        game.players.add(new Player(name: 'PlayerB'))
        assertNotNull("game state not returned", game.currentGameState())
        assertEquals("players not present", game.currentGameState().players.size(), 2)
        checkPlayerOrder()
        game.players = new ArrayList<Player>()
        game.players.add(new Player(name: 'PlayerB'))
        game.players.add(new Player(name: 'PlayerA'))
        checkPlayerOrder()
    }

    void testPlayerPosition()
    {
        game.players = new ArrayList<Player>()
        Player playerA = new Player(name:'PlayerA')
        Player playerB = new Player(name:'PlayerB')
        def mcPlayerA = new ExpandoMetaClass(Player)
        mcPlayerA.row = {->5}
        mcPlayerA.column = {->5}
        mcPlayerA.initialize()
        playerA.setMetaClass(mcPlayerA)
        game.players.add(playerA)
        game.players.add(playerB)
        game.setColumnsPlayerCanMove(2)
        game.setRowsPlayerCanMove(3)
        assertTrue("player A should be able to move to 3, 2", game.currentGameState().playerCanMoveHere(2, 3, 'PlayerA'))
        assertTrue("player A should be able to move to 8, 7", game.currentGameState().playerCanMoveHere(8, 7, 'PlayerA'))
        assertFalse("player A should not be able to move to 2, 2", game.currentGameState().playerCanMoveHere(2, 2, 'PlayerA'))
        assertFalse("player A should not be able to move to 8, 8", game.currentGameState().playerCanMoveHere(8, 8, 'PlayerA'))
    }

    private void checkPlayerOrder()
    {
        game.currentGameState().players.eachWithIndex {player, i ->
            if (player.name == 'PlayerA') assertEquals("player A not first in list", i, 0)
            if (player.name == 'PlayerB') assertEquals("player B not last in list", i, 1)
        }
    }
}
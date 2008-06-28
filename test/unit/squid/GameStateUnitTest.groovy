package squid

import squid.test.BaseSquidTestCase

class GameStateUnitTest extends BaseSquidTestCase
{
    void testDynamicGetPlayerProperties()
    {
        assertEquals(game.currentGameState().playerStatus('PlayerA'), PlayerState.READY)
        assertEquals(game.currentGameState().playerShotLanded('PlayerA'), false)
    }

    void testPlayerOrdering()
    {
        assertNotNull("game state not returned", game.currentGameState())
        assertEquals("players not present", game.currentGameState().playerStates.size(), 2)
        checkPlayerOrder()
        game = new Game(10, 10, 'PlayerB', 'PlayerA')
        setGameStateService(game)
        checkPlayerOrder()
    }

    void testValidMovesForPlayers()
    {
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'PlayerA')
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'PlayerB')
        game.setColumnsPlayerCanMove(2)
        game.setRowsPlayerCanMove(3)
        assertTrue("player A should be able to move to 3, 2", game.currentGameState().canPlayerMoveHere(2, 3, 'PlayerA'))
        assertTrue("player A should be able to move to 8, 7", game.currentGameState().canPlayerMoveHere(8, 7, 'PlayerA'))
        assertFalse("player A should not be able to move to 2, 2", game.currentGameState().canPlayerMoveHere(2, 2, 'PlayerA'))
        assertFalse("player A should not be able to move to 8, 8", game.currentGameState().canPlayerMoveHere(8, 8, 'PlayerA'))
        assertTrue("player B should be able to move to 5, 7", game.currentGameState().canPlayerMoveHere(5, 7, 'PlayerB'))
        assertTrue("player B should be able to move to 10, 10", game.currentGameState().canPlayerMoveHere(10, 10, 'PlayerB'))
        assertFalse("player B should not be able to move to 4, 6", game.currentGameState().canPlayerMoveHere(4, 6, 'PlayerB'))
        assertFalse("player B should not be able to move to 4, 9", game.currentGameState().canPlayerMoveHere(4, 9, 'PlayerB'))
    }

    private void checkPlayerOrder()
    {
        game.currentGameState().playerStates.eachWithIndex {playerState, i ->
            if (playerState.player.name == 'PlayerA') assertEquals("player A not first in list", i, 0)
            if (playerState.player.name == 'PlayerB') assertEquals("player B not last in list", i, 1)
        }
    }
}
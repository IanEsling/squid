package squid

import squid.test.BaseSquidTestCase

/**
 */
class GameStateServiceUnitTest extends BaseSquidTestCase
{
    void testCurrentGameState()
    {
        game.gameStateService = new GameStateService()
        assertNotNull(game.currentGameState())
    }

    void testCalculatePlayerShooting()
    {
        GameStateService gss = new GameStateService()
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'PlayerA')
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'PlayerB')
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'PlayerA')
        game.newTurn(new Turn(5, 5, Turn.FIRE), 'PlayerB')
        println gss.playerHealth(game)
        assertEquals("player health wrong after shooting", gss.playerHealth(game).get('PlayerA'), 0)
    }

    void testPlayerPosition()
    {
        checkPlayerRowAndColumn(1, 1, 'PlayerA')
        checkPlayerRowAndColumn(10, 10, 'PlayerB')
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'PlayerA')
        checkPlayerRowAndColumn(1, 1, 'PlayerA')
        checkPlayerRowAndColumn(10, 10, 'PlayerB')
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'PlayerB')
        checkPlayerRowAndColumn(2, 3, 'PlayerA')
        checkPlayerRowAndColumn(8, 9, 'PlayerB')
    }

    void testPlayerStatus()
    {
        assertTrue("players not ready", game.currentGameState().playerStates.every {it.status == 'ready'})
        game.newTurn(new Turn(3, 3, Turn.MOVE), 'PlayerA')
        assertFalse("players shouldn't be all ready", game.currentGameState().playerStates.every {it.status == 'ready'})
        assertEquals("player A status should be waiting", game.currentGameState().playerStatus('PlayerA'), 'waiting')
        assertEquals("player B status should be ready", game.currentGameState().playerStatus('PlayerB'), 'ready')
    }

    private def checkPlayerRowAndColumn(row, column, playerName)
    {
        assertEquals "$playerName not in correct row", game.currentGameState().playerPosition(playerName).row, row
        assertEquals "$playerName not in correct column", game.currentGameState().playerPosition(playerName).column, column
        assertTrue "$playerName should be found in row $row and column $column", game.currentGameState().anyoneThere(row, column)
    }
}
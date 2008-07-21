package squid

import squid.test.BaseSquidTestCase

class PlayerStateUnitTest  extends BaseSquidTestCase {

    void testCalculatePlayerHealth()
    {
        assertEquals("player A starting health incorrect", game.currentGameState().playerHealth('PlayerA'), 1)
        assertEquals("player B starting health incorrect", game.currentGameState().playerHealth('PlayerB'), 1)
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'PlayerA')
        game.newTurn(new Turn(5, 5, Turn.FIRE), 'PlayerB')
        assertEquals("player A health after being shot incorrect", game.currentGameState().playerHealth('PlayerA'), 0)
        assertEquals("player B health after shooting incorrect", game.currentGameState().playerHealth('PlayerB'), 1)
    }
}
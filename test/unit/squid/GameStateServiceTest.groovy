package squid
/**
 */
class GameStateServiceTest extends GroovyTestCase {

    Game game
    def gss

    void setUp()
    {
        setGameMetaMethods()
        setPlayerMetaMethods()
        setPlayerStateMetaMethods()
        game = new Game(10, 10, 'PlayerA', 'PlayerB')
        setGameStateService(game)
    }

    void testPlayerPosition()
    {
        assertEquals "player A not in correct row", game.currentGameState().playerRow('PlayerA'), 1
    }

    void testPlayerStatus()
    {
        assertTrue("players not ready", game.currentGameState().playerStates.every {it.status == 'ready'})
        game.newTurn(new Turn(3, 3, Turn.MOVE), 'PlayerA')
        assertFalse("players shouldn't be all ready", game.currentGameState().playerStates.every {it.status == 'ready'})
        assertEquals("player A status should be waiting", game.currentGameState().playerState('PlayerA').status, 'waiting')
        assertEquals("player B status should be ready", game.currentGameState().playerState('PlayerB').status, 'ready')
    }

    private void setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame) }
        game.gameStateService = gss
    }

    private void setPlayerStateMetaMethods()
    {
        PlayerState.metaClass.getGameStateService = {->return new GameStateService()}
    }

    private void setPlayerMetaMethods()
    {
        Player.metaClass.addToTurns = {turn -> turns.add(turn)}
    }

    private void setGameMetaMethods()
    {
        Game.metaClass.save = {}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player-> players.add(player)}
    }
}
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
        game = new Game(10, 10, 'PlayerA', 'PlayerB')
        setGameStateService(game)
    }

    void testPlayerState()
    {
        assertTrue(game.players.size()==2)
    }

    private void setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame) }
        game.gameStateService = gss
    }

    private void setPlayerMetaMethods()
    {
        Player.metaClass.constructor = {String name, Game newGame -> return new Player(name:name)}
    }

    private void setGameMetaMethods()
    {
        Game.metaClass.save = {}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player-> players.add(player)}
    }
}

package squid.test

import squid.*

/**
 */
class BaseSquidTestCase extends GroovyTestCase
{
    List<Game> games
    Game game
    def gss

    void setUp()
    {
        games = new ArrayList<Game>()
        setGameMetaMethods()
        setPlayerMetaMethods()
        game = new Game(10, 10, 'PlayerA', 'PlayerB')
        setGameStateService(game)
    }

    void setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame, new GameStateService()) }
        game.gameStateService = gss
    }

    def setPlayerMetaMethods()
    {
        Player.metaClass.addToTurns = {turn-> turns.add(turn)}
        Player.metaClass.getGame = { return game }
    }

    private void setGameMetaMethods()
    {
        Game.metaClass.save = {games.add(delegate)}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player -> players.add(player)}
    }
}
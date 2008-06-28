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
        Player.metaClass.invokeMethod = {String name, args ->

            if (name == 'addToTurns')
            {
                delegate.turns.add(args[0])
            }
            if (name == 'getGame')
            {
                return game
            }

            if (name != 'getGame' && name != 'addToTurns')
            {
                def validMethod = Player.metaClass.getMetaMethod(name, args)
                if (validMethod != null)
                {
                    validMethod.invoke(delegate, args)
                }
                else
                {
                    return Player.metaClass.invokeMissingMethod(delegate, name, args)
                }
            }

        }
    }

    private void setGameMetaMethods()
    {
        Game.metaClass.save = {games.add(delegate)}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player -> players.add(player)}
    }
}
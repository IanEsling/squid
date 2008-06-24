package squid.test

import squid.*

/**
 */
class BaseSquidTestCase extends GroovyTestCase
{
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

    protected void tearDown()
    {
        PlayerState.metaClass.invokeMethod = {String name, args ->
            def validMethod = PlayerState.metaClass.getMetaMethod(name, args)
            if (validMethod != null)
            {
                validMethod.invoke(delegate, args)
            }
            else
            {
                return PlayerState.metaClass.invokeMissingMethod(delegate, name, args)
            }
        }

        Player.metaClass.invokeMethod = {String name, args ->
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

    void setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame) }
        game.gameStateService = gss
    }

    def setPlayerStateMetaMethods()
    {
        PlayerState.metaClass.invokeMethod = {String name, args ->
            if (name == 'getGameStateService')
            {
                return new GameStateService()
            }
            else
            {

                def validMethod = PlayerState.metaClass.getMetaMethod(name, args)
                if (validMethod != null)
                {
                    validMethod.invoke(delegate, args)
                }
                else
                {
                    return PlayerState.metaClass.invokeMissingMethod(delegate, name, args)
                }
            }
        }
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
        Game.metaClass.save = {}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player -> players.add(player)}
    }

}
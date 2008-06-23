package squid
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

    private void setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame) }
        game.gameStateService = gss
    }

    private void setPlayerStateMetaMethods()
    {
        PlayerState.metaClass.getGameStateService = {-> return new GameStateService()}
    }

    private void setPlayerMetaMethods()
    {
        Player.metaClass.addToTurns = {turn -> turns.add(turn)}
    }

    private void setGameMetaMethods()
    {
        Game.metaClass.save = {}
        Game.metaClass.getId = {1}
        Game.metaClass.addToPlayers = {player -> players.add(player)}
    }
}
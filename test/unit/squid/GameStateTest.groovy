package squid

class GameStateTest extends GroovyTestCase
{
    Game game
    def gss

    void setUp()
    {
        game = new Game(rows: 10, columns: 10)
        setGameMetaMethods(game)
        setGameStateService(game)
    }

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
        mcPlayerA.row = {->1}
        mcPlayerA.column = {->1}
        mcPlayerA.initialize()
        playerA.setMetaClass(mcPlayerA)
        game.players.add(playerA)
        game.players.add(playerB)
        game.setPlayerMoveColumns(3)
        game.setPlayerMoveRows(3)
        assertTrue("player A should be able to move here", game.currentGameState().playerCanMoveHere(3, 3, 'PlayerA'))
    }

    private def checkPlayerOrder()
    {
        return game.currentGameState().players.eachWithIndex {player, i ->
            if (player.name == 'PlayerA') assertEquals("player A not first in list", i, 0)
            if (player.name == 'PlayerB') assertEquals("player B not last in list", i, 1)
        }
    }

    private def setGameStateService(Game game)
    {
        gss = new Expando()
        gss.gameState = {testGame -> return new GameState(testGame) }
        game.gameStateService = gss
    }

    private def setGameMetaMethods(Game game)
    {
        def emc = new ExpandoMetaClass(Game)
        emc.save = {}
        emc.getId = {1}
        emc.initialize()
        game.setMetaClass(emc)
    }
}
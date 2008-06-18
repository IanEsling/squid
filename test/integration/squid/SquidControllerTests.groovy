package squid

import squid.Game
import squid.GameState
import squid.Turn
import squid.SquidController
import groovy.util.*

class SquidControllerTests extends GroovyTestCase
{
    def gameStateService
    
    void testStartNewGame()
    {
        def squid = new SquidController()
        squid.params.playerA = "A"
        squid.params.playerB = "B"
        squid.params.rows = "10"
        squid.params.columns = "10"
        squid.newGame.call()
        def game = Game.list().max()
        assertEquals("players not correct for new game", game.players.size(), 2)
        game.players.each{
            assertTrue("player name not A or B",it.name == 'A'||it.name=='B')
        }
        assertTrue("no rows for new game", game.rows == 10)
        assertTrue("no columns for new game", game.columns == 10)
        assertTrue("new game id not set up correctly", game.id > 0)
    }

    void testStartNewGameWithMissingParams()
    {
        def squid = new SquidController()
        squid.params.playerA = ""
        squid.params.playerB = ""
        squid.params.rows = ""
        squid.params.columns = ""
        squid.newGame.call()
        assertTrue("New Game not created", Game.list().size() > 0)
        def game = Game.list().max()
        assertEquals("new game player a not set up correctly", game.players.find{it.name="Player A"}.name, "Player A")
        assertEquals("new game player b not set up correctly", game.players.find{it.name="Player B"}.name, "Player B")
        assertTrue("no rows for new game", game.rows == 10)
        assertTrue("no columns for new game", game.columns == 10)
        assertTrue("new game id not set up correctly", game.id > 0)
    }

    void testPlayerStatus()
    {
        def squid = new SquidController()
        squid.newGame.call()
        newMove('2', '1', "Player A")
        assertEquals("game not waiting for Player A", getGame().player('Player A').get(GameState.PLAYER_STATUS), "waiting")
        assertEquals("game not ready for Player B", getGame().player('Player B').get(GameState.PLAYER_STATUS), "ready")
        newMove('4', '3', "Player B")
        assertEquals("game not ready for Player A", getGame().player('Player A').get(GameState.PLAYER_STATUS), "ready")
        assertEquals("game not ready for Player B", getGame().player('Player B').get(GameState.PLAYER_STATUS), "ready")
        newMove('8', '7', "Player B")
        assertEquals("game not ready for Player A", getGame().player('Player A').get(GameState.PLAYER_STATUS), "ready")
        assertEquals("game not waiting for Player B", getGame().player('Player B').get(GameState.PLAYER_STATUS), "waiting")
        newMove('8', '8', "Player A", Turn.FIRE)
        assertEquals("game not ready for Player A", getGame().player('Player A').get(GameState.PLAYER_STATUS), "ready")
        assertEquals("game not ready for Player B", getGame().player('Player B').get(GameState.PLAYER_STATUS), "ready")
    }

    void testPlayerPosition()
    {
        def squid = new SquidController()
        squid.newGame.call()
        newMove('3', '2', 'Player A')
        checkPlayer(1, 1, 'Player A')
        checkPlayer(10, 10, 'Player B')
        newMove('8', '9', 'Player B')
        checkPlayer(3, 2, 'Player A')
        checkPlayer(8, 9, 'Player B')
        newMove('7', '6', 'Player A', Turn.FIRE)
        checkPlayer(3, 2, 'Player A')
        checkPlayer(8, 9, 'Player B')
        newMove('7', '8', 'Player B')
        checkPlayer(3, 2, 'Player A')
        checkPlayer(7, 8, 'Player B')
        newMove('4', '3', 'Player A')
        checkPlayer(3, 2, 'Player A')
        checkPlayer(7, 8, 'Player B')
    }

    private void checkPlayer(Integer row, Integer col, String player)
    {
        checkPlayerRow(row, player)
        checkPlayerColumn(col, player)
    }

    private void checkPlayerRow(Integer row, String player)
    {
        assertEquals("player ${player} not in row ${row}", row.toString(), getGame().player(player).get(GameState.PLAYER_ROW))
    }

    private void checkPlayerColumn(Integer col, String player)
    {
        assertEquals("player ${player} not in column ${col}", col.toString(), getGame().player(player).get(GameState.PLAYER_COLUMN))
    }

    private GameState getGame()
    {
        def game = Game.list().max()
        game.gameStateService = gameStateService
        game.currentGameState()
    }

    private void newMove(String row, String column, String player, String turnType = Turn.MOVE)
    {
        def squid = new SquidController()
        squid.params.row = row
        squid.params.column = column
        squid.params.player = player
        squid.params.gameId = Game.list().max().id
        squid.params.turnType = turnType
        squid.order.call()
    }
}

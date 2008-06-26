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
        setGameService()
        setPlayerService()
        newMove('2', '1', "PlayerA")
        checkPlayerStatus('PlayerA', "waiting")
        checkPlayerStatus('PlayerB', "ready")
        newMove('4', '3', "PlayerB")
        checkPlayerStatus('PlayerA', "ready")
        checkPlayerStatus('PlayerB', "ready")
        newMove('8', '7', "PlayerB")
        checkPlayerStatus('PlayerA', "ready")
        checkPlayerStatus('PlayerB', "waiting")
        newMove('8', '8', "PlayerA", Turn.FIRE)
        checkPlayerStatus('PlayerA', "ready")
        checkPlayerStatus('PlayerB', "ready")
    }

    void testPlayerPosition()
    {
        def squid = new SquidController()
        squid.newGame.call()
        setGameService()
        setPlayerService()
        newMove('3', '2', 'PlayerA')
        checkPlayer(1, 1, 'PlayerA')
        checkPlayer(10, 10, 'PlayerB')
        newMove('8', '9', 'PlayerB')
        checkPlayer(3, 2, 'PlayerA')
        checkPlayer(8, 9, 'PlayerB')
        newMove('7', '6', 'PlayerA', Turn.FIRE)
        checkPlayer(3, 2, 'PlayerA')
        checkPlayer(8, 9, 'PlayerB')
        newMove('7', '8', 'PlayerB')
        checkPlayer(3, 2, 'PlayerA')
        checkPlayer(7, 8, 'PlayerB')
        newMove('4', '3', 'PlayerA')
        checkPlayer(3, 2, 'PlayerA')
        checkPlayer(7, 8, 'PlayerB')
    }

    private void checkPlayerStatus(String playerName, String status)
    {
        def playerState = getGame().playerStates.find {it.playerName==playerName}
        assertEquals("player status not correct for ${playerName}", playerState.status, status)

    }

    private void setPlayerService()
    {
        Player.findAll().each {
            it.gameStateService = gameStateService
        }
    }

    private void setGameService()
    {
        Game.findAll().each {
            it.gameStateService = gameStateService
        }
    }

    private void checkPlayer(Integer row, Integer col, String player)
    {
        checkPlayerRow(row, player)
        checkPlayerColumn(col, player)
    }

    private void checkPlayerRow(Integer row, String playerName)
    {
        assertEquals("player ${playerName} not in row ${row}", row, getGame().playerRow(playerName))
    }

    private void checkPlayerColumn(Integer col, String playerName)
    {
        assertEquals("player ${playerName} not in column ${col}", col, getGame().playerColumn(playerName))
    }

    private GameState getGame()
    {
        def squid = new SquidController()
        return squid.currentGame.call().get('gameState')        
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

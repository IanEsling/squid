package squid

class GameStateServiceTests extends GroovyTestCase
{

    def game
    def gameStateService

    protected void setUp()
    {
        game = newGame()
    }

    void testPlayerPositions()
    {
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        assertEquals("player A row moved before player B move received", game.currentGameState().player('A').get(GameState.PLAYER_ROW), '1')
        assertEquals("player A column moved before player B move received", game.currentGameState().player('A').get(GameState.PLAYER_COLUMN), '1')
        assertEquals("player B row moved after player A move received", game.currentGameState().player('B').get(GameState.PLAYER_ROW), '10')
        assertEquals("player B column moved after player A move received", game.currentGameState().player('B').get(GameState.PLAYER_COLUMN), '10')
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        assertEquals("player A row wrong after move", game.currentGameState().player('A').get(GameState.PLAYER_ROW), '2')
        assertEquals("player A column wrong after move", game.currentGameState().player('A').get(GameState.PLAYER_COLUMN), '3')
        assertEquals("player B row wrong after move", game.currentGameState().player('B').get(GameState.PLAYER_ROW), '8')
        assertEquals("player B column wrong after move", game.currentGameState().player('B').get(GameState.PLAYER_COLUMN), '9')
    }

    void testPlayerStatusAndTurnNumber()
    {
        game.newTurn(new Turn(2, 2, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'waiting', 'ready', 1)
        game.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 2)
        game.newTurn(new Turn(3, 3, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'waiting', 'ready', 2)
        game.newTurn(new Turn(8, 7, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 3)
        game.newTurn(new Turn(6, 5, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'waiting', 3)
        game.newTurn(new Turn(5, 4, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 4)
    }

    void testGameOverConditions()
    {
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush: true)
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'B').save(flush: true)
        def gameState = game.currentGameState()
        assertEquals("game not over when players on same cell", gameState.gameOver, true)
        assertEquals("game not a draw when players on same cell", gameState.winner.size(), 2)
        game = newGame()
        game.newTurn(new Turn(5, 5, Turn.FIRE), 'A').save(flush: true)
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'B').save(flush: true)
        gameState = game.currentGameState()
        assertEquals("game not over when player A shot B", gameState.gameOver, true)
        assertEquals("A not the only winner", gameState.winner.size(), 1)
        assertEquals("player A not the winner after shooting player B", gameState.winner.every {it.name=='A'}, true)
        game = newGame()
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush: true)
        game.newTurn(new Turn(5, 5, Turn.FIRE), 'B').save(flush: true)
        gameState = game.currentGameState()
        assertEquals("game not over when player B shot A", gameState.gameOver, true)
        assertEquals("B not the only winner", gameState.winner.size(), 1)
        assertEquals("player B not the winner after shooting player A", gameState.winner.every {it.name=='B'}, true)
    }

    void testPlayerStartPositions()
    {
        assertTrue("new game has too many turns", game.players.every{it.turns.size()==1})
        def gameState = game.currentGameState()
        assertEquals("player A not in starting row", gameState.player('A').get(GameState.PLAYER_ROW), "1")
        assertEquals("player B not in starting row", gameState.player('B').get(GameState.PLAYER_ROW), "10")
        assertEquals("player A not in starting column", gameState.player('A').get(GameState.PLAYER_COLUMN), "1")
        assertEquals("player B not in starting column", gameState.player('B').get(GameState.PLAYER_COLUMN), "10")
    }

    void testAddNewTurn()
    {
        game.newTurn(new Turn(1, 1, Turn.MOVE), 'A').save(flush: true)
        assertEquals("player A doesn't have new turn", game.players.find{it.name=='A'}.turns.size(), 2)
        def gameState = game.currentGameState()
        assertEquals("game turn number not 1", gameState.turnNumber, 1)
        game.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        assertEquals("player B doesn't have 1 turn", game.players.every{it.turns.size()==2}, true)
        gameState = game.currentGameState()
        assertEquals("game turn number not 2", gameState.turnNumber, 2)
    }

    void testShotLanded()
    {
        def gameState = game.currentGameState()
        assertEquals("shot landed before turn taken", gameState.player('A').get(GameState.SHOT_LANDED), "false")
        assertEquals("shot landed before turn taken", gameState.player('A').get(GameState.SHOT_LANDED_ROW), null)
        assertEquals("shot landed before turn taken", gameState.player('A').get(GameState.SHOT_LANDED_COLUMN), null)
        game.newTurn(new Turn(2, 3, Turn.FIRE), 'A').save(flush: true)
        gameState = game.currentGameState()
        assertEquals("shot landed before turn taken", gameState.player('A').get(GameState.SHOT_LANDED), "false")
        assertEquals("shot landed before player B moved", gameState.player('A').get(GameState.SHOT_LANDED_COLUMN), null)
        assertEquals("shot landed before player B moved", gameState.player('A').get(GameState.SHOT_LANDED_ROW), null)
        game.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        gameState = game.currentGameState()
        assertEquals("shot landed before turn taken", gameState.player('A').get(GameState.SHOT_LANDED), "true")
        assertEquals("shot not landed in correct row", gameState.player('A').get(GameState.SHOT_LANDED_ROW), "2")
        assertEquals("shot not landed in correct column", gameState.player('A').get(GameState.SHOT_LANDED_COLUMN), "3")
        assertEquals("shot landed before turn taken", gameState.player('B').get(GameState.SHOT_LANDED), "false")
        assertEquals("shot landed in row for Player B", gameState.player('B').get(GameState.SHOT_LANDED_ROW), null)
        assertEquals("shot landed in column for Player B", gameState.player('B').get(GameState.SHOT_LANDED_COLUMN), null)
    }

    void testDrawIfPlayersShootEachOther()
    {
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        game.newTurn(new Turn(2, 3, Turn.FIRE), 'B').save(flush: true)
        game.newTurn(new Turn(8, 9, Turn.FIRE), 'A').save(flush: true)
        def gameState = game.currentGameState()
        assertEquals("game not a draw", gameState.winner.size(), 2)
        assertEquals("game not a draw", gameState.gameOver, true)
    }

    void testPlayerHere()
    {
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        def gameState = game.currentGameState()
        assertEquals("player not found in position 1, 1", gameState.aPlayerHere("1", "1"), true)
        assertEquals("wrong player found at 1, 1", gameState.playerHere("1", "1"), 1)
        assertEquals("player found in wrong position 2, 3", gameState.aPlayerHere("2", "3"), false)
        assertEquals("player not found in position, 10, 10", gameState.aPlayerHere("10", "10"), true)
        assertEquals("wrong player found at 10, 10", gameState.playerHere("10", "10"), 0)
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush:true)
        gameState = game.currentGameState()
        assertEquals("player not found in position 2, 3", gameState.aPlayerHere("2", "3"), true)
        assertEquals("wrong player found at 2, 3", gameState.playerHere("2", "3"), 1)
        assertEquals("player found in wrong position 1, 1", gameState.aPlayerHere("1", "1"), false)
        assertEquals("player found in wrong position 10, 10", gameState.aPlayerHere("10", "10"), false)
        assertEquals("player not found in position 8, 9", gameState.aPlayerHere("8", "9"), true)
        assertEquals("wrong player found at 10, 10", gameState.playerHere("8", "9"), 0)
    }

    void testShootingHere()
    {
        game.newTurn(new Turn(2, 3, Turn.FIRE), 'A').save(flush:true)
        def gameState = game.currentGameState()
        assertFalse("shot landed before B has moved", gameState.aShotHere("2", "3"))
        game.newTurn(new Turn(7, 8, Turn.MOVE), 'B').save(flush:true)
        gameState = game.currentGameState()
        assertTrue("shot not landed after B has moved", gameState.aShotHere("2", "3"))
        assertEquals("wrong player has fired", gameState.playerShotHere("2", "3"), 1)
    }

    void testPlayerCanMoveHere()
    {
        game.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush:true)
        game.newTurn(new Turn(6, 9, Turn.MOVE), 'B').save(flush:true)
        def gameState = game.currentGameState()
        def rows = 5-Game.ROWS_PLAYER_CAN_MOVE..5+Game.ROWS_PLAYER_CAN_MOVE
        def columns = 5-Game.COLUMNS_PLAYER_CAN_MOVE..5+Game.COLUMNS_PLAYER_CAN_MOVE
        rows.each {row->
            columns.each {column->
                assertTrue("player A should be able to move here: ${row}, ${column}", gameState.playerCanMoveHere(row, column, 'A'))
            }
        }
        rows = 1..4-Game.ROWS_PLAYER_CAN_MOVE
        rows.each {row->
            columns.each {column->
                assertFalse("player A should not be able to move here: ${row}, ${column}", gameState.playerCanMoveHere(row, column, 'A'))
            }
        }
        columns = 1..4-Game.COLUMNS_PLAYER_CAN_MOVE
        rows.each {row->
                    columns.each {column->
                        assertFalse("player A should not be able to move here: ${row}, ${column}", gameState.playerCanMoveHere(row, column, 'A'))
                    }
                }
        rows = 6-Game.ROWS_PLAYER_CAN_MOVE..6+Game.ROWS_PLAYER_CAN_MOVE
        columns = 9-Game.COLUMNS_PLAYER_CAN_MOVE..9+Game.COLUMNS_PLAYER_CAN_MOVE
        rows.each {row->
            columns.each {column->
                assertTrue("player B should be able to move here: ${row}, ${column}", gameState.playerCanMoveHere(row, column, 'B'))
            }
        }
    }


    private void checkPlayerStatusesAndTurnNumber(Game game, String playerAStatus, String playerBStatus, Integer turnNumber)
    {
        def gameState = game.currentGameState()
        assertEquals("player A status incorrect on turn ${turnNumber}", gameState.player('A').get('status'), playerAStatus)
        assertEquals("player B status incorrect on turn ${turnNumber}", gameState.player('B').get('status'), playerBStatus)
        assertEquals("turn number incorrect", gameState.turnNumber, turnNumber)
    }

    private Game newGame()
    {
        def newGame = new Game(10, 10, "A", "B")
        newGame.save(flush: true)
        newGame.gameStateService = gameStateService
        return newGame
    }
}

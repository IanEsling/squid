package squid

class GameStateServiceTests extends GroovyTestCase {

    Game testGame
    GameStateService gameStateService

    protected void setUp() {
        testGame = newGame()
    }

    Player player(String playerName) {
        testGame.players.find {
            it.name == playerName
        }
    }

    Integer playerRow(String playerName)
    {
        return testGame.currentGameState().playerRow(playerName)
    }

    Integer playerColumn(String playerName)
    {
        return testGame.currentGameState().playerColumn(playerName)
    }

    void testPlayerPositions() {
        testGame.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        def gameState = testGame.currentGameState()
        println "testing anyoneThere with gameState $gameState"
        assertFalse("player A in 2, 3 before player B has moved", gameState.anyoneThere(2, 3))
        println "testing playerRow..."
        assertEquals("player A row moved before player B move received", playerRow('A'), 1)
        assertEquals("player A column moved before player B move received", playerColumn('A'), 1)
        assertEquals("player B row moved after player A move received", playerRow('B'), 10)
        assertEquals("player B column moved after player A move received", playerColumn('B'), 10)
        testGame.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertTrue("player A not in 2,3 after player B has moved", gameState.anyoneThere(2, 3))
        assertEquals("player A row wrong after move", playerRow('A'), 2)
        assertEquals("player A column wrong after move", playerColumn('A'), 3)
        assertEquals("player B row wrong after move", playerRow('B'), 8)
        assertEquals("player B column wrong after move", playerColumn('B'), 9)
    }

    void testPlayerStatusAndTurnNumber() {
        testGame.newTurn(new Turn(2, 2, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'waiting', 'ready', 1)
        testGame.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'ready', 'ready', 2)
        testGame.newTurn(new Turn(3, 3, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'waiting', 'ready', 2)
        testGame.newTurn(new Turn(8, 7, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'ready', 'ready', 3)
        testGame.newTurn(new Turn(6, 5, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'ready', 'waiting', 3)
        testGame.newTurn(new Turn(5, 4, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(testGame, 'ready', 'ready', 4)
    }

    void testGameOverConditions() {
        testGame.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush: true)
        testGame.newTurn(new Turn(5, 5, Turn.MOVE), 'B').save(flush: true)
        def gameState = testGame.currentGameState()
        assertEquals("game not over when players on same cell", gameState.gameOver, true)
        assertEquals("game not a draw when players on same cell", gameState.winner.size(), 2)
        testGame = newGame()
        testGame.newTurn(new Turn(5, 5, Turn.FIRE), 'A').save(flush: true)
        testGame.newTurn(new Turn(5, 5, Turn.MOVE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertEquals("game not over when player A shot B", gameState.gameOver, true)
        assertEquals("A not the only winner", gameState.winner.size(), 1)
        assertEquals("player A not the winner after shooting player B", gameState.winner.every {it.name == 'A'}, true)
        testGame = newGame()
        testGame.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush: true)
        testGame.newTurn(new Turn(5, 5, Turn.FIRE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertEquals("game not over when player B shot A", gameState.gameOver, true)
        assertEquals("B not the only winner", gameState.winner.size(), 1)
        assertEquals("player B not the winner after shooting player A", gameState.winner.every {it.name == 'B'}, true)
    }

    void testPlayerStartPositions() {
        assertTrue("new game has too many turns", testGame.players.every {it.turns.size() == 1})
        assertEquals("player A not in starting row", playerRow('A'), 1)
        assertEquals("player B not in starting row", playerRow('B'), 10)
        assertEquals("player A not in starting column", playerColumn('A'), 1)
        assertEquals("player B not in starting column", playerColumn('B'), 10)
    }

    void testAddNewTurn() {
        testGame.newTurn(new Turn(1, 1, Turn.MOVE), 'A').save(flush: true)
        assertEquals("player A doesn't have new turn", testGame.players.find {it.name == 'A'}.turns.size(), 2)
        def gameState = testGame.currentGameState()
        assertEquals("game turn number not 1", gameState.turnNumber, 1)
        testGame.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        assertEquals("player B doesn't have 1 turn", testGame.players.every {it.turns.size() == 2}, true)
        gameState = testGame.currentGameState()
        assertEquals("game turn number not 2", gameState.turnNumber, 2)
    }

    void testShotLanded() {
        assertEquals("shot landed before turn taken", hasShotLandedForPlayer('A'), false)
        assertEquals("shot landed before turn taken", rowShotLandedInForPlayer('A'), null)
        assertEquals("shot landed before turn taken", columnShotLandedInForPlayer('A'), null)
        testGame.newTurn(new Turn(2, 3, Turn.FIRE), 'A').save(flush: true)
        assertEquals("shot landed before turn taken", hasShotLandedForPlayer('A'), false)
        assertEquals("shot landed before player B moved", columnShotLandedInForPlayer('A'), null)
        assertEquals("shot landed before player B moved", rowShotLandedInForPlayer('A'), null)
        testGame.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        assertEquals("shot landed before turn taken", hasShotLandedForPlayer('A'), true)
        assertEquals("shot not landed in correct row", rowShotLandedInForPlayer('A'), 2)
        assertEquals("shot not landed in correct column", columnShotLandedInForPlayer('A'), 3)
        assertEquals("shot landed before turn taken", hasShotLandedForPlayer('B'), false)
        assertEquals("shot landed in row for Player B", rowShotLandedInForPlayer('B'), null)
        assertEquals("shot landed in column for Player B", columnShotLandedInForPlayer('B'), null)
    }


    void testDrawIfPlayersShootEachOther() {
        testGame.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        testGame.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        testGame.newTurn(new Turn(2, 3, Turn.FIRE), 'B').save(flush: true)
        testGame.newTurn(new Turn(8, 9, Turn.FIRE), 'A').save(flush: true)
        def gameState = testGame.currentGameState()
        assertEquals("game not a draw", gameState.winner.size(), 2)
        assertEquals("game not a draw", gameState.gameOver, true)
    }

    void testPlayerHere() {
        testGame.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        def gameState = testGame.currentGameState()
        assertEquals("player not found in position 1, 1", gameState.anyoneThere(1, 1), true)
        assertEquals("wrong player found at 1, 1", gameState.isThereAPlayerHere(1, 1), 0)
        assertEquals("player found in wrong position 2, 3", gameState.anyoneThere(2, 3), false)
        assertEquals("player not found in position, 10, 10", gameState.anyoneThere(10, 10), true)
        assertEquals("wrong player found at 10, 10", gameState.isThereAPlayerHere(10, 10), 1)
        testGame.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertEquals("player not found in position 2, 3", gameState.anyoneThere(2, 3), true)
        assertEquals("wrong player found at 2, 3", gameState.isThereAPlayerHere(2, 3), 0)
        assertEquals("player found in wrong position 1, 1", gameState.anyoneThere(1, 1), false)
        assertEquals("player found in wrong position 10, 10", gameState.anyoneThere(10, 10), false)
        assertEquals("player not found in position 8, 9", gameState.anyoneThere(8, 9), true)
        assertEquals("wrong player found at 10, 10", gameState.isThereAPlayerHere(8, 9), 1)
    }

    void testShootingHere() {
        testGame.newTurn(new Turn(2, 3, Turn.FIRE), 'A').save(flush: true)
        def gameState = testGame.currentGameState()
        assertFalse("shot landed before B has moved", gameState.aShotHere(2, 3))
        testGame.newTurn(new Turn(7, 8, Turn.MOVE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertTrue("shot not landed after B has moved", gameState.aShotHere(2, 3))
        assertEquals("wrong player has fired", gameState.whichPlayerShotHere(2, 3), 0)
        checkNoShotsLandedExceptHere(gameState, 2, 3)

        testGame.newTurn(new Turn(3, 4, Turn.MOVE), 'A').save(flush: true)
        testGame.newTurn(new Turn(3, 4, Turn.FIRE), 'B').save(flush: true)
        gameState = testGame.currentGameState()
        assertTrue("shot not landed after B has fired", gameState.aShotHere(3, 4))
        checkNoShotsLandedExceptHere(gameState, 3, 4)
        assertEquals("wrong player shot here", gameState.whichPlayerShotHere(3, 4), 1)
    }

    private def checkNoShotsLandedExceptHere(gameState, expectedRow, expectedColumn) {
        def rows = 1..10
        def columns = 1..10
        rows.each {row ->
            columns.each {col ->
                if (row != expectedRow && col != expectedColumn) {
                    assertFalse("shot not supposed to have landed here, row $row column $col", gameState.aShotHere(row, col))
                }
            }
        }
    }

    void testPlayerCanMoveHere() {
        testGame.newTurn(new Turn(5, 5, Turn.MOVE), 'A').save(flush: true)
        testGame.newTurn(new Turn(6, 9, Turn.MOVE), 'B').save(flush: true)
        def gameState = testGame.currentGameState()
        def rows = 5 - testGame.rowsPlayerCanMove..5 + testGame.rowsPlayerCanMove
        def columns = 5 - testGame.columnsPlayerCanMove..5 + testGame.columnsPlayerCanMove
        rows.each {row ->
            columns.each {column ->
                if (row!=5 && column!=5)
                    assertTrue("player A should be able to move here: ${row}, ${column}", gameState.canPlayerMoveHere(row, column, 'A'))
            }
        }
        rows = 1..4 - testGame.rowsPlayerCanMove
        rows.each {row ->
            columns.each {column ->
                assertFalse("player A should not be able to move here: ${row}, ${column}", gameState.canPlayerMoveHere(row, column, 'A'))
            }
        }
        columns = 1..4 - testGame.columnsPlayerCanMove
        rows.each {row ->
            columns.each {column ->
                assertFalse("player A should not be able to move here: ${row}, ${column}", gameState.canPlayerMoveHere(row, column, 'A'))
            }
        }
        rows = 6 - testGame.rowsPlayerCanMove..6 + testGame.rowsPlayerCanMove
        columns = 9 - testGame.columnsPlayerCanMove..9 + testGame.columnsPlayerCanMove
        rows.each {row ->
            columns.each {column ->
                if (row!=6 && column!=9)
                    assertTrue("player B should be able to move here: ${row}, ${column}", gameState.canPlayerMoveHere(row, column, 'B'))
            }
        }
    }

    private void checkPlayerStatusesAndTurnNumber(Game game, String playerAStatus, String playerBStatus, Integer turnNumber) {
        def gameState = game.currentGameState()
        assertEquals("player A status incorrect on turn ${turnNumber}", gameState.playerStatus('A'), playerAStatus)
        assertEquals("player B status incorrect on turn ${turnNumber}", gameState.playerStatus('B'), playerBStatus)
        assertEquals("turn number incorrect", gameState.turnNumber, turnNumber)
    }

    private Game newGame() {
        def newGame = new Game(10, 10, "A", "B")
        newGame.save(flush: true)
        newGame.players.each {
            it.gameStateService = gameStateService
        }
        newGame.gameStateService = gameStateService
        return newGame
    }

    private boolean hasShotLandedForPlayer(String playerName) {
        return testGame.currentGameState().playerShotLanded(playerName)
    }

    private Integer rowShotLandedInForPlayer(String playerName) {
        return testGame.currentGameState().playerShotLandedRow(playerName)
    }

    private Integer columnShotLandedInForPlayer(String playerName) {
        return testGame.currentGameState().playerShotLandedColumn(playerName)
    }

}

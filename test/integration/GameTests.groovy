class GameTests extends GroovyTestCase {

    void testGameOrdering() {
        new Game(playerA: "A", playerB: "B", rows: 1, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 4, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 2, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 3, columns: 10).save(flush: true)

        assertEquals("games not created", Game.list().size(), 4)
        assertEquals("min game id incorrect", Game.list().min().id, Game.findByRows(1).id)
        assertEquals("max game id incorrect", Game.list().max().id, Game.findByRows(3).id)
    }

    void testPlayerStartPositions() {
        def game = new Game(playerA: "A", playerB: "B", rows: 10, columns: 3)
        game.save(flush: true)
        assertTrue("new game has turns", game.turns == null)
        assertEquals("player A not in starting row", game.playerRow("A"), 1)
        assertEquals("player B not in starting row", game.playerRow("B"), 10)
        assertEquals("player A not in starting column", game.playerColumn("A"), 1)
        assertEquals("player B not in starting column", game.playerColumn("B"), 3)
    }

    void testAddNewTurn() {
        def game = newGame()
        assertTrue("new game has turns", game.turns == null)
        game.addToTurns(new Turn(player: "A", row: 1, column: 1, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game doesn't have 1 turn", game.turns.size(), 1)
        assertEquals("game turn number not 1", game.lastTurnNumberMadeByPlayer("A"), 1)
        game.addToTurns(new Turn(player: "A", row: 2, column: 1, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Fire"))
        game.save(flush: true)
        assertEquals("game doesn't have 2 turn", game.turns.size(), 2)
        assertEquals("game turn number not 2", game.lastTurnNumberMadeByPlayer("A"), 2)
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game doesn't have 3 turns", game.turns.size(), 3)
        assertEquals("game turn number not 1 for player B", game.lastTurnNumberMadeByPlayer("B"), 1)
    }

    void testGetGameTurn() {
        def game = newGame()
        assertEquals("game turn not 1", game.status().turnNumber, 1)
        game.addToTurns(new Turn(player: "A", row: 1, column: 1, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game turn not 1", game.status().turnNumber, 1)
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game turn not 2", game.status().turnNumber, 2)
    }

    void testGetPlayerPositionAfterSubmittingOrder() {
        def game = newGame()
        game.addToTurns(new Turn(player: "A", row: 2, column: 2, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("player A column not still in first turn position", game.playerColumn("A"), 1)
        assertEquals("player A row not still in first turn position", game.playerRow("A"), 1)
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("player A column not moved", game.playerColumn("A"), 2)
        assertEquals("player A row not moved", game.playerRow("A"), 2)
        assertEquals("player B column not moved", game.playerColumn("B"), 9)
        assertEquals("player B row not moved", game.playerRow("B"), 9)
    }

    void testGetPlayerPositionAfterSubmittingFireOrder() {
        def game = new Game()
        game.addToTurns(new Turn(player: "A", row: 2, column: 2, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "A", row: 3, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Fire"))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 8, column: 8, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("player A column not correct after firing", game.playerColumn("A"), 2)
        assertEquals("player A row not correct after firing", game.playerRow("A"), 2)
        assertEquals("player B column not correct after firing", game.playerColumn("B"), 8)
        assertEquals("player B row not correct after firing", game.playerRow("B"), 8)
    }

    void testPlayerCanMoveHere() {
        def game = newGame()
        assertTrue(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 1, 1))
        assertTrue(game.playerCanMoveHere("A", 1, Game.COLUMNS_PLAYER_CAN_MOVE + 1))
        assertTrue(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 1, Game.COLUMNS_PLAYER_CAN_MOVE + 1))
        assertFalse(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 2, Game.COLUMNS_PLAYER_CAN_MOVE + 2))
    }

    void testGameOverIfBothPlayersMoveOntoSameSpot() {
        def game = newGame()
        game.addToTurns(new Turn(player: "A", row: 2, column: 2, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 2, column: 2, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertTrue(game.playerColumn("A") == 2)
        assertTrue(game.playerColumn("B") == 2)
        assertTrue(game.playerRow("A") == 2)
        assertTrue(game.playerRow("B") == 2)
        assertTrue("game not over", game.status().gameOver)
        assertEquals("game progress not a draw", game.status().winner, "Draw")
    }

    void testShotLanded() {
        def game = new Game()
        assertEquals("shot landed before turn taken", game.shotLandedInRow('A', 2), false)
        assertEquals("shot landed before turn taken", game.shotLandedInColumn('A', 3), false)
        game.addToTurns(new Turn(player: "A", row: 2, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.FIRE))
        game.save(flush: true)
        assertFalse("shot landed before player B moved", game.shotLanded('A'))
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        assertTrue("shot not landed before player B moved", game.shotLanded('A'))
        assertFalse("shot landed by player B", game.shotLanded('B'))
        assertEquals("shot not landed in correct row", game.shotLandedInRow('A', 2), true)
        assertEquals("shot not landed in correct column", game.shotLandedInColumn('A', 3), true)
        assertEquals("shot landed in row for Player B", game.shotLandedInRow('B', 9), false)
        assertEquals("shot landed in column for Player B", game.shotLandedInColumn('B', 9), false)
    }

    void testPlayerAWins() {
        def game = new Game()
        game.addToTurns(new Turn(player: "A", row: 2, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 8, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 5, column: 6, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "A", row: 5, column: 6, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.FIRE))
        game.save(flush: true)
        assertEquals("player A has not won", game.playerAHasWon(), true);
    }

    void testPlayerBWins() {
        def game = new Game()
        game.addToTurns(new Turn(player: "A", row: 2, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 8, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 5, column: 6, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.FIRE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "A", row: 5, column: 6, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        assertEquals("player B has not won", game.playerBHasWon(), true);
    }

    void testDrawIfPlayersShootEachOther() {
        def game = new Game()
        game.addToTurns(new Turn(player: "A", row: 2, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 8, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.MOVE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "B", row: 2, column: 3, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: Turn.FIRE))
        game.save(flush: true)
        game.addToTurns(new Turn(player: "A", row: 8, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: Turn.FIRE))
        game.save(flush: true)
        assertEquals("game not a draw", game.status().winner, Game.DRAW)
    }

    private Game newGame() {
        new Game(playerA: "A", playerB: "B", rows: 10, columns: 10).save(flush: true)
    }
}

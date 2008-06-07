class GameTests extends GroovyTestCase {

    void testGameOrdering() {
        new Game(playerA: "A", playerB: "B", rows: 10, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 40, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 20, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 30, columns: 10).save(flush: true)

        assertEquals("games not created", Game.list().size(), 4)
        assertEquals("min game id incorrect", Game.list().min().id, Game.findByRows(10).id)
        assertEquals("max game id incorrect", Game.list().max().id, Game.findByRows(30).id)
    }

    void testPlayerStartPositions() {
        def game = new Game(playerA: "A", playerB: "B", rows: 10, columns: 30)
        game.save(flush: true)
        assertTrue("new game has turns", game.turns == null)
        assertEquals("player A not in starting row", game.playerRow("A"), 1)
        assertEquals("player B not in starting row", game.playerRow("B"), 10)
        assertEquals("player A not in starting column", game.playerColumn("A"), 1)
        assertEquals("player B not in starting column", game.playerColumn("B"), 30)
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

    void testGetGameTurn()
    {
        def game = newGame()
        assertEquals("game turn not 1",game.status().turnNumber, 1)
        game.addToTurns(new Turn(player: "A", row: 1, column: 1, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game turn not 1",game.status().turnNumber, 1)
        game.addToTurns(new Turn(player: "B", row: 9, column: 9, turnNumber: game.lastTurnNumberMadeByPlayer("B") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("game turn not 2",game.status().turnNumber, 2)
    }

    void testGetPlayerPositionAfterSubmittingOrder()
    {
        def game = newGame()
        game.addToTurns(new Turn(player: "A", row: 2, column: 2, turnNumber: game.lastTurnNumberMadeByPlayer("A") + 1, turnType: "Move"))
        game.save(flush: true)
        assertEquals("player A column not still in first turn position", game.playerColumn("A"), 1)
        assertEquals("player A row not still in first turn position", game.playerRow("A"), 1)
    }

    void testPlayerCanMoveHere()
    {
        def game = newGame()
        assertTrue(game.playerCanMoveHere("A", 1, 2))
        assertTrue(game.playerCanMoveHere("A", 2, 1))
        assertTrue(game.playerCanMoveHere("A", 2, 2))
        assertFalse(game.playerCanMoveHere("A", 2, 3))
    }

    private Game newGame() {
        new Game(playerA: "A", playerB: "B", rows: 10, columns: 10).save(flush: true)
    }
}

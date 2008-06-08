class SquidControllerTests extends GroovyTestCase {

    void testStartNewGame() {
        def squid = new SquidController()
        squid.params.playerA = "A"
        squid.params.playerB = "B"
        squid.params.rows = "10"
        squid.params.columns = "10"
        squid.newGame.call()
        def game = Game.list().max()
        assertEquals("new game player a not set up correctly", game.playerA, "A")
        assertEquals("new game player b not set up correctly", game.playerB, "B")
        assertTrue("no rows for new game", game.rows == 10)
        assertTrue("no columns for new game", game.columns == 10)
        assertTrue("new game id not set up correctly", game.id > 0)
    }

    void testStartNewGameWithMissingParams() {
        def squid = new SquidController()
        squid.params.playerA = ""
        squid.params.playerB = ""
        squid.params.rows = ""
        squid.params.columns = ""
        squid.newGame.call()
        assertTrue("New Game not created", Game.list().size() > 0)
        def game = Game.list().max()
        assertEquals("new game player a not set up correctly", game.playerA, "Player A")
        assertEquals("new game player b not set up correctly", game.playerB, "Player B")
        assertTrue("no rows for new game", game.rows == 10)
        assertTrue("no columns for new game", game.columns == 10)
        assertTrue("new game id not set up correctly", game.id > 0)
    }

    void testPlayerStatus() {
        def squid = new SquidController()
        squid.newGame.call()
        newMove(2, 1, "A")
        assertEquals("game not waiting for Player A", getGame().playerAStatus(), "waiting")
        assertEquals("game not ready for Player B", getGame().playerBStatus(), "ready")
        newMove(4, 3, "B")
        assertEquals("game not ready for Player A", getGame().playerAStatus(), "ready")
        assertEquals("game not ready for Player B", getGame().playerBStatus(), "ready")
        newMove(8, 7, "B")
        assertEquals("game not ready for Player A", getGame().playerAStatus(), "ready")
        assertEquals("game not waiting for Player B", getGame().playerBStatus(), "waiting")
        newMove(8, 8, "A", TurnType.Fire)
        assertEquals("game not ready for Player A", getGame().playerAStatus(), "ready")
        assertEquals("game not ready for Player B", getGame().playerBStatus(), "ready")
    }

    void testPlayerPosition() {
        def squid = new SquidController()
        squid.newGame.call()
        newMove(3, 2, 'A')
        checkPlayer(1, 1, 'A')
        checkPlayer(10, 10, 'B')
        newMove(8, 9, 'B')
        checkPlayer(3, 2, 'A')
        checkPlayer(8, 9, 'B')
        newMove(7, 6, 'A', TurnType.Fire)
        checkPlayer(3, 2, 'A')
        checkPlayer(8, 9, 'B')
        newMove(7, 8, 'B')
        checkPlayer(3, 2, 'A')
        checkPlayer(7, 8, 'B')
        newMove(4, 3, 'A')
        checkPlayer(3, 2, 'A')
        checkPlayer(7, 8, 'B')
    }

    private void checkPlayer(Integer row, Integer col, String player) {
        checkPlayerRow(row, player)
        checkPlayerColumn(col, player)
    }

    private void checkPlayerRow(Integer row, String player) {
        assertEquals("player ${player} not in row ${row}", row, getGame().playerRow(player))
    }

    private void checkPlayerColumn(Integer col, String player) {
        assertEquals("player ${player} not in column ${col}", col, getGame().playerColumn(player))
    }

    private Game getGame() {
        def squid = new SquidController()
        return squid.currentGame.call().get('game')
    }

    private void newMove(Integer row, Integer column, String player, TurnType turnType = TurnType.Move) {
        def squid = new SquidController()
        squid.params.row = row
        squid.params.column = column
        squid.params.player = player
        squid.params.gameId = getGame().id
        squid.params.turnType = turnType.toString()
        squid.order.call()
    }
}

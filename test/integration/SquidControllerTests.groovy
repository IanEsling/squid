class SquidControllerTests extends GroovyTestCase {

    void testStartNewGame() {
        def squid = new SquidController()
        squid.params.playerA = "A"
        squid.params.playerB = "B"
        squid.params.rows = 10
        squid.params.columns = 10
        squid.newGame.call()
        def game = Game.list().max()
        assertEquals("new game player a not set up correctly", game.playerA, "A")
        assertEquals("new game player b not set up correctly", game.playerB, "B")
        assertTrue("no rows for new game", game.rows > 0)
        assertTrue("no columns for new game", game.columns > 0)
        assertTrue("new game id not set up correctly", game.id > 0)
    }

    void testPlayerStatus() {
        def squid = new SquidController()
        squid.newGame.call()
        def game = newMove(2, 1, "A")
        assertEquals("game not waiting for Player A", game.playerAStatus(), "waiting")
        assertEquals("game not ready for Player B", game.playerBStatus(), "ready")
        newMove(4, 3, "B")
        assertEquals("game not ready for Player A", game.playerAStatus(), "ready")
        assertEquals("game not ready for Player B", game.playerBStatus(), "ready")
        newMove(8, 7, "B")
        assertEquals("game not ready for Player A", game.playerAStatus(), "ready")
        assertEquals("game not waiting for Player B", game.playerBStatus(), "waiting")
    }

    private Game newMove(Integer row, Integer column, String player) {
        def squid = new SquidController()
        def game = Game.list().max()
        squid.params.row = row
        squid.params.column = column
        squid.params.player = player
        squid.params.gameId = game.id
        squid.params.turnType = "Move"
        squid.order.call()

        return game
    }
}

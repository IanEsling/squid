class SquidControllerTests extends GroovyTestCase {

    void testStartNewGame()
    {
        def squid = new SquidController()
        squid.params.playerA = "A"
        squid.params.playerB = "B"
        squid.params.rows = 10
        squid.params.columns = 10
        squid.newGame()
        def game = Game.list().max()
        assertEquals("new game player a not set up correctly", game.playerA, "A")
        assertEquals("new game player b not set up correctly", game.playerB, "B")
        assertTrue("no rows for new game", game.rows > 0)
        assertTrue("no columns for new game", game.columns > 0)
        assertTrue("new game id not set up correctly", game.id > 0)
    }
        
    void testOrdersForPlayersRecorded() {

        def squid = new SquidController()
        squid.newGame.call()
        def game = Game.list().max()
        newMove(2, 1, "A", game, squid)
        assertEquals("Player A row, turn 1 wrong", game.playerRow("A"), 2)
        assertEquals("Player A column, turn 1 wrong", game.playerColumn("A"), 1)
        newMove(3, 2, "A", game, squid)
        assertEquals("Player A row, turn 2 wrong", game.playerRow("A"), 3)
        assertEquals("Player A column, turn 2 wrong", game.playerColumn("A"), 2)
        newMove(9, 8, "B", game, squid)
        assertEquals("Player B row, turn 1 wrong", game.playerRow("B"), 9)
        assertEquals("Player B column, turn 1 wrong", game.playerColumn("B"), 8)
    }

    private void newMove(Integer row, Integer column, String player, Game game, SquidController squid)
    {
        squid.params.row = row
        squid.params.column = column
        squid.params.player = player
        squid.params.gameId = game.id
        squid.order()

        assertEquals("Player "+ player + " row wrong", game.playerRow(player), row)
        assertEquals("Player "+ player + " column wrong", game.playerColumn(player), column)
    }

    private OrderForm newOrder(row, column, player, gameId)
    {
        def order = new OrderForm()
        order.row = row
        order.column = column
        order.player = player
        order.gameId = gameId
        return order
    }

    void testOrderResolution() {

        def squid = new SquidController()
        squid.newGame.call()
        def game = Game.list().max()
        newMove(2, 1, "A", game, squid)
        assertEquals("orders resolved after only player A moved", "waiting", squid.orderStatus(game))
        newMove(3, 6, "B", game, squid)
        assertEquals("orders not resolved after player B moved", "resolved", squid.orderStatus(game))
        newMove(1, 3, "B", game, squid)
        assertEquals("orders resolved after player B moved turn 2", "waiting", squid.orderStatus(game))
        newMove(4, 5, "A", game, squid)
        assertEquals("orders not resolved after player A moved turn 2", "resolved", squid.orderStatus(game))
    }
}

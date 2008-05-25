class SquidControllerTests extends GroovyTestCase {

    void testStartNewGame()
    {
        def squid = new SquidController()
        squid.newGame.call()
        def model = squid.modelAndView.model.game
        assertEquals("new game player a not set up correctly", model.playerA, "A")
        assertEquals("new game player b not set up correctly", model.playerB, "B")
        assertTrue("no rows for new game", model.rows > 0)
        assertTrue("no columns for new game", model.columns > 0)
        assertEquals("new game id not set up correctly", model.id, 1)
    }
        
    void testOrdersForPlayersRecorded() {

        def squid = new SquidController()
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 1
        squid.params.player = "A"
        squid.order.call()
        assertEquals("Player A position, turn 1 wrong", squid.playerAPosition(), "A1")
        squid.params.moveTo = "B2"
        squid.params.turnNumber = 2
        squid.params.player = "A"
        squid.order.call()
        assertEquals("Player A position, turn 2 wrong", squid.playerAPosition(), "B2")
        squid.params.moveTo = "Z99"
        squid.params.turnNumber = 1
        squid.params.player = "B"
        squid.order.call()
        assertEquals("Player A position wrong after Player B turn", squid.playerAPosition(), "B2")
        assertEquals("PLayer B position wrong", squid.playerBPosition(), "Z99")
    }

    void testOrderResolution() {

        def squid = new SquidController()
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 1
        squid.params.player = "A"
        squid.order.call()
        assertEquals("orders resolved after only player A moved", "waiting", squid.orderStatus)
        squid.params.moveTo = "Z1"
        squid.params.turnNumber = 1
        squid.params.player = "B"
        squid.order.call()
        assertEquals("orders not resolved after player B moved", "resolved", squid.orderStatus)
        squid.params.moveTo = "Z2"
        squid.params.turnNumber = 2
        squid.params.player = "B"
        squid.order.call()
        assertEquals("orders resolved after player B moved turn 2", "waiting", squid.orderStatus)
        squid.params.moveTo = "A2"
        squid.params.turnNumber = 2
        squid.params.player = "A"
        squid.order.call()
        assertEquals("orders not resolved after player A moved turn 2", "resolved", squid.orderStatus)
    }
}

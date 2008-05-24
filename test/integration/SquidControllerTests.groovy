class SquidControllerTests extends GroovyTestCase {

    void testLastTurnForPlayer() {
        def squid = new SquidController()
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 1
        squid.params.player = "A"
        squid.order.call()
        assertEquals("last turn for player A not 1", 1, squid.lastTurnForPlayer("A"))
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 2
        squid.params.player = "A"
        squid.order.call()
        assertEquals("last turn for player A not 2", 2, squid.lastTurnForPlayer("A"))
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 299
        squid.params.player = "A"
        squid.order.call()
        squid.params.moveTo = "Z1"
        squid.params.turnNumber = 399
        squid.params.player = "B"
        squid.order.call()
        assertEquals("last turn for player A not 299", 299, squid.lastTurnForPlayer("A"))
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
}

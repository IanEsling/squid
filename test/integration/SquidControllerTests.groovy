class SquidControllerTests extends GroovyTestCase {

    void testPlayerOrdersRecorded() {

        def squid = new SquidController()
        squid.params.moveTo = "A1"
        squid.params.turnNumber = 1
        squid.order.call()
        assertEquals(squid.currentPosition, "A1")
        assertEquals(squid.currentTurn, 1)
        squid.params.moveTo = "B2"
        squid.params.turnNumber = 2
        squid.order.call()
        assertEquals(squid.currentPosition, "B2")
        assertEquals(squid.currentTurn, 2)
    }
}

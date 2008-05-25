class TurnTests extends GroovyTestCase {

    void testTurn() {
        def turn = new Turn()
        turn.player = "A"
        turn.turnNumber = 2
        turn.moveTo = "XX"
        turn.save(flush: true)

        assertEquals(turn.get(1).player, "A")
        assertEquals(turn.get(1).turnNumber, 2)
        assertEquals(turn.get(1).moveTo, "XX")
    }

    void testTurnComparison() {
        new Turn(player:"A", turnNumber:1, moveTo:"AA").save(flush:true)
        new Turn(player:"A", turnNumber:2, moveTo:"BB").save(flush:true)
        new Turn(player:"A", turnNumber:3, moveTo:"AA").save(flush:true)
        new Turn(player:"B", turnNumber:1, moveTo:"AA").save(flush:true)
        assertEquals("not all turns saved", Turn.list().size(), 4)
        assertEquals("player A turn 3 not returned as max", Turn.findAllByPlayer("A").max().turnNumber, 3)
        assertEquals("player A turn 1 not returned as min", Turn.findAllByPlayer("A").min().turnNumber, 1)
    }
}

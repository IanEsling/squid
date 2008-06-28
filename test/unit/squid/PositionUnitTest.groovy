package squid
/**
 */
class PositionUnitTest extends GroovyTestCase{

    void testPosition()
    {
        Position pos = new Position(2, 3)
        assertNotNull("no position created", pos)
        assertEquals("position row incorrect", pos.row, 2)
        assertEquals("position column incorrect", pos.column, 3)
        assertTrue("positions not equal", pos==Position.position(2, 3))
        assertFalse("positions equal when shouldn't be", pos==Position.position(1, 1))
    }
}
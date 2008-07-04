package squid

import squid.test.BaseSquidTestCase

/**
 */
class PositionUnitTest extends BaseSquidTestCase
{
    Position pos

    void setUp()
    {
        super.setUp()
        pos = new Position(2, 3)
    }

    void testPosition()
    {
        assertNotNull("no position created", pos)
        assertEquals("position row incorrect", pos.row, 2)
        assertEquals("position column incorrect", pos.column, 3)
    }

    void testPositionEquality()
    {
        assertTrue("positions not equal", pos == Position.position(2, 3))
        assertFalse("positions equal when shouldn't be", pos == Position.position(1, 1))
    }

    void testCanMoveHere()
    {
        Game game = new Game(10, 10, 'A', 'B')
        game.setRowsPlayerCanMove(1)
        game.setColumnsPlayerCanMove(2)
        assertTrue("player should be able to move to 1, 1", pos.canMoveHere(1, 1, game))
        assertTrue("player should be able to move to 1, 5", pos.canMoveHere(1, 5, game))
        assertTrue("player should be able to move to 3, 1", pos.canMoveHere(3, 1, game))
        assertTrue("player should be able to move to 3, 5", pos.canMoveHere(3, 5, game))
        assertFalse("player shouldn't be able to move to 4, 5", pos.canMoveHere(4, 5, game))
        assertFalse("player shouldn't be able to move to 3, 6", pos.canMoveHere(3, 6, game))
        assertFalse("player shouldn't be able to move to 10, 10", pos.canMoveHere(10, 10, game))
    }

    void testCanShootHere()
    {
        Game game = new Game(10, 10, 'A', 'B')
        game.setRowsPlayerCanMove(1)
        game.setRowsPlayerCanShoot(5)
        game.setColumnsPlayerCanMove(1)
        game.setColumnsPlayerCanShoot(4)
        assertTrue("should be able to fire at 1, 1", pos.canFireHere(1, 1, game))
        assertTrue("should be able to fire at 1, 1", pos.canFireHere(2, 3, game))//able to shoot self
        assertTrue("should be able to fire at 1, 1", pos.canFireHere(7, 7, game))
        assertTrue("should be able to fire at 1, 1", pos.canFireHere(7, 1, game))
        assertTrue("should be able to fire at 1, 1", pos.canFireHere(1, 7, game))
        assertFalse("should not be able to fire at 7, 8", pos.canFireHere(7, 8, game))
        assertFalse("should not be able to fire at 8, 7", pos.canFireHere(8, 7, game))
        assertFalse("should not be able to fire at 8, 8", pos.canFireHere(8, 8, game))
    }
}
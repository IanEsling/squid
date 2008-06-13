class GameTests extends GroovyTestCase
{
    void testGameOrdering()
    {
        new Game(playerA: "A", playerB: "B", rows: 1, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 4, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 2, columns: 10).save(flush: true)
        new Game(playerA: "A", playerB: "B", rows: 3, columns: 10).save(flush: true)

        assertEquals("games not created", Game.list().size(), 4)
        assertEquals("min game id incorrect", Game.list().min().id, Game.findByRows(1).id)
        assertEquals("max game id incorrect", Game.list().max().id, Game.findByRows(3).id)
    }

    void testPlayerCanMoveHere()
    {
        def game = newGame()
        assertTrue(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 1, 1))
        assertTrue(game.playerCanMoveHere("A", 1, Game.COLUMNS_PLAYER_CAN_MOVE + 1))
        assertTrue(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 1, Game.COLUMNS_PLAYER_CAN_MOVE + 1))
        assertFalse(game.playerCanMoveHere("A", Game.ROWS_PLAYER_CAN_MOVE + 2, Game.COLUMNS_PLAYER_CAN_MOVE + 2))
    }

    private Game newGame()
    {
        new Game(playerA: "A", playerB: "B", rows: 10, columns: 10).save(flush: true)
    }
}

package squid

class GameStateServiceTests extends GroovyTestCase
{

    def game
    def gameStateService

    protected void setUp()
    {
        game = newGame()
        game.gameStateService = gameStateService
    }



    void testPlayerPositions()
    {
        game.newTurn(new Turn(2, 3, Turn.MOVE), 'A').save(flush: true)
        assertEquals("player A row moved before player B move received", game.currentGameState().player('A').get('row'), '1')
        assertEquals("player A column moved before player B move received", game.currentGameState().player('A').get('column'), '1')
        assertEquals("player B row moved after player A move received", game.currentGameState().player('B').get('row'), '10')
        assertEquals("player B column moved after player A move received", game.currentGameState().player('B').get('column'), '10')
        game.newTurn(new Turn(8, 9, Turn.MOVE), 'B').save(flush: true)
        assertEquals("player A row wrong after move", game.currentGameState().player('A').get('row'), '2')
        assertEquals("player A column wrong after move", game.currentGameState().player('A').get('column'), '3')
        assertEquals("player B row wrong after move", game.currentGameState().player('B').get('row'), '8')
        assertEquals("player B column wrong after move", game.currentGameState().player('B').get('column'), '9')
    }

    void testPlayerStatusAndTurnNumber()
    {
        game.newTurn(new Turn(2, 2, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'waiting', 'ready', 1)
        game.newTurn(new Turn(9, 9, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 2)
        game.newTurn(new Turn(3, 3, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'waiting', 'ready', 2)
        game.newTurn(new Turn(8, 7, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 3)
        game.newTurn(new Turn(6, 5, Turn.MOVE), 'B').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'waiting', 3)
        game.newTurn(new Turn(5, 4, Turn.MOVE), 'A').save(flush: true)
        checkPlayerStatusesAndTurnNumber(game, 'ready', 'ready', 4)
    }

    void testGameOverConditions()
    {
        def game = newGame()
        game.newTurn(new Turn("A", 5, 5, Turn.MOVE, game)).save(flush: true)
        game.newTurn(new Turn('B', 5, 5, Turn.MOVE, game)).save(flush: true)
        def gameState = game.currentGameState()
        assertEquals("game not over when players on same cell", gameState.gameOver, true)
        assertEquals("game not a draw when players on same cell", gameState.winner, GameState.DRAW)
        game = newGame()
        game.newTurn(new Turn('A', 5, 5, Turn.FIRE, game)).save(flush: true)
        game.newTurn(new Turn('B', 5, 5, Turn.MOVE, game)).save(flush: true)
        gameState = game.currentGameState()
        assertEquals("game not over when player A shot B", gameState.gameOver, true)
        assertEquals("player A not the winner after shooting player B", gameState.winner, GameState.PLAYER_A)
        game = newGame()
        game.newTurn(new Turn('A', 5, 5, Turn.MOVE, game)).save(flush: true)
        game.newTurn(new Turn('B', 5, 5, Turn.FIRE, game)).save(flush: true)
        gameState = game.currentGameState()
        assertEquals("game not over when player B shot A", gameState.gameOver, true)
        assertEquals("player B not the winner after shooting player A", gameState.winner, GameState.PLAYER_B)
    }

    void testPlayerStartPositions()
    {
        def game = new Game(playerA: "A", playerB: "B", rows: 10, columns: 3)
        game.save(flush: true)
        assertTrue("new game has turns", game.turns == null)
        def gameState = game.currentGameState()
        assertEquals("player A not in starting row", gameState.playerARow, 1)
        assertEquals("player B not in starting row", gameState.playerBRow, 10)
        assertEquals("player A not in starting column", gameState.playerAColumn, 1)
        assertEquals("player B not in starting column", gameState.playerBColumn, 3)
    }

    void testAddNewTurn()
    {
        def game = newGame()
        assertTrue("new game has turns", game.turns == null)
        game.newTurn(new Turn("A", 1, 1, Turn.MOVE, game)).save(flush: true)
        assertEquals("game doesn't have 1 turn", game.turns.size(), 1)
        def gameState = game.currentGameState()
        assertEquals("game turn number not 1", gameState.turnNumber, 1)
        game.newTurn(new Turn("B", 9, 9, Turn.MOVE, game)).save(flush: true)
        assertEquals("game doesn't have 3 turns", game.turns.size(), 2)
        gameState = game.currentGameState()
        assertEquals("game turn number not 1 for player B", gameState.turnNumber, 2)
    }

    void testShotLanded()
    {
        def game = new Game()
        def gameState = game.currentGameState()
        assertEquals("shot landed before turn taken", gameState.playerAShotRow, null)
        assertEquals("shot landed before turn taken", gameState.playerAShotColumn, null)
        game.newTurn(new Turn("A", 2, 3, Turn.FIRE, game)).save(flush: true)
        gameState = game.currentGameState()
        assertEquals("shot landed before player B moved", gameState.playerAShotColumn, null)
        assertEquals("shot landed before player B moved", gameState.playerAShotRow, null)
        game.newTurn(new Turn("B", 9, 9, Turn.MOVE, game)).save(flush: true)
        gameState = game.currentGameState()
        assertEquals("shot not landed in correct row", gameState.playerAShotRow, 2)
        assertEquals("shot not landed in correct column", gameState.playerAShotColumn, 3)
        assertEquals("shot landed in row for Player B", gameState.playerBShotRow, null)
        assertEquals("shot landed in column for Player B", gameState.playerBShotColumn, null)
    }

    void testDrawIfPlayersShootEachOther()
    {
        def game = new Game()
        game.newTurn(new Turn("A", 2, 3, Turn.MOVE, game)).save(flush: true)
        game.newTurn(new Turn("B", 8, 9, Turn.MOVE, game)).save(flush: true)
        game.newTurn(new Turn("B", 2, 3, Turn.FIRE, game)).save(flush: true)
        game.newTurn(new Turn("A", 8, 9, Turn.FIRE, game)).save(flush: true)
        def gameState = game.currentGameState()
        assertEquals("game not a draw", gameState.winner, Game.DRAW)
        assertEquals("game not a draw", gameState.gameOver, true)
    }

    private void checkPlayerStatusesAndTurnNumber(Game game, String playerAStatus, String playerBStatus, Integer turnNumber)
    {
        def gameState = game.currentGameState()
        assertEquals("player A status incorrect on turn ${turnNumber}", gameState.player('A').get('status'), playerAStatus)
        assertEquals("player B status incorrect on turn ${turnNumber}", gameState.player('B').get('status'), playerBStatus)
        assertEquals("turn number incorrect", gameState.turnNumber, turnNumber)
    }

    private Game newGame()
    {
        def newGame = new Game(10, 10, "A", "B")
        newGame.save(flush: true)
        return newGame
    }
}

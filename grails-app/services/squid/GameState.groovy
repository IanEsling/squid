package squid


class GameState
{
    //needs sorting for the gui css styles
    SortedSet<PlayerState> playerStates

    Game game
    Integer turnNumber
    boolean gameOver = false
    //can have more than one winner if it's a draw
    List<Player> winner
    Integer gameId

    GameState(Game game, GameStateService gameStateService)
    {
        this.game = game
        winner = new ArrayList<Player>()
        playerStates = new TreeSet<PlayerState>()
        game.players.each {playerStates << new PlayerState(it, gameStateService)}
        gameId = game.getId()
    }

    def findPlayer = {playerName ->
        playerStates.find {it.playerName == playerName}
    }
    
    Integer playerRow(String playerName)
    {
        findPlayer(playerName).row
    }

    Integer playerColumn(String playerName)
    {
        findPlayer(playerName).column
    }

    String playerStatus(String playerName)
    {
        findPlayer(playerName).status
    }

    String declareWinner()
    {
        if (winner.size() > 1) return 'The game is a draw'
        else
            return "The winner is ${winner.get(0).name}"
    }

    boolean anyoneThere(row, column)
    {
        playerStates.any {
            it.row == row && it.column == column
        }
    }

    Integer playerHere(row, column)
    {
        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.row == row && playerState.column == column) index = i
        }
        index
    }

    boolean aShotHere(row, column)
    {
        playerStates.any {playerState ->
            playerState.shotLandedRow == row && playerState.shotLandedColumn == column
        }
    }

    Integer playerShotHere(row, column)
    {

        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.shotLandedColumn == column && playerState.shotLandedRow == row) index = i
        }
        index
    }

    boolean playerCanMoveHere(row, column, playerName)
    {
        (Math.abs(row - playerRow(playerName)) <= game.rowsPlayerCanMove) &&
        (Math.abs(column - playerColumn(playerName)) <= game.columnsPlayerCanMove) &&
        !(row == playerRow(playerName) && column == playerColumn(playerName))
    }
}

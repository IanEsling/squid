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

    GameState(Game game)
    {
        this.game = game
        winner = new ArrayList<Player>()
        playerStates = new TreeSet<PlayerState>()
        game.players.each {playerStates << new PlayerState(it)}
        gameId = game.getId()
    }

    def playerRow(String playerName)
    {
        playerStates.find {it.playerName == playerName}.row
    }

    def playerColumn(String playerName)
    {
        playerStates.find {it.playerName == playerName}.column
    }

    def playerStatus(String playerName)
    {
        playerStates.find {it.playerName == playerName}.status
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
            it.row() == row && it.column() == column
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
        players.any {player ->
            player.shotLandedRow() == row && player.shotLandedColumn() == column
        }
    }

    Integer playerShotHere(row, column)
    {

        Integer index = null
        players.eachWithIndex {player, i ->
            if (player.shotLandedColumn() == column && player.shotLandedRow() == row) index = i
        }
        index
    }

    boolean playerCanMoveHere(row, column, playerName)
    {
        def player = players.find() {it.name == playerName}
        (Math.abs(row - player.row()) <= game.rowsPlayerCanMove) &&
        (Math.abs(column - player.column()) <= game.columnsPlayerCanMove) &&
        !(row == player.row() && column == player.column())        
    }
}

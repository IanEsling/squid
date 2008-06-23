package squid

class GameState
{
    //needs sorting for the gui css styles
    SortedSet<Player> players

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
        players = new TreeSet<Player>()
        game.players.each {players << it}
        gameId = game.getId()
    }

    String declareWinner()
    {
        if (winner.size() > 1) return 'The game is a draw'
        else
            return "The winner is ${winner.get(0).name}"
    }

    boolean anyoneThere(row, column)
    {
        game.players.any {
            it.row() == row && it.column() == column
        }
    }

    Integer playerHere(row, column)
    {
        Integer index = null
        players.eachWithIndex {player, i ->
            if (player.row() == row && player.column() == column) index = i
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
        (Math.abs(row - player.row()) <= Game.ROWS_PLAYER_CAN_MOVE) &&
        (Math.abs(column - player.column()) <= Game.ROWS_PLAYER_CAN_MOVE) &&
        !(row == player.row() && column == player.column())        
    }
}

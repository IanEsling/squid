package squid

import java.util.*

class GameState {
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'
    public final static String SHOT_LANDED = 'shotLanded'
    public final static String SHOT_LANDED_ROW = 'shotLandedRow'
    public final static String SHOT_LANDED_COLUMN = 'shotLandedColumn'
    public final static String PLAYER_STATUS = 'status'
    public final static String PLAYER_ROW = 'row'
    public final static String PLAYER_COLUMN = 'column'

    SortedSet<Player> players

    Game game
    Integer turnNumber
    boolean gameOver = false
    List<Player> winner
    Integer gameId

    GameState(Game game) {
        this.game = game
        winner = new ArrayList<Player>()
        players = new TreeSet<Player>()
        game.players.each {players << it}
        gameId = game.id
    }

    String declareWinner() {
        if (winner.size() > 1) return 'The game is a draw'
        else
            return "The winner is ${winner.get(0).name}"
    }

    boolean anyoneThere(row, column) {
        game.players.any {
            it.row() == row && it.column() == column
        }
    }

    Integer playerHere(row, column) {
        Integer index = null
        players.eachWithIndex {player, i ->
            if (player.row() == row && player.column() == column) index = i
        }
        index
    }

    boolean aShotHere(row, column) {
        players.any {player ->
            player.shotLandedRow() == row && player.shotLandedColumn() == column
        }
    }

    Integer playerShotHere(row, column) {

        Integer index = null
        players.eachWithIndex {player, i ->
            if (player.shotLandedColumn() == column && player.shotLandedRow() == row) index = i
        }
        index
    }

    boolean playerCanMoveHere(row, column, playerName) {
        def player = players.find() {it.name == playerName}
        (Math.abs(row - player.row()) <= Game.ROWS_PLAYER_CAN_MOVE) && (Math.abs(column - player.column()) <= Game.ROWS_PLAYER_CAN_MOVE)
    }
}

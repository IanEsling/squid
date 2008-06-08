class Game implements Comparable {

    public final static Integer ROWS_PLAYER_CAN_MOVE = 2
    public final static Integer COLUMNS_PLAYER_CAN_MOVE = 2

    static hasMany = [turns: Turn]

    String playerA = "Player A"
    String playerB = "Player B"
    Integer rows = 10
    Integer columns = 10
    String playerAStatus
    String playerBStatus
    Integer turnNumber
    boolean gameOver = false
    String winner

    static constraints =
    {
        playerA(nullable: false, blank: false)
        playerB(nullable: false, blank: false)
        rows(nullable: false, min: 1, max: 12)
        columns(nullable: false, min: 1, max: 12)
        playerAStatus(nullable: true)
        playerBStatus(nullable: true)
        turnNumber(nullable: true)
        winner(nullable: true, inList: Winner.values().collect {it.toString()})
    }

    public Game status() {
        playerBStatus = playerBStatus()
        playerAStatus = playerAStatus()
        turnNumber = turnNumber()
        if (playersInSameCell()) {
            gameOver = true
            winner = 'Draw'
        }
        return this
    }

    private boolean playersInSameCell() {
        return playerRow('A') == playerRow('B') && playerColumn('A') == playerColumn('B')
    }

    public Integer turnNumber() {
        return lastTurnNumberMadeByPlayer("A") < lastTurnNumberMadeByPlayer("B") ?
            lastTurnNumberMadeByPlayer("A") + 1 :
            lastTurnNumberMadeByPlayer("B") + 1
    }

    private Integer defaultRow(String player) {
        return player.equals("A") ? 1 : rows
    }

    private Integer defaultColumn(String player) {
        return player.equals("A") ? 1 : columns
    }

    public Integer playerRow(String player) {
        def turn = playerStatus(player).equals("waiting") ? previousTurnByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(String player) {

        def turn = playerStatus(player).equals("waiting") ? previousTurnByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    public boolean playerCanMoveHere(String player, Integer row, Integer column) {
        return ((playerRow(player) - ROWS_PLAYER_CAN_MOVE..playerRow(player) + ROWS_PLAYER_CAN_MOVE).contains(row) &&
                (playerColumn(player) - COLUMNS_PLAYER_CAN_MOVE..playerColumn(player) + COLUMNS_PLAYER_CAN_MOVE).contains(column))
    }

    private String playerStatus(String player) {
        player.equals("A") ? playerAStatus() : playerBStatus
    }

    public String playerAStatus() {
        return lastTurnNumberMadeByPlayer("A") > lastTurnNumberMadeByPlayer("B") ? "waiting" : "ready"
    }

    public String playerBStatus() {
        return lastTurnNumberMadeByPlayer("A") < lastTurnNumberMadeByPlayer("B") ? "waiting" : "ready"
    }

    private Turn previousTurnByPlayer(String player) {
        turns?.find {it?.player?.equals(player) && it?.turnNumber == lastTurnNumberMadeByPlayer(player) - 1}
    }

    private Turn lastMoveByPlayer(String player) {
        turns?.findAll {
            (it?.player == player
                    && it.turnType == TurnType.Move.toString())
        }?.max()
    }

    public Integer lastTurnNumberMadeByPlayer(String player) {
        def turn = turns?.findAll {
            it.player == player
        }?.max()?.turnNumber
        return turn==null?0:turn
    }

    public int compareTo(Object o) {
        if (o instanceof Game) {
            def game = (Game) o
            return this.id - game.id
        }
        return 0
    }
}

enum Winner {
    Draw,
    PlayerA,
    PlayerB
}

class Game implements Comparable {

    public final static Integer ROWS_PLAYER_CAN_MOVE = 2
    public final static Integer COLUMNS_PLAYER_CAN_MOVE = 2
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'

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
        winner(nullable: true, inList: [DRAW, PLAYER_A, PLAYER_B])
    }

    public Game status() {
        playerBStatus = playerBStatus()
        playerAStatus = playerAStatus()
        turnNumber = turnNumber()
        if (playersInSameCell()) {
            gameOver = true
            winner = DRAW
        }
        if (playerAHasWon() || playerBHasWon()) {
            gameOver = true
            winner = playerAHasWon() ?
                (playerBHasWon() ? DRAW : PLAYER_A) :
                PLAYER_B
        }
        return this
    }

    boolean playerAHasWon() {
        if (shotLanded('A') && playerBStatus() == 'ready') {
            def turn = lastTurnByPlayer('A')
            return (turn.row == playerRow('B') && turn.column == playerColumn('B'))
        }
    }

    boolean playerBHasWon() {
        if (shotLanded('B') && playerAStatus() == 'ready') {
            def turn = lastTurnByPlayer('B')
            return (turn.row == playerRow('A') && turn.column == playerColumn('A'))
        }
    }

    boolean shotLanded(String player) {
        if (playerStatus(player) == 'ready') {
            return turns?.findAll {it.player == player}?.max()?.turnType == Turn.FIRE
        }
    }

    boolean shotLandedInRow(String player, Integer row) {
        if (shotLanded(player)) {
            return turns?.findAll {
                (it.player == player
                        && it.turnType == Turn.FIRE)
            }?.max()?.row == row
        }
    }

    boolean shotLandedInColumn(String player, Integer column) {
        if (shotLanded(player)) {
            return turns?.findAll {
                (it.player == player
                        && it.turnType == Turn.FIRE)
            }?.max()?.column == column
        }
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
        def turn = playerStatus(player).equals("waiting") ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(String player) {
        def turn = playerStatus(player).equals("waiting") ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    public boolean playerCanMoveHere(String player, Integer row, Integer column) {
        return ((playerRow(player) - ROWS_PLAYER_CAN_MOVE..playerRow(player) + ROWS_PLAYER_CAN_MOVE).contains(row) &&
                (playerColumn(player) - COLUMNS_PLAYER_CAN_MOVE..playerColumn(player) + COLUMNS_PLAYER_CAN_MOVE).contains(column))
    }

    private String playerStatus(String player) {
        player == 'A' ? playerAStatus() : playerBStatus()
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

    private Turn previousMoveByPlayer(String player) {
        turns?.findAll {
            (it?.player == player
                    && it?.turnType == Turn.MOVE
                    && it?.turnNumber < lastTurnNumberMadeByPlayer(player))
        }?.max()
    }

    private Turn lastTurnByPlayer(String player) {
        turns?.findAll {it?.player == player}?.max()
    }

    private Turn lastMoveByPlayer(String player) {
        turns?.findAll {
            (it?.player == player
                    && it.turnType == Turn.MOVE)
        }?.max()
    }

    public Integer lastTurnNumberMadeByPlayer(String player) {
        def turn = turns?.findAll {
            it.player == player
        }?.max()?.turnNumber
        return turn == null ? 0 : turn
    }

    public int compareTo(Object o) {
        if (o instanceof Game) {
            def game = (Game) o
            return this.id - game.id
        }
        return 0
    }
}



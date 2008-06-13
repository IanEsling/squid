class Game implements Comparable
{
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

    static constraints =
    {
        playerA(nullable: false, blank: false)
        playerB(nullable: false, blank: false)
        rows(nullable: false, min: 1, max: 12)
        columns(nullable: false, min: 1, max: 12)
    }

    Game newTurn(Turn turn)
    {
        addToTurns(turn)
        this
    }
    
    GameState currentGameState()
    {
        new GameState(this)
    }

    String playerName(String player)
    {
        player=='A'?playerA:playerB
    }

    public boolean playerCanMoveHere(String player, Integer row, Integer column)
    {
        def gameState = currentGameState()
        return (((gameState.playerRow(player, this) - ROWS_PLAYER_CAN_MOVE..gameState.playerRow(player, this) + ROWS_PLAYER_CAN_MOVE).contains(row)
                &&
                (gameState.playerColumn(player, this) - COLUMNS_PLAYER_CAN_MOVE..gameState.playerColumn(player, this)
                        + COLUMNS_PLAYER_CAN_MOVE).contains(column))
        && !(gameState.playerRow(player, this) == row && gameState.playerColumn(player, this) == column))
    }
    
    public int compareTo(Object o)
    {
        if (o instanceof Game)
        {
            return this.id - o.id
        }
        return 0
    }
}



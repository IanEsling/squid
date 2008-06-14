package squid
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

    def gameStateService

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
        gameStateService.gameState(this)
    }

    String playerName(String player)
    {
        player=='A'?playerA:playerB
    }

    public boolean playerCanMoveHere(String player, Integer row, Integer column)
    {
        def gameState = currentGameState()
        return (((gameStateService.playerRow(player, this) - ROWS_PLAYER_CAN_MOVE..gameStateService.playerRow(player, this) + ROWS_PLAYER_CAN_MOVE).contains(row)
                &&
                (gameStateService.playerColumn(player, this) - COLUMNS_PLAYER_CAN_MOVE..gameStateService.playerColumn(player, this)
                        + COLUMNS_PLAYER_CAN_MOVE).contains(column))
        && !(gameStateService.playerRow(player, this) == row && gameStateService.playerColumn(player, this) == column))
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



package squid

class Game implements Comparable
{
    def gameStateService

    public static Integer ROWS_PLAYER_CAN_MOVE = 2
    public static Integer COLUMNS_PLAYER_CAN_MOVE = 2

    static hasMany = [players: Player]

    Integer rows = 10
    Integer columns = 10
    List<Player> players

    static constraints =
    {
        rows(nullable: false, min: 1, max: 12)
        columns(nullable: false, min: 1, max: 12)
    }

    Game() {}

    Game(Integer rows, Integer columns, String ... player)
    {
        this.rows = rows
        this.columns = columns
        player.each {playerName -> addToPlayers(new Player(playerName, delegate))}
    }

    Game newTurn(Turn turn, playerName)
    {
        def player = players.find {it.name == playerName}
        if (player == null)
        {
            String errMsg = "cannot make new turn for player ${playerName}" +
                    " in game ${id} with players ${players.collect {it.name}}"
            throw new PlayerNotFoundException(errMsg)
        }
        player.newTurn(turn)
        this
    }

    GameState currentGameState()
    {        
        gameStateService.gameState(this)
    }

    void setPlayerMoveRows(Integer rows)
    {
        ROWS_PLAYER_CAN_MOVE = rows
    }

    void setPlayerMoveColumns(Integer columns)
    {
        COLUMNS_PLAYER_CAN_MOVE = columns
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



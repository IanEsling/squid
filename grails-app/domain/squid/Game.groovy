package squid

class Game implements Comparable
{
    def gameStateService
    
    public final static Integer ROWS_PLAYER_CAN_MOVE = 2
    public final static Integer COLUMNS_PLAYER_CAN_MOVE = 2
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'

    static hasMany = [players: Player]

    Integer rows = 10
    Integer columns = 10

    static constraints =
    {
        rows(nullable: false, min: 1, max: 12)
        columns(nullable: false, min: 1, max: 12)
    }

    Game(){}
    
    Game(Integer rows, Integer columns, String... player)
    {
        this.rows = rows
        this.columns = columns
        player.each {playerName -> addToPlayers(new Player(playerName, delegate))}
    }

    Game newTurn(Turn turn, playerName)
    {
        def player = players.find{it.name == playerName}
        if (player==null)
        {
            String errMsg ="cannot make new turn for player ${playerName}" +
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

    public int compareTo(Object o)
    {
        if (o instanceof Game)
        {
            return this.id - o.id
        }
        return 0
    }
}



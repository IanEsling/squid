package squid
class GameState
{
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'
    public final static String SHOT_LANDED = 'shotLanded'
    public final static String SHOT_LANDED_ROW = 'shotLandedRow'
    public final static String SHOT_LANDED_COLUMN = 'shotLandedColumn'
    public final static String PLAYER_STATUS = 'status'
    public final static String PLAYER_ROW = 'row'
    public final static String PLAYER_COLUMN = 'column'

    Map<Player, Map<String, String>> players
    Integer turnNumber
    boolean gameOver = false
    List<Player> winner
    Integer gameId

    GameState(Game game)
    {
        winner = new ArrayList<Player>()
        players = new HashMap<Player, Map<String, String>>()
        game.players.each {players.put(it, new HashMap<String, String>())}
        gameId = game.id
    }

    Map<String, String> player(String playerName)
    {
        Map returnMap = new HashMap<String, String>()
        players.each {player, values ->
            if (player.name == playerName) returnMap = values
        }
        if (returnMap.size() > 0) return returnMap
        else
            throw new PlayerNotFoundException("GameState cannot locate player name: ${playerName}")
    }

    Map<String, String> player(Player player)
    {
        if (players.containsKey(player)) return players.get(player)
        throw new PlayerNotFoundException("GameState cannot locate player ${player.name}")
    }
}

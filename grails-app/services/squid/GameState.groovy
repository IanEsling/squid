package squid

import java.util.*

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

    SortedMap<Player, Map<String, String>> players
    
    Integer turnNumber
    boolean gameOver = false
    List<Player> winner
    Integer gameId

    GameState(Game game)
    {
        winner = new ArrayList<Player>()
        players = new TreeMap<Player, Map<String, String>>()
        game.players.each {players.put(it, new HashMap<String, String>())}
        gameId = game.id
    }

    boolean aPlayerHere(row, column)
    {
        players.any {player, values ->
            Integer.valueOf(values.get(PLAYER_ROW))==Integer.valueOf(row) && Integer.valueOf(values.get(PLAYER_COLUMN))==Integer.valueOf(column)
        }
    }

    Integer playerHere(row, column)
    {
        Integer index = null
        players.eachWithIndex {player, values, i->
            if (Integer.valueOf(values.get(PLAYER_ROW))==Integer.valueOf(row) && Integer.valueOf(values.get(PLAYER_COLUMN))==Integer.valueOf(column)) index = Integer.valueOf(i)
        }
        index
    }

    boolean aShotHere(row, column)
    {
        players.any {player, values ->
            values.get(SHOT_LANDED)=='true' && Integer.valueOf(values.get(SHOT_LANDED_COLUMN))==Integer.valueOf(column) && Integer.valueOf(values.get(SHOT_LANDED_ROW))==Integer.valueOf(row)
        }
    }

    Integer playerShotHere(row, column)
    {

        Integer index = null
        players.eachWithIndex {player, values, i ->
            if (values.get(SHOT_LANDED)=='true' && Integer.valueOf(values.get(SHOT_LANDED_COLUMN))==Integer.valueOf(column) && Integer.valueOf(values.get(SHOT_LANDED_ROW))==Integer.valueOf(row)) index = i
        }
        index
    }

    boolean playerCanMoveHere(row, column, playerName)
    {
        def playerValues = new HashMap<String, String>()
        players.find{player, values ->
            if (player.name == playerName) playerValues = values
        }
        (Math.abs(Integer.valueOf(row)-Integer.valueOf(playerValues.get(PLAYER_ROW))) <= Game.ROWS_PLAYER_CAN_MOVE) && (Math.abs(Integer.valueOf(column)-Integer.valueOf(playerValues.get(PLAYER_COLUMN))) <= Game.ROWS_PLAYER_CAN_MOVE)
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

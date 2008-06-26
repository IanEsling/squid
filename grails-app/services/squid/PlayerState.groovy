package squid

/**
 */
class PlayerState implements Comparable
{
    public final static String READY = 'ready'
    public final static String WAITING = 'waiting'

    Player player
    String playerName
    String status
    Integer row, column, shotLandedRow, shotLandedColumn
    boolean shotLanded

    PlayerState(){}

    PlayerState(Player player, GameStateService gameStateService)
    {
        this.player = player
        this.playerName = player.name
        status = gameStateService.playerStatus(player)
        row = gameStateService.playerRow(player)
        column = gameStateService.playerColumn(player)
        shotLanded = gameStateService.shotLanded(player, status)
        shotLandedRow = gameStateService.shotLandedRow(player, status)
        shotLandedColumn = gameStateService.shotLandedColumn(player, status)
    }

    public int compareTo(Object o)
    {
        return player.name == o.player.name ? 0 : player.name > o.player.name ? 1 : -1
    }
}
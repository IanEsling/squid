package squid

/**
 */
class PlayerState implements Comparable
{
    public final static String READY = 'ready'
    public final static String WAITING = 'waiting'

    Player player
    String playerName, status
    Position position, shotLandedPosition
    Integer health
    boolean shotLanded

    PlayerState() {}

    PlayerState(Player player, GameStateService gameStateService)
    {
        this.player = player
        this.playerName = player.name
        status = gameStateService.playerStatus(player)
        position = gameStateService.playerPosition(player)
        shotLanded = gameStateService.shotLanded(player, status)
        shotLandedPosition = gameStateService.shotLandedPosition(player, status)
        health = 1
    }

    public int compareTo(Object o)
    {
        return player.name == o.player.name ? 0 : player.name > o.player.name ? 1 : -1
    }
}
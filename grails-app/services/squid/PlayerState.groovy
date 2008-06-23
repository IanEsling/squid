package squid
/**
 */
class PlayerState implements Comparable
{
    public final static String READY = 'ready'
    public final static String WAITING = 'waiting'

    Player player
    String status
    Integer row, column
    def gameStateService

    PlayerState(Player player)
    {
        this.player = player
        status = getGameStateService().playerStatus(player)
        row = getGameStateService().playerRow(player)
        column = getGameStateService().playerColumn(player)
    }
    
    public int compareTo(Object o)
    {
        return (o.player.compareTo(player))
    }
}
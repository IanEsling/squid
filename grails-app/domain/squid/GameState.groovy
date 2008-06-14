package squid
class GameState
{
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'

    String playerAStatus
    String playerBStatus
    Integer playerARow
    Integer playerAShotRow
    Integer playerAColumn
    Integer playerAShotColumn
    Integer playerBRow
    Integer playerBColumn
    Integer playerBShotRow
    Integer playerBShotColumn
    Integer turnNumber
    boolean gameOver = false
    String winner
    Integer gameId
}

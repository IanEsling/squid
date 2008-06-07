class Game implements Comparable {

    static hasMany = [turns: Turn]

    String playerA = "A"
    String playerB = "B"
    Integer rows = 10
    Integer columns = 10
    String playerAStatus
    String playerBStatus
    Integer turnNumber

    static constraints =
    {
        playerA(nullable: false, blank: false)
        playerB(nullable: false, blank: false)
        rows(nullable: false, minSize: 1, maxSize: 12)
        columns(nullable: false, minSize: 1, maxSize: 12)
        playerAStatus(nullable: true)
        playerBStatus(nullable: true)
        turnNumber(nullable: true)
    }

    public Game status() {
        playerBStatus = playerBStatus()
        playerAStatus = playerAStatus()
        turnNumber = turnNumber()
        return this
    }

    public Integer turnNumber() {
        return lastTurnNumberMadeByPlayer("A")<lastTurnNumberMadeByPlayer("B")?
            lastTurnNumberMadeByPlayer("A")+1:
            lastTurnNumberMadeByPlayer("B")+1
    }

    private Integer defaultRow(String player) {
        return player.equals("A") ? 1 : rows
    }

    private Integer defaultColumn(String player) {
        return player.equals("A") ? 1 : columns
    }

    public Integer playerRow(String player) {
        def turn = playerStatus(player).equals("waiting")?previousTurnByPlayer(player):lastTurnByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(String player) {

        def turn = playerStatus(player).equals("waiting")?previousTurnByPlayer(player):lastTurnByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    private String playerStatus(String player)
    {
        player.equals("A")?playerAStatus():playerBStatus
    }

    public String playerAStatus() {
        return lastTurnNumberMadeByPlayer("A") > lastTurnNumberMadeByPlayer("B") ? "waiting" : "ready"
    }

    public String playerBStatus() {
        return lastTurnNumberMadeByPlayer("A") < lastTurnNumberMadeByPlayer("B") ? "waiting" : "ready"
    }

    private Turn previousTurnByPlayer(String player)
    {
        turns?.find {it?.player?.equals(player) && it?.turnNumber == lastTurnNumberMadeByPlayer(player) - 1}
    }

    private Turn lastTurnByPlayer(String player) {
        turns?.find {it?.player?.equals(player) && it?.turnNumber == lastTurnNumberMadeByPlayer(player)}
    }

    public Integer lastTurnNumberMadeByPlayer(String player) {
        def i = 0
        turns?.each {
            if (it.player.equals(player) && it.turnNumber > i)
                i = it.turnNumber
        }
        return i
    }

    public int compareTo(Object o) {
        if (o instanceof Game) {
            def game = (Game) o
            return id - game.id
        }
        return 0
    }
}

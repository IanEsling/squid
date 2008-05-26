class Game implements Comparable {

    static hasMany = [turns: Turn]

    //default values
    String playerA = "A"
    String playerB = "B"
    Integer rows = 10
    Integer columns = 10

    static constraints = 
    {
        playerA(nullable:false, blank:false)
        playerB(nullable:false, blank:false)
        rows(nullable:false, minSize:1, maxSize:12)
        columns(nullable:false, minSize:1, maxSize:12)
    }

    private Integer defaultRow(String player) {
        return player.equals("A") ? 1 : rows
    }

    private Integer defaultColumn(String player) {
        return player.equals("A") ? 1 : columns
    }

    public Integer playerRow(String player) {
        def turn = lastTurnByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(String player) {
        def turn = lastTurnByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    private Turn lastTurnByPlayer(String player) {
        turns?.find {it?.player?.equals(player) && it?.turnNumber == lastTurnNumberMadeByPlayer(player)}
    }

    public Integer lastTurnNumberMadeByPlayer(player) {
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

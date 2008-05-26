class Game implements Comparable {
    static hasMany = [turns: Turn]

    String playerA
    String playerB
    Integer rows
    Integer columns

    public Integer playerRow(String player)
    {
        lastTurnByPlayer(player).row
    }

    public Integer playerColumn(String player)
    {
        lastTurnByPlayer(player).column
    }

    private Turn lastTurnByPlayer(String player) {
        turns.find {it.player.equals(player) && it.turnNumber == lastTurnNumberMadeByPlayer(player)}
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

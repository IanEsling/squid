class Turn implements Comparable
{
    public static final String MOVE = 'Move'
    public static final String FIRE = 'Fire'

    public int compareTo(Object o)
    {
        if (!o instanceof Turn) return 0

        def compareMe = (Turn) o
        if (compareMe.player == player)
        {
            return turnNumber - compareMe.turnNumber
        }
        return 0
    }

    String player
    Integer turnNumber
    Integer row
    Integer column
    String turnType

    static constraints = {
        player(nullable: false)
        turnNumber(nullable: false)
        row(nullable: false)
        column(nullable: false)
        turnType(nullable: false, inList: [MOVE, FIRE])
    }
}


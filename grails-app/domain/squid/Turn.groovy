package squid
class Turn implements Comparable
{
    public static final String MOVE = 'Move'
    public static final String FIRE = 'Fire'

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

    public Turn(){}

    public Turn(player, row, column, turnType, Game game)
    {
        this.player = player
        this.row = row.asType(Integer.class)
        this.column = column.asType(Integer.class)
        this.turnType = turnType
        def turnNumber = game.turns?.findAll {
            it.player == player
        }?.max()?.turnNumber
        this.turnNumber = turnNumber==null? 1 :turnNumber + 1
    }

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
}


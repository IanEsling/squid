package squid

class Turn implements Comparable
{
    public static final String MOVE = 'Move'
    public static final String FIRE = 'Fire'

    Integer turnNumber
    Integer row
    Integer column
    String turnType

    static constraints = {
        turnNumber(nullable: false)
        row(nullable: false)
        column(nullable: false)
        turnType(nullable: false, inList: [MOVE, FIRE])
    }

    public Turn(){}

    public Turn(row, column, turnType)
    {
        this.row = row.asType(Integer.class)
        this.column = column.asType(Integer.class)
        this.turnType = turnType
    }

    public int compareTo(Object o)
    {
        if (!o instanceof Turn) return 0

        return turnNumber - o.turnNumber
    }
}


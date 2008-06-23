package squid

class Player implements Comparable
{
    static hasMany = [turns: Turn]
    static belongsTo = [game: Game]

    def gameStateService

    Game game
    String name
    Integer startingRow, startingColumn
    List<Turn> turns = new ArrayList<Turn>()

    Player() {}

    Player(String name, Game game)
    {
        this.name = name
        this.game = game
        //starting positions will need work if more than 2 players
        makeFirstMove(game)
    }

    private def makeFirstMove(Game game)
    {
        startingRow = game.players ? game.rows : 1
        startingColumn = game.players ? game.columns : 1
        newTurn(new Turn(startingRow, startingColumn, Turn.MOVE))
    }

    boolean shotLanded()
    {
        gameStateService.shotLanded(this, game)
    }

    Integer shotLandedRow()
    {
        gameStateService.shotLandedRow(this, game)
    }

    Integer shotLandedColumn()
    {
        gameStateService.shotLandedColumn(this, game)
    }

    void newTurn(turn)
    {
        def turnNumber = turns?.max()?.turnNumber
        turn.turnNumber = turnNumber == null ? 0 : turnNumber + 1
        addToTurns(turn)
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Player)
        {
            return ((Player) obj).name == this.name
        }
        return obj.equals(this.name)
    }

    public int compareTo(Object o)
    {
        if (o instanceof Player)
            return (this.name == o.name ? 0 : (this.name > o.name ? 1: -1))

        return 0
    }
}

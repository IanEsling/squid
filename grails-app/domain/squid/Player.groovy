package squid

class Player implements Comparable
{
    static hasMany = [turns: Turn]
    static belongsTo = [game: Game]

    def gameStateService

    String name

    Player() {}

    Player(String name, Game game)
    {
        this.name = name
        this.game = game
        Integer startingRow = game.players == null ? 1 : game.rows
        Integer startingColumn = game.players == null ? 1 : game.columns
        newTurn(new Turn(startingRow, startingColumn, Turn.MOVE))
    }

    String status()
    {
        gameStateService.playerStatus(this, game)
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
            return this.id - o.id

        return 0
    }
}

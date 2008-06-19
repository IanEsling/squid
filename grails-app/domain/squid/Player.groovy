package squid

class Player
{
    static hasMany = [turns: Turn]

    String name

    Player() {}

    Player(String name, Game game)
    {
        this.name = name
        Integer startingRow = game.players == null ? 1 : game.rows
        Integer startingColumn = game.players == null ? 1 : game.columns
        newTurn(new Turn(startingRow, startingColumn, Turn.MOVE))
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
}

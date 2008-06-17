package squid

class Player
{
    static hasMany = [turns: Turn]

    String name
    Integer startingRow, startingColumn

    Player() {}

    Player(String name, Game game)
    {
        this.name = name
        this.startingRow = game.players == null ? 1 : game.rows
        this.startingColumn = game.players == null ? 1 : game.columns
    }

    void newTurn(turn)
    {
        def turnNumber = turns?.max()?.turnNumber
        turn.turnNumber = turnNumber == null ? 1 : turnNumber + 1
        addToTurns(turn)
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Player)
        {
            return ((Player) obj).name == this.name
        }
        return false
    }


}

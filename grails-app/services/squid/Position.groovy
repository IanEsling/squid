package squid
/**
 */
class Position {
    Integer row
    Integer column

    Position(Integer newRow, Integer newColumn)
    {
        this.row = newRow
        this.column = newColumn
    }

    static Position position(Integer newRow, Integer newColumn)
    {
        return new Position(newRow, newColumn)
    }

    boolean canFireHere(Integer rowToShootAt, Integer columnToShootAt, Game game)
    {
        (Math.abs(row - rowToShootAt) <= game.rowsPlayerCanShoot) &&
                (Math.abs(column - columnToShootAt) <= game.columnsPlayerCanShoot)
    }

    boolean canMoveHere(Integer rowToMoveTo, Integer columnToMoveTo, Game game)
    {
        (Math.abs(row - rowToMoveTo) <= game.rowsPlayerCanMove) &&
                (Math.abs(column - columnToMoveTo) <= game.columnsPlayerCanMove) &&
                !(row == rowToMoveTo && column == columnToMoveTo)
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Position)
        {
            return obj.row==row && obj.column==column
        }
        return false
    }
}
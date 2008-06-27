package squid


class GameState
{
    //needs sorting for the gui css styles
    SortedSet<PlayerState> playerStates

    Game game
    Integer turnNumber
    boolean gameOver = false
    //can have more than one winner if it's a draw
    List<Player> winner
    Integer gameId

    def invoke = {String name, args ->
        if (name.substring(0, 6) == 'player')
        {
            def property = name.substring(6, 7).toLowerCase() + name.substring(7)
            return delegate.playerStates.find {it.playerName == args[0].toString()}."${property}"
        }
        else
        {
            def validMethod = GameState.metaClass.getMetaMethod(name, args)
            if (validMethod != null)
            {
                return validMethod.invoke(delegate, args)
            }
            else
            {
                return GameState.metaClass.invokeMissingMethod(delegate, name, args)
            }
        }
    }

    GameState(Game game, GameStateService gameStateService)
    {
        this.game = game
        winner = new ArrayList<Player>()
        playerStates = new TreeSet<PlayerState>()
        game.players.each {playerStates << new PlayerState(it, gameStateService)}
        gameId = game.getId()
        GameState.metaClass.invokeMethod = invoke
    }

    String declareWinner()
    {
        if (winner.size() > 1) return 'The game is a draw'
        else
            return "The winner is ${winner.get(0).name}"
    }

    boolean anyoneThere(Integer row, Integer column)
    {
        return playerStates.any {
            System.out.println "checking playerStates position for ${it.dump()} in row $row and column $column"
            System.out.println "playerState has row $it.row and column $it.column"
            it.row == row && it.column == column
        }
    }

    Integer isThereAPlayerHere(row, column)
    {
        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.row == row && playerState.column == column) index = i
        }
        index
    }

    boolean aShotHere(row, column)
    {
        playerStates.any {playerState ->
            playerState.shotLandedRow == row && playerState.shotLandedColumn == column
        }
    }

    Integer whichPlayerShotHere(row, column)
    {

        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.shotLandedColumn == column && playerState.shotLandedRow == row) index = i
        }
        index
    }

    boolean canPlayerMoveHere(row, column, playerName)
    {
        (Math.abs(row - playerRow(playerName)) <= game.rowsPlayerCanMove) &&
                (Math.abs(column - playerColumn(playerName)) <= game.columnsPlayerCanMove) &&
                !(row == playerRow(playerName) && column == playerColumn(playerName))
    }
}

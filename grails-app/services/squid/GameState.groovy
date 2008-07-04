package squid

import static squid.Position.position

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

    GameState(Game game, GameStateService gameStateService)
    {
        this.game = game
        winner = new ArrayList<Player>()
        playerStates = new TreeSet<PlayerState>()
        game.players.each {playerStates << new PlayerState(it, gameStateService)}
        //intercept GameState methods.  Only really need to do this once on the GameState metaclass
        //rather than for each instance, but can't seem to find where/how to do that.  Would
        //have thought BootStrap.groovy was the obvious place but that doesn't seem to work.
        metaClass.invokeMethod = invoke
    }

    def invoke = {String name, args ->
        //treat all playerSomeProperty(playerName) method calls as requests
        //for someProperty on the player's PlayerState object
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

    String declareWinner()
    {
        if (winner.size() > 1) return 'The game is a draw'
        else
            return "The winner is ${winner.get(0).name}"
    }

    boolean anyoneThere(Integer row, Integer column)
    {
        return playerStates.any {
            it.position == position(row, column)
        }
    }

    Integer whichPlayerHere(row, column)
    {
        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.position == position(row, column)) index = i
        }
        index
    }

    boolean aShotHere(row, column)
    {
        playerStates.any {playerState ->
            playerState.shotLandedPosition == position(row, column)
        }
    }

    Integer whichPlayerShotHere(row, column)
    {

        Integer index = null
        playerStates.eachWithIndex {playerState, i ->
            if (playerState.shotLandedPosition == position(row, column)) index = i
        }
        index
    }

    boolean canPlayerFireHere(Integer row, Integer column, String playerName)
    {
        playerPosition(playerName).canFireHere(row, column, game)
    }

    boolean canPlayerMoveHere(Integer row, Integer column, String playerName)
    {
        playerPosition(playerName).canMoveHere(row, column, game)
    }
}

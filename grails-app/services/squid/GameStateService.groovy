package squid

class GameStateService
{
    GameState gameState(Game game)
    {
        game.save(flush: true) //seems to be needed for the gui to stay stable
        GameState gameState = new GameState(game, this)
        gameState.turnNumber = turnNumber(game)
        if (playersInSameCell(gameState))
        {
            gameState.gameOver = true
            game.players.each {gameState.winner << it}
        }
        if (aPlayerHasWon(gameState).size() > 0)
        {
            gameState.gameOver = true
            gameState.winner = aPlayerHasWon(gameState)
        }
        return gameState
    }

    List<Player> aPlayerHasWon(GameState gameState)
    {
        List<Player> winners = new ArrayList<Player>()
        if (gameState.playerStates.collect {it.status}.every {PlayerState.READY}
                && gameState.playerStates.collect {it.shotLanded}.any {true})
        {
            gameState.playerStates.each {shootingPlayerState ->
                if (shootingPlayerState.shotLanded)
                {
                    if (gameState.playerStates.any {
                        it.position == shootingPlayerState.shotLandedPosition
                    })
                    {
                        winners << shootingPlayerState.player
                    }
                }
            }
            return winners
        }
        return winners
    }

    boolean shotLanded(Player player, String status)
    {
        return ((lastTurnByPlayer(player)?.turnType == Turn.FIRE && status == PlayerState.READY)
                ||
                (previousTurnByPlayer(player)?.turnType) == Turn.FIRE && status == PlayerState.WAITING)
    }

    Position shotLandedPosition(Player player, String status)
    {
        return new Position(shotLandedRow(player, status), shotLandedColumn(player, status))
    }

    Integer shotLandedRow(Player player, String status)
    {
        if (shotLanded(player, status))
        {
            return status == PlayerState.READY ? lastTurnByPlayer(player).row : previousTurnByPlayer(player).row
        }
    }

    Integer shotLandedColumn(Player player, String status)
    {
        if (shotLanded(player, status))
        {
            return status == PlayerState.READY ? lastTurnByPlayer(player).column : previousTurnByPlayer(player).column
        }
    }

    private Turn lastTurnByPlayer(Player player)
    {
        player.turns?.max()
    }

    private Turn previousTurnByPlayer(Player player)
    {
        player.turns?.findAll {
            it?.turnNumber < lastTurnNumberMadeByPlayer(player)
        }?.max()
    }

    private Turn previousMoveByPlayer(Player player)
    {
        player.turns?.findAll {
            (it?.turnType == Turn.MOVE
                    && it?.turnNumber < lastTurnNumberMadeByPlayer(player))
        }?.max()
    }

    private Turn lastMoveByPlayer(Player player)
    {
        player.turns?.findAll {
            (it?.turnType == Turn.MOVE)
        }?.max()
    }

    private Integer defaultRow(Player player)
    {
        return player.startingRow
    }

    private Integer defaultColumn(Player player)
    {
        return player.startingColumn
    }

    Position playerPosition(Player player)
    {
        return new Position(playerRow(player), playerColumn(player))
    }
    
    public Integer playerRow(Player player)
    {
        def turn = playerStatus(player).equals(PlayerState.WAITING) ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(Player player)
    {
        def turn = playerStatus(player).equals(PlayerState.WAITING) ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    private boolean playersInSameCell(GameState gameState)
    {
        boolean sameCell = false
        gameState.playerStates.each {thisPlayer ->
            if (gameState.playerStates.any {thatPlayer ->
                thisPlayer.position == thatPlayer.position &&
                        thisPlayer.playerName != thatPlayer.playerName
            })
                sameCell = true
        }
        return sameCell
    }

    public Integer turnNumber(Game game)
    {
        def turnNumber = 0
        game.players.each {
            if (playerStatus(it) == 'ready')
                turnNumber = lastTurnNumberMadeByPlayer(it) + 1
        }
        return turnNumber
    }

    private Integer lastTurnNumberMadeByPlayer(Player player)
    {
        Integer turn = player?.turns?.max()?.turnNumber

        return turn == null ? 0 : turn
    }

    private Integer lastTurnNumberMadeByOtherPlayer(Player player)
    {
        Integer turnNumber = 0
        player.game.players.each {
            if (it.name != player.name && lastTurnNumberMadeByPlayer(it) > turnNumber)
                turnNumber = lastTurnNumberMadeByPlayer(it)
        }
        return turnNumber
    }

    String playerStatus(Player player)
    {
        Integer myLastTurnNumber = lastTurnNumberMadeByPlayer(player)
        Integer theirLastTurnNumber = lastTurnNumberMadeByOtherPlayer(player)

        return myLastTurnNumber > theirLastTurnNumber ?
            PlayerState.WAITING :
            PlayerState.READY
    }
}

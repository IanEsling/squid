package squid

class GameStateService
{

    GameState gameState(Game game)
    {
        game.save(flush: true) //seems to be needed for the gui to stay stable
        GameState gameState = new GameState(game)
        gameState.turnNumber = turnNumber(game)
        if (playersInSameCell(game))
        {
            gameState.gameOver = true
            game.players.each {gameState.winner << it}
        }
        if (aPlayerHasWon(game, gameState).size() > 0)
        {
            gameState.gameOver = true
            gameState.winner = aPlayerHasWon(game, gameState)
        }
        return gameState
    }

    List<Player> aPlayerHasWon(Game game, GameState gameState)
    {
        List<Player> winners = new ArrayList<Player>()
        if (game.players.collect {it.status()}.every {'ready'}
                && game.players.collect {it.shotLanded()}.any {true})
        {
            gameState.players.each {player ->
                if (player.shotLanded())
                {
                    def shotRow = player.shotLandedRow()
                    def shotColumn = player.shotLandedColumn()
                    if (game.players.any {
                        it.row() == shotRow && it.column() == shotColumn
                    })
                    {
                        winners << player
                    }
                }
            }
            return winners
        }
        return winners
    }

    boolean shotLanded(Player player, Game game)
    {
        return ((player.turns?.max()?.turnType == Turn.FIRE && player.status() == 'ready')
                ||
                (previousTurnByPlayer(player)?.turnType) == Turn.FIRE && player.status() == 'waiting')
    }

    Integer shotLandedRow(Player player, Game game)
    {
        if (shotLanded(player, game))
        {
            return playerStatus(player, game) == 'ready' ? lastTurnByPlayer(player).row : previousTurnByPlayer(player).row
        }
    }

    Integer shotLandedColumn(Player player, Game game)
    {
        if (shotLanded(player, game))
        {
            return playerStatus(player, game) == 'ready' ? lastTurnByPlayer(player).column : previousTurnByPlayer(player).column
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

    public Integer playerRow(Player player, Game game)
    {
        def turn = playerStatus(player, game).equals("waiting") ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultRow(player) : turn.row
    }

    public Integer playerColumn(Player player, Game game)
    {
        def turn = playerStatus(player, game).equals("waiting") ? previousMoveByPlayer(player) : lastMoveByPlayer(player)
        return (turn == null) ? defaultColumn(player) : turn.column
    }

    private boolean playersInSameCell(Game game)
    {
        boolean sameCell = false
        game.players.each {thisPlayer ->
            if (game.players.any {thatPlayer ->
                thisPlayer.row() == thatPlayer.row() &&
                        thisPlayer.column() == thatPlayer.column() &&
                        thisPlayer.name != thatPlayer.name
            })
                sameCell = true
        }
        return sameCell
    }

    public Integer turnNumber(Game game)
    {
        def turnNumber = 0
        game.players.each {
            if (playerStatus(it, game) == 'ready')
                turnNumber = lastTurnNumberMadeByPlayer(it) + 1
        }
        return turnNumber
    }

    private Integer lastTurnNumberMadeByPlayer(Player player)
    {
        def turn = player.turns?.max()?.turnNumber

        return turn == null ? 0 : turn
    }

    private Integer lastTurnNumberMadeByOtherPlayer(Player player, Game game)
    {
        Integer turnNumber = 0
        game.players.each {
            if (it.name != player.name && lastTurnNumberMadeByPlayer(it) > turnNumber)
                turnNumber = lastTurnNumberMadeByPlayer(it)
        }
        return turnNumber
    }

    private String playerStatus(Player player, Game game)
    {
        return lastTurnNumberMadeByPlayer(player) > lastTurnNumberMadeByOtherPlayer(player, game) ? "waiting" : "ready"
    }
}

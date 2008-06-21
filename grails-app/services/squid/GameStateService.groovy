package squid

class GameStateService
{
    GameState gameState(Game game)
    {
        GameState gameState = new GameState(game)
        game.players.each {player->
//            gameState.player(player).put(GameState.PLAYER_ROW, playerRow(player, game).toString())
//            gameState.player(player).put(GameState.PLAYER_COLUMN, playerColumn(player, game).toString())
//            gameState.player(player).put(GameState.SHOT_LANDED, shotLanded(player, game).toString())
            if (shotLanded(player, game))
            {
                def fireTurn = playerStatus(player, game)=='ready'?lastTurnByPlayer(player):previousTurnByPlayer(player)
                gameState.player(player).put(GameState.SHOT_LANDED_ROW, fireTurn.row.toString())
                gameState.player(player).put(GameState.SHOT_LANDED_COLUMN, fireTurn.column.toString())
            }
        }
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
            gameState.players.each {player, value ->
                String shotRow = value.get(GameState.SHOT_LANDED_ROW)
                String shotColumn = value.get(GameState.SHOT_LANDED_COLUMN)
                if (player.shotLanded())
                {
                    if (game.players.any {it.row().toString() == shotRow && it.column().toString() == shotColumn
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
        boolean sameRow = true
        boolean sameColumn = true
        Integer lastRow, lastColumn
        game.players.each {
            def thisRow = playerRow(it, game)
            def thisColumn = playerColumn(it, game)
            if (lastRow != null && thisRow != lastRow) {sameRow = false}
            if (lastColumn != null && thisColumn != lastColumn) {sameColumn = false}
            lastRow = thisRow
            lastColumn = thisColumn
        }
        return sameRow && sameColumn
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

    Integer lastTurnNumberMadeByPlayer(Player player)
    {
        def turn = player.turns?.max()?.turnNumber

        return turn == null ? 0 : turn
    }

    Integer lastTurnNumberMadeByOtherPlayer(Player player, Game game)
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

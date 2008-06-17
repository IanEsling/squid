package squid

class GameStateService
{
    GameState gameState(Game game)
    {
        GameState gameState = new GameState(game)
        game.players.each {
            gameState.player(it).put('status', playerStatus(it, game))
            gameState.player(it).put('row', playerRow(it, game).toString())
            gameState.player(it).put('column', playerColumn(it, game).toString())
            gameState.player(it).put('shotLanded', shotLanded(it, game).toString())
            if (shotLanded(it, game))
            {
                def fireTurn = player.turns.findAll {
                    it.turnType == Turn.FIRE
                }.max()
                gameState.player(it).put('shotLandedRow', fireTurn.row)
                gameState.player(it).put('shotLandedColumn', fireTurn.column)
            }
        }
        gameState.turnNumber = turnNumber(game)
//        if (playersInSameCell(game))
//        {
//            gameState.gameOver = true
//            gameState.winner = GameState.DRAW
//        }
//        if (playerAHasWon(game, gameState) || playerBHasWon(game, gameState))
//        {
//            gameState.gameOver = true
//            gameState.winner = playerAHasWon(game, gameState) ?
//                (playerBHasWon(game, gameState) ? GameState.DRAW : GameState.PLAYER_A) :
//                GameState.PLAYER_B
//        }
        return gameState
    }

//    boolean playerAHasWon(Game game, GameState gameState)
//    {
//        if (shotLanded('A', game) && gameState.playerBStatus == 'ready')
//        {
//            def turn = lastTurnByPlayer('A', game)
//            return (turn.row == playerRow('B', game) && turn.column == playerColumn('B', game))
//        }
//    }
//
//    boolean playerBHasWon(Game game, GameState gameState)
//    {
//        if (shotLanded('B', game) && gameState.playerAStatus == 'ready')
//        {
//            def turn = lastTurnByPlayer('B', game)
//            return (turn.row == playerRow('A', game) && turn.column == playerColumn('A', game))
//        }
//    }

    boolean shotLanded(Player player, Game game)
    {
        return ((player.turns?.max()?.turnType == Turn.FIRE && playerStatus(player, game) == 'ready')
                ||
                (previousMoveByPlayer(player)?.turnType) == Turn.FIRE && playerStatus(player, game) == 'waiting')
    }

    boolean shotLandedInRow(Player player, Integer row, Game game)
    {
        if (shotLanded(player, game))
        {
            return player.turns?.findAll {
                (it.turnType == Turn.FIRE)
            }?.max()?.row == row
        }
    }

    boolean shotLandedInColumn(Player player, Integer column, Game game)
    {
        if (shotLanded(player, game))
        {
            return game.turns?.findAll {
                (it.player == player
                        && it.turnType == Turn.FIRE)
            }?.max()?.column == column
        }
    }

    private Turn lastTurnByPlayer(Player player, Game game)
    {
        game.turns?.findAll {it?.player == player}?.max()
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
            if (lastRow != null && it.row != lastRow) {sameRow = false}
            if (lastColumn != null && it.column != lastColumn) {sameColumn = false}
            lastRow = it.row
            lastColumn = it.column
        }
        return sameRow && sameColumn
    }

    public Integer turnNumber(Game game)
    {
        def turnNumber = 0
        game.players.each {
            if (playerStatus(it, game)=='ready')
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

    private String otherPlayer(String player)
    {
        return player == 'A' ? 'B' : 'A'
    }
}

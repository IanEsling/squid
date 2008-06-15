package squid
class GameStateService
{
    GameState gameState(Game game)
    {
        GameState gameState = new GameState()
        gameState.gameId = game?.id
        gameState.turnNumber = turnNumber(game)
        gameState.playerAStatus = playerStatus('A', game)
        gameState.playerBStatus = playerStatus('B', game)
        gameState.playerARow = playerRow('A', game)
        if (shotLanded('A', game))
        {
            def turn = game.turns.findAll {
                (it.player == 'A'
                        && it.turnType == Turn.FIRE)
            }.max()
            gameState.playerAShotRow = turn.row
            gameState.playerAShotColumn = turn.column
        }
        gameState.playerAColumn = playerColumn('A', game)

        gameState.playerBRow = playerRow('B', game)
        if (shotLanded('B', game))
        {
            def turn = game.turns.findAll {
                (it.player == 'B'
                        && it.turnType == Turn.FIRE)
            }.max()
            gameState.playerBShotRow = turn.row
            gameState.playerBShotColumn = turn.column
        }
        gameState.playerBColumn = playerColumn('B', game)
        if (playersInSameCell(game))
        {
            gameState.gameOver = true
            gameState.winner = GameState.DRAW
        }
        if (playerAHasWon(game, gameState) || playerBHasWon(game, gameState))
        {
            gameState.gameOver = true
            gameState.winner = playerAHasWon(game, gameState) ?
                (playerBHasWon(game, gameState) ? GameState.DRAW : GameState.PLAYER_A) :
                GameState.PLAYER_B
        }
        return gameState
    }

    boolean playerAHasWon(Game game, GameState gameState)
    {
        if (shotLanded('A', game) && gameState.playerBStatus == 'ready')
        {
            def turn = lastTurnByPlayer('A', game)
            return (turn.row == playerRow('B', game) && turn.column == playerColumn('B', game))
        }
    }

    boolean playerBHasWon(Game game, GameState gameState)
    {
        if (shotLanded('B', game) && gameState.playerAStatus == 'ready')
        {
            def turn = lastTurnByPlayer('B', game)
            return (turn.row == playerRow('A', game) && turn.column == playerColumn('A', game))
        }
    }

    boolean shotLanded(String player, Game game)
    {
        if (playerStatus(player, game) == 'ready')
        {
            return game.turns?.findAll {it.player == player}?.max()?.turnType == Turn.FIRE
        }
    }

    boolean shotLandedInRow(String player, Integer row, Game game)
    {
        if (shotLanded(player, game))
        {
            return game.turns?.findAll {
                (it.player == player
                        && it.turnType == Turn.FIRE)
            }?.max()?.row == row
        }
    }

    boolean shotLandedInColumn(String player, Integer column, Game game)
    {
        if (shotLanded(player, game))
        {
            return game.turns?.findAll {
                (it.player == player
                        && it.turnType == Turn.FIRE)
            }?.max()?.column == column
        }
    }

    private Turn lastTurnByPlayer(String player, Game game)
    {
        game.turns?.findAll {it?.player == player}?.max()
    }

    private Turn previousMoveByPlayer(String player, Game game)
    {
        game.turns?.findAll {
            (it?.player == player
                    && it?.turnType == Turn.MOVE
                    && it?.turnNumber < lastTurnNumberMadeByPlayer(player, game))
        }?.max()
    }

    private Turn lastMoveByPlayer(String player, Game game)
    {
        game.turns?.findAll {
            (it?.player == player
                    && it.turnType == Turn.MOVE)
        }?.max()
    }

    private Integer defaultRow(String player, Game game)
    {
        return player.equals("A") ? 1 : game.rows
    }

    private Integer defaultColumn(String player, Game game)
    {
        return player.equals("A") ? 1 : game.columns
    }

    public Integer playerRow(String player, Game game)
    {
        def turn = playerStatus(player, game).equals("waiting") ? previousMoveByPlayer(player, game) : lastMoveByPlayer(player, game)
        return (turn == null) ? defaultRow(player, game) : turn.row
    }

    public Integer playerColumn(String player, Game game)
    {
        def turn = playerStatus(player, game).equals("waiting") ? previousMoveByPlayer(player, game) : lastMoveByPlayer(player, game)
        return (turn == null) ? defaultColumn(player, game) : turn.column
    }

    private boolean playersInSameCell(Game game)
    {
        return playerRow('A', game) == playerRow('B', game) && playerColumn('A', game) == playerColumn('B', game)
    }

    public Integer turnNumber(Game game)
    {
        return lastTurnNumberMadeByPlayer('A', game) < lastTurnNumberMadeByPlayer('B', game) ?
            lastTurnNumberMadeByPlayer('A', game) + 1 :
            lastTurnNumberMadeByPlayer('B', game) + 1
    }

    Integer lastTurnNumberMadeByPlayer(String player, Game game)
    {
        def turn = game.turns?.findAll {
            it.player == player
        }?.max()?.turnNumber

        return turn == null ? 0 : turn
    }

    private String playerStatus(String player, Game game)
    {
        return lastTurnNumberMadeByPlayer(player, game) > lastTurnNumberMadeByPlayer(otherPlayer(player), game) ? "waiting" : "ready"
    }

    private String otherPlayer(String player)
    {
        return player == 'A' ? 'B' : 'A'
    }
}

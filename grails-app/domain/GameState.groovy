class GameState
{
    public final static String DRAW = 'Draw'
    public final static String PLAYER_A = 'PlayerA'
    public final static String PLAYER_B = 'PlayerB'

    String playerAStatus
    String playerBStatus
    Integer playerARow
    Integer playerAShotRow
    Integer playerAColumn
    Integer playerAShotColumn
    Integer playerBRow
    Integer playerBColumn
    Integer playerBShotRow
    Integer playerBShotColumn
    Integer turnNumber
    boolean gameOver = false
    String winner
    Integer gameId

    GameState() {}

    GameState(Game game)
    {
        gameId = game?.id
        turnNumber = turnNumber(game)
        playerAStatus = playerStatus('A', game)
        playerBStatus = playerStatus('B', game)
        playerARow = playerRow('A', game)
        if (shotLanded('A', game))
        {
            def turn = game.turns.findAll {
                (it.player == 'A'
                        && it.turnType == Turn.FIRE)
            }.max()
            playerAShotRow = turn.row
            playerAShotColumn = turn.column
        }
        playerAColumn = playerColumn('A', game)

        playerBRow = playerRow('B', game)
        if (shotLanded('B', game))
        {
            def turn = game.turns.findAll {
                (it.player == 'B'
                        && it.turnType == Turn.FIRE)
            }.max()
            playerBShotRow = turn.row
            playerBShotColumn = turn.column
        }
        playerBColumn = playerColumn('B', game)
        if (playersInSameCell(game))
        {
            gameOver = true
            winner = DRAW
        }
        if (playerAHasWon(game) || playerBHasWon(game))
        {
            gameOver = true
            winner = playerAHasWon(game) ?
                (playerBHasWon(game) ? DRAW : PLAYER_A) :
                PLAYER_B
        }
    }

    boolean playerAHasWon(Game game)
    {
        if (shotLanded('A', game) && playerBStatus == 'ready')
        {
            def turn = lastTurnByPlayer('A', game)
            return (turn.row == playerRow('B', game) && turn.column == playerColumn('B', game))
        }
    }

    boolean playerBHasWon(Game game)
    {
        if (shotLanded('B', game) && playerAStatus == 'ready')
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

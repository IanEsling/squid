package squid

import squid.Game
import squid.Turn

class SquidController
{
    def defaultAction = "squid"

    def squid = { currentGame.call() }

    def latestGame = {
        Game.list().max()
    }

    def currentGame = {player ->
        if (Game.list().size() == 0) return null

        [gameState: latestGame.call().currentGameState(), player: player]
    }

    def move = {
        currentGame.call(params.player)
    }

    def newGame = {
        String playerA = paramMissing('playerA') ? "PlayerA" : params.playerA
        String playerB = paramMissing('playerB') ? "PlayerB" : params.playerB
        def rows = paramMissing('rows') ? 10 : Integer.valueOf(params.rows)
        def columns = paramMissing('columns') ? 10 : Integer.valueOf(params.columns)
        def game = new Game(rows, columns, playerA, playerB)
        game.save(flush: true)

        redirect(uri: "/squid")
    }

    def submitOrder = {
        order(params)
        redirect(uri: "/squid")
    }

    def order = {
        def game = Game.get(params.gameId)
        game.newTurn(new Turn(Integer.valueOf(params.row), Integer.valueOf(params.column), params.turnType), params.player)
    }

    boolean paramMissing(property)
    {
        params?.get(property)==""||params?.get(property)==null
    }
}

class SquidController
{

    def defaultAction = "squid"

    def squid = { currentGame.call() }

    def currentGame = {player ->
        if (Game.findAll().size() == 0) return null

        def game = Game.findAll().max()
        [gameState: game.currentGameState(), game:game, player:player, playerName:game.playerName(player)]
    }

    def move = {
        currentGame.call(params.player)
    }

    def playerA = {
        currentGame.call('A')
    }

    def playerB = {
        currentGame.call('B')
    }

    def newGame = {
        if (params.playerA == "") params.remove("playerA")
        if (params.playerB == "") params.remove("playerB")
        if (params.rows == "") params.remove("rows")
        if (params.columns == "") params.remove("columns")
        def game = new Game(params)
        game.save(flush: true)

        redirect(uri: "/squid")
    }

    def submitOrder = {
        order(params)
        redirect(uri: "/squid")
    }

    def order = {
        def game = Game.get(params.gameId)
        game.newTurn(new Turn(params.player, Integer.valueOf(params.row), Integer.valueOf(params.column), params.turnType, game))
    }
}

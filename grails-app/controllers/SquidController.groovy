class SquidController
{

    def defaultAction = "squid"

    def squid = { currentGame.call() }

    def currentGame = {
        if (Game.findAll().size() == 0) return null

        return [game: Game.findAll().max().currentGameState()]
    }

    def playerA = {
        currentGame.call()
    }

    def playerB = {
        currentGame.call()
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
        game.newTurn(new Turn(params.player, params.row, params.column, params.turnType, game))
    }
}

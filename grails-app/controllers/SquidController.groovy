import org.springframework.web.servlet.ModelAndView

class SquidController {

    def defaultAction = "squid"

    def squid = { currentGame.call() }

    def currentGame = {
        if (Game.findAll().size() == 0) return null

        return [game: Game.findAll().max().status()]
    }

    def newGame = {
        def game = new Game(params)
        if (game.validate())
            game.save(flush: true)

        redirect(uri: "/squid")
    }

    def submitOrder = {
        order(params)
        redirect(uri: "/squid")
    }
    
    def order = {
        def game = Game.get(params.gameId)
        def turn = new Turn(params)
        turn.turnNumber = game.lastTurnNumberMadeByPlayer(params.player) + 1
        game.addToTurns(turn)
        game.save(flush: true)
    }
}

class SquidController {

    def index = { currentGame.call() }

    def currentGame = {
        if (Game.findAll().size() == 0) newGame.call()
        else
            render(view: "squid", model: [game: Game.findAll().max()])
    }

    def newGame = {
        def game = new Game(playerA: "A", playerB: "B", rows: 6, columns: 15)
        game.save(flush: true)
        redirect(uri:"/squid")

    }

    def order = {OrderForm form ->
        def game = Game.get(form.gameId)
        game.addToTurns(new Turn(player:form.player, row:form.row, column:form.column, turnNumber:game.lastTurnNumberMadeByPlayer(form.player)+1))
        game.save(flush: true)
    }

    public String orderStatus(Game game) {
        (game.lastTurnNumberMadeByPlayer("A") == game.lastTurnNumberMadeByPlayer("B")) ?
                "resolved" :
                "waiting"
    }

    String otherPlayer(String player) {
        player.equals("A") ? "B" : "A"
    }

    String playerAPosition() {
        Turn.findAllByPlayer("A").max().moveTo
    }

    String playerBPosition() {
        Turn.findAllByPlayer("B").max().moveTo
    }
}

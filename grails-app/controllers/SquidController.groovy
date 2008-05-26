import org.springframework.web.servlet.ModelAndView

class SquidController {

    def defaultAction = "squid"

    def squid = { currentGame.call() }

    def currentGame = {
        if (Game.findAll().size() == 0) return createNewGame()

        return [game: Game.findAll().max()]
    }

    def newGame = {
        def game = new Game(params)
        if (game.validate())
            game.save(flush: true)
        else {
            return render(view:'/squid/errors', bean:game)
        }

        redirect(uri: "/squid")
    }

    private Game createNewGame() {
        return new Game(playerA: playerAName(), playerB: playerBName(), rows: rows(), columns: columns())
    }

    private def playerAName() {
        params['playerA'].equals("") ? "A" : request.parameters.playerA
    }

    private def playerBName() {
        params['playerB'].equals("") ? "B" : request.parameters.playerB
    }

    private def columns() {
        params['columns'].equals("") ? 10 : request.parameters.columns
    }

    private def rows() {
        params['rows'].equals("") ? 10 : request.parameters.rows
    }

    def order = {OrderForm form ->
        def game = Game.get(form.gameId)
        game.addToTurns(new Turn(player: form.player, row: form.row, column: form.column, turnNumber: game.lastTurnNumberMadeByPlayer(form.player) + 1))
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

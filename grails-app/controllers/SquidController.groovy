class SquidController {

    def orderStatus

    def newGame = {
        def game =  new Game(playerA:"A", playerB:"B", rows:10, columns:10)
        game.save(flush:true)
        render(view:"/squid/squid", model:[game:game])
    }

    def order = {OrderForm form ->
        def turn = new Turn(player: form.player, turnNumber: form.turnNumber, moveTo: form.moveTo)
        turn.save(flush: true)
        orderStatus(form)
    }

    private String orderStatus(OrderForm form) {
        def turns = Turn.findAllByPlayer(otherPlayer(form.player))
        orderStatus = (turns.size() == 0) ? "waiting" :
            ((turns.max().turnNumber == form.turnNumber) ?
                "resolved" :
                "waiting")
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

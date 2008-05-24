class SquidController {

    def orderStatus

    def index = { }

    def order = {OrderForm form ->
        def turn = new Turn(player: form.player, turnNumber: form.turnNumber, moveTo: form.moveTo)
        turn.save(flush: true)
        orderStatus(form)
    }

    private String orderStatus(OrderForm form) {
        orderStatus = (lastTurnForPlayer(otherPlayer(form.player)) == form.turnNumber) ? "resolved" : "waiting"
    }

    String otherPlayer(String player) {
        player.equals("A") ? "B" : "A"
    }

    Integer lastTurnForPlayer(String player) {
        def i = 0
        Turn.list().each {Turn turn ->
            if (turn.player.equals(player) && turn.turnNumber > i)
                i = turn.turnNumber
        }
        return i
    }

    String playerAPosition() {
        Turn.findByPlayerAndTurnNumber("A", lastTurnForPlayer("A")).moveTo
    }

    String playerBPosition() {
        Turn.findByPlayerAndTurnNumber("B", lastTurnForPlayer("B")).moveTo
    }
}

class SquidController {
    def currentPosition, currentTurn

    def index = { }

    def order = {OrderForm form ->
        currentPosition = form.moveTo
        currentTurn = form.turnNumber
    }    
}

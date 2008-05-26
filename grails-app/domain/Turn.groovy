class Turn {
    String player
    Integer turnNumber
    Integer row
    Integer column

    static constraints = {
        player(nullable:false)
        turnNumber(nullable:false)
        row(nullable:false)
        column(nullable:false)
    }
}

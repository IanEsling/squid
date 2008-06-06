class Turn {
    
    String player
    Integer turnNumber
    Integer row
    Integer column
    String turnType

    static constraints = {
        player(nullable: false)
        turnNumber(nullable: false)
        row(nullable: false)
        column(nullable: false)
        turnType(inList:['Move','Fire'])
    }
}
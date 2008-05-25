class Turn implements Comparable {
    String player
    Integer turnNumber
    String moveTo

    public int compareTo(Object o) {
        if (o instanceof Turn)
        {
            def turn = (Turn)o
            if (turn.player.equals(player))
                return turnNumber - turn.turnNumber
        }
        return 0
    }
}

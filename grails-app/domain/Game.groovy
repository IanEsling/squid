class Game implements Comparable {
    static hasMany = [turns : Turn]

    String playerA
    String playerB
    Integer rows
    Integer columns

    public int compareTo(Object o) {
        if (o instanceof Game)
        {
            def game = (Game)o
            return id - o.id
        }
        return 0
    }
}

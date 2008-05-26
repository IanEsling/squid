class GameTests extends GroovyTestCase {

    void testGameOrdering() {
        new Game(playerA:"A", playerB:"B", rows:10, columns:10).save(flush:true)
        new Game(playerA:"A", playerB:"B", rows:40, columns:10).save(flush:true)
        new Game(playerA:"A", playerB:"B", rows:20, columns:10).save(flush:true)
        new Game(playerA:"A", playerB:"B", rows:30, columns:10).save(flush:true)

        assertEquals("games not created", Game.list().size(), 4)
        assertEquals("min game id incorrect", Game.list().min().id, Game.findByRows(10).id)
        assertEquals("max game id incorrect", Game.list().max().id, Game.findByRows(30).id)
    }
}
